package com.cowboy.enigma.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.util.Locale;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class EnigmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALID = """
    { "rotors": ["I","II","III"], "rings": "AAA", "positions": "AAA",
      "reflector": "B", "plugboard": "AT BL DF GJ HM NW OP QY RZ VX", "text": "HELLO" }
    """;

    private final ObjectMapper mapper = new ObjectMapper();

    private ResultActions postJson(String json) throws Exception {
        return mockMvc.perform(post("/api/enigma")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }

    @Test
    void acceptsHelloWorld() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "BBB",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "HELLOWORLD"
                }
                """;
        postJson(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("CPQZNUMKFJ"))
                .andExpect(jsonPath("$.prepared").value("HELLOWORLD"))
                .andExpect(jsonPath("$.finalPositions").value("AAK"));
    }

    @Test
    void rejectsDuplicatePlugboardLetter() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AB AC",
                  "text":      "HELLO"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("Duplicate character detected: 'A'")));
    }

    @Test
    void acceptsAndPreparesFreeText() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello world!"
                }
                """;
        postJson(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prepared").value("HELLOXWORLD"));
    }

    @Test
    void acceptsRoundTrip() throws Exception {
        String settings = """
                  "rotors":    ["I", "II", "III"],
                  "rings":     "BBB",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                """;
        // 1. encrypt
        MvcResult first = mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + settings + "\"text\": \"HELLOWORLD\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String cipher = JsonPath.read(first.getResponse().getContentAsString(), "$.output");

        // 2. decrypt: same settings, ciphertext as input
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + settings + "\"text\": \"" + cipher + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("HELLOWORLD"));
    }

    @Test
    void barbarossaRoundTripTest() throws Exception {
        String ciphertext = "EDPUDNRGYSZRCXNUYTPOMRMBOFKTBZREZKMLXLVEFGUEYSIOZVEQMIKUBPMMYLKLTTDEISMDICAGYKUACTCDOMOHWXMUUIAUBSTSLRNBZSZWNRFXWFYSSXJZVIJHIDISHPRKLKAYUPADTXQSPINQMATLPIFSVKDASCTACDPBOPVHJK";
        String plaintext  = "AUFKLXABTEILUNGXVONXKURTINOWAXKURTINOWAXNORDWESTLXSEBEZXSEBEZXUAFFLIEGERSTRASZERIQTUNGXDUBROWKIXDUBROWKIXOPOTSCHKAXOPOTSCHKAXUMXEINSAQTDREINULLXUHRANGETRETENXANGRIFFXINFXRGTX";

        String settings = """
                  "rotors":    ["II", "IV", "V"],
                  "rings":     "BUL",
                  "positions": "BLA",
                  "reflector": "B",
                  "plugboard": "AV BS CG DL FU HZ IN KM OW RX",
                """;
        // 1. encrypt
        MvcResult first = mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + settings + "\"text\": \"" + plaintext + "\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String cipher = JsonPath.read(first.getResponse().getContentAsString(), "$.output");
        assertEquals(ciphertext, cipher);
        // 2. decrypt: same settings, ciphertext as input
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + settings + "\"text\": \"" + cipher + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value(plaintext));
    }

    @Test
    void acceptsEmptyText() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      ""
                }
                """;
        postJson(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value(""))
                .andExpect(jsonPath("$.finalPositions").value("AAA"));
    }

    @Test
    void acceptsTextThatPreparesToNothing() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "!!!!"
                }
                """;
        postJson(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPositions").value("AAA"))
                .andExpect(jsonPath("$.prepared").value(""))
                .andExpect(jsonPath("$.output").value(""));
    }

    @Test
    void rejectsDuplicateRotors() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "I", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Duplicate Rotor type:")));
    }

    @Test
    void rejectsNullRotor() throws Exception {
        String request = """
                {
                  "rotors":    ["I", null, "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Exactly 3 non-null Rotors required")));
    }

    @Test
    void rejectsWrongRotorCount() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Exactly 3 non-null Rotors required")));
    }

    @Test
    void rejectsUnknownRotor() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "VI", "II"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Unknown Rotor:")));
    }

    @Test
    void rejectsUnknownReflector() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "V", "II"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "D",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "Hello"
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Unknown Reflector:")));
    }

    @Test
    void rejectsMalformedRequest() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "V", 
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      null
                }
                """;
        postJson(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error")
                        .value(containsString("Malformed request")));
    }

    @ParameterizedTest(name = "rejects missing field {0}")
    @CsvSource(delimiter = '|', value = {
            "rotors    | Field 'rotors' is required",
            "rings     | Field 'rings' is required",
            "positions | Field 'positions' is required",
            "reflector | Field 'reflector' is required",
            "plugboard | Field 'plugboard' is required",
            "text      | Field 'text' is required"
    })
    void rejectsMissingField(String field, String message) throws Exception {
        ObjectNode body = (ObjectNode) mapper.readTree(VALID);
        body.remove(field);
        postJson(body.toString())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(message));
    }

    @ParameterizedTest(name = "rejects bad letters {0}")
    @CsvSource(delimiter = '|', value = {
            "rings     | AA   | Rings must be 3 letters A-Z: AA",
            "rings     | AA6  | Rings must be 3 letters A-Z: AA6",
            "positions | AAAA | Positions must be 3 letters A-Z: AAAA",
            "positions | A3A  | Positions must be 3 letters A-Z: A3A"
    })
    void rejectsBadLetters(String field, String value, String message) throws Exception {
        ObjectNode body = (ObjectNode) mapper.readTree(VALID);
        body.put(field, value);
        postJson(body.toString())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(message));
    }

    @ParameterizedTest(name = "rejects null field {0}")
    @ValueSource(strings = {"rotors", "rings", "positions", "reflector", "plugboard", "text"})
    void rejectsNullField(String field) throws Exception {
        ObjectNode body = (ObjectNode) mapper.readTree(VALID);
        body.putNull(field);
        postJson(body.toString())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Field '" + field + "' is required"));
    }

    @ParameterizedTest(name = "accepts lowercase {0}")
    @ValueSource(strings = {"rotors", "rings", "positions", "reflector", "plugboard"})
    void acceptsLowerCase(String field) throws Exception {
        ObjectNode body = (ObjectNode) mapper.readTree(VALID);
        String lowerJson = body.get(field).toString().toLowerCase(Locale.ROOT);
        body.set(field, mapper.readTree(lowerJson));
        postJson(body.toString())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCG"))
                .andExpect(jsonPath("$.finalPositions").value("AAF"));
    }
}
