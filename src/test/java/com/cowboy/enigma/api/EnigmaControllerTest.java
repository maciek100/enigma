package com.cowboy.enigma.api;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class EnigmaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void encryptsHelloWorld() throws Exception {
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
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
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
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value(containsString("Duplicate character detected: 'A'")));
    }

    @Test
    void acceptLowerCaseRotorLetters() throws Exception {
        String request = """
                {
                  "rotors":    ["i", "ii", "iii"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "HELLO"
                }
                """;
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCG"))
                .andExpect(jsonPath("$.finalPositions").value("AAF"));
    }

    @Test
    void acceptLowerCaseRingLetters() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "aaa",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "HELLO"
                }
                """;
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCG"))
                .andExpect(jsonPath("$.finalPositions").value("AAF"));
    }

    @Test
    void acceptLowerCasePositionLetters() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "aaa",
                  "reflector": "B",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "HELLO WORLD"
                }
                """;
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCGFZNCKF"));
    }

    @Test
    void acceptLowerCaseReflectorLetter() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "b",
                  "plugboard": "AT BL DF GJ HM NW OP QY RZ VX",
                  "text":      "HELLO WORLD"
                }
                """;
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCGFZNCKF"));
    }

    @Test
    void acceptLowerCasePlugboardLetter() throws Exception {
        String request = """
                {
                  "rotors":    ["I", "II", "III"],
                  "rings":     "AAA",
                  "positions": "AAA",
                  "reflector": "B",
                  "plugboard": "at bl df gj hm nw op qy rz vx",
                  "text":      "HELLO WORLD"
                }
                """;
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("FBBCGFZNCKF"));
    }

    @Test
    public void isPreparerWiredIn () throws Exception {
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
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prepared").value("HELLOXWORLD"));
    }

    @Test
    public void roundTripTest () throws Exception {
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
                        .content("{" + settings + "\"text\": \"HELLOWWORLD\"}"))
                .andExpect(status().isOk())
                .andReturn();
        String cipher = JsonPath.read(first.getResponse().getContentAsString(), "$.output");

        //2. decrypt
        // 2. decrypt: same settings, ciphertext as input
        mockMvc.perform(post("/api/enigma")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" + settings + "\"text\": \"" + cipher + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.output").value("HELLOWORLD"));
    }

}
