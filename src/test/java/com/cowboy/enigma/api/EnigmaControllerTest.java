package com.cowboy.enigma.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
                .andExpect(jsonPath("$.output").value("CPQZNUMKFJ"));
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
}
