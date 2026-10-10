package cowboy.enigma;

import com.cowboy.enigma.Reflector;
import com.cowboy.enigma.ReflectorType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReflectorTest {
    private static final String UKW_B = "YRUHQSLDPXNGOKMIEBFZCWVJAT";

    @Test
    public void testReflector1 () {
        Reflector reflector = new Reflector(ReflectorType.B);
        for(int i = 0; i < 26; i++) {
            assertEquals(UKW_B.charAt(i) - 'A', reflector.reflect(i),
                    String.format("letter '%c' should reflect to '%c'", (char) ('A' + i), UKW_B.charAt(i)));
        }
    }

    @Test
    public void testReflector2 () {
        Reflector reflector = new Reflector(ReflectorType.B);
        assertEquals(0, reflector.reflect(24));
    }

    @Test
    public void testAllReflector () {
        Reflector reflector = new Reflector(ReflectorType.B);
        for (int i = 0; i < 26; i++) {
            assertEquals(i, reflector.reflect(reflector.reflect(i)),
                    "Incorrect reflector at index " + i);
            assertNotEquals(i, reflector.reflect(i),
                    "letter " + i + " reflects to itself.");
        }
    }

    @Test
    public void testReflectOutOfBoundsLow () {
        Reflector reflector = new Reflector(ReflectorType.A);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> reflector.reflect(-1));
        assertTrue(iae.getMessage().contains("Reflector position must be between [0, 25]"));
    }
    @Test
    public void testReflectOutOfBoundsHigh () {
        Reflector reflector = new Reflector(ReflectorType.A);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> reflector.reflect(26));
        assertTrue(iae.getMessage().contains("Reflector position must be between [0, 25]"));
    }
}
