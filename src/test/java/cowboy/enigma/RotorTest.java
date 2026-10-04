package cowboy.enigma;

import com.cowboy.enigma.Rotor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RotorTest {

// ABCDEFGHIJKLMNOPQRSTUVWXYZ
    private List<String> rotorStrings = List.of(
            "EKMFLGDQVZNTOWYHXUSPAIBRCJ",
            "AJDKSIRUXBLHWTMCQGZNPYFVOE",
            "BDFHJLCPRTXVZNYEIWGAKMUSQO",
            "ESOVPZJAYQUIRHXLNFTGKDCMWB",
            "VZBRGITYUPSDNHLXAWMJQOFECK");

    @Test
    public void testRotor1() {
        String expected = "UWYGADFPVZBECKMTHXSLRINQOJ";
        Rotor rotor = new Rotor(2, rotorStrings.getFirst());
        assertEquals(expected, rotor.expose());
    }

    @Test
    public void testForward1() {
        Rotor rotor = new Rotor(0, rotorStrings.getFirst());
        int output = rotor.forward('C' - 'A', 'M' - 'A');
        assertEquals('M', output + 'A');
    }

    @Test
    public void testForward2() {
        Rotor rotor = new Rotor(3, rotorStrings.getFirst());
        int output = rotor.forward(0, 0);
        assertEquals('U', output + 'A');
    }

    @Test
    public void testBackward1() {
        Rotor rotor = new Rotor(0, rotorStrings.getFirst());
        int output = rotor.backward('M' - 'A', 'M' - 'A') ;
        assertEquals('C', output + 'A');
    }

    @Test
    public void testBackward2() {
        Rotor rotor = new Rotor(1, rotorStrings.getFirst());
        int output = rotor.backward('L' - 'A', 'M' - 'A');
        System.out.println((char)output);
        assertEquals('C', output +'A');
    }

    @Test
    public void testBackward3() {
        Rotor rotor = new Rotor(3, rotorStrings.getFirst());
        int output = rotor.backward('U' - 'A', 'A' - 'A');
        System.out.println((char)output);
        assertEquals('A', output + 'A');
    }

    @Test
    public void testForwardBackward() {
        Rotor rotor = new Rotor(0, rotorStrings.getFirst());
        char letter = 'F';
        int temp = rotor.forward(letter - 'A', 'M');
        System.out.println(temp);
        assertEquals(letter - 'A', rotor.backward(temp, 'M'));
    }

    // COMPREHENSIVE TESTS
    /*
    1. Matches the reference wiring. At position A and ring setting A,
    forward(x) == expected.charAt(x) - 'A' for all 26 letters,
    with the expected string typed in the test from Crypto Museum.
    Do this for all five rotors, since a typo can be in any of them.
     */
    @Test
    public void testReferenceWiring() {
        for (String rotorString : rotorStrings) {
            Rotor rotor = new Rotor(0, rotorString);
            char[] rsArray = rotorString.toCharArray();
            for (int i = 0; i < 26; i++) {
                assertEquals(rotor.forward(i, 0), rsArray[i] - 'A');
            }
        }
    }
}
