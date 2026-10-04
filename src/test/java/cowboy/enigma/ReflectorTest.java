package cowboy.enigma;

import com.cowboy.enigma.Reflector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectorTest {
    private final String reflectorStringB = "YRUHQSLDPXNGOKMIEBFZCWVJAT";
    @Test
    public void testReflector1 () {
        Reflector reflector = new Reflector(reflectorStringB);
        System.out.println(reflector.expose());
    }

    @Test
    public void testReflector2 () {
        Reflector reflector = new Reflector(reflectorStringB);
        assertEquals('X' - 'A', reflector.reflect(reflector.reflect('X' - 'A')));
    }

    @Test
    public void testAllReflector () {
        Reflector reflector = new Reflector(reflectorStringB);
        for (int i = 0; i < 26; i++) {
            assertEquals(i, reflector.reflect(reflector.reflect(i)));
        }
    }


}
