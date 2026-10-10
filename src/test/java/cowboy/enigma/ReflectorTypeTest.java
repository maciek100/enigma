package cowboy.enigma;

import com.cowboy.enigma.ReflectorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectorTypeTest {
    @Test
    public void everyReflectorTypeIsAValidReflector() {
        for (ReflectorType type : ReflectorType.values()) {
            String w = type.getWiring();
            assertEquals(26, w.length(), type + " length");
            for (int i = 0; i < 26; i++) {
                char c = w.charAt(i);
                assertTrue(c >= 'A' && c <= 'Z', type + " bad character '" + c + "'");
                assertNotEquals('A' + i, c, type + " maps " + (char) ('A' + i) + " to itself");
                assertEquals('A' + i, w.charAt(c - 'A'), type + " not symmetric at " + (char) ('A' + i));
            }
        }
    }
}
