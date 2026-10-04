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
    /*
    2. Round trip everywhere. Nested loops over position (26) × ring setting (26) × input (26):
    backward(forward(x, pos), pos) == x. Put pos, ring and x in the failure message so
    a failure tells you exactly where.
     */
    @Test
    public void testRoundTripEverywhere() {
        for (String rotorString : rotorStrings) {
            for(int ringSetting = 0; ringSetting < 26; ringSetting++) {
                Rotor rotor = new Rotor(ringSetting, rotorString);
                for(int position = 0; position < 26; position++) {
                    for (int input = 0; input < 26; input++) {
                        assertEquals(input,rotor.backward(rotor.forward(input, position), position),
                                "rotor=" + rotorString
                                        + " ring=" + ringSetting
                                        + " position=" + position
                                        + " x=" + input);
                    }
                }
            }
        }
    }

    /*
    3. Each setting is a full permutation. For each position and ring setting, the 26 outputs of forward
    must all be different. Collect them in a boolean[26] or a Set and check that all 26 slots are filled.
    This catches a bad modulo or offset that sends two inputs to the same output, which the round trip
    alone might not reveal if backward has the same bug.
     */
    @Test
    public void testRoundTripFullUniquePermutationForward() {
        for (String rotorString : rotorStrings) {
            for(int ringSetting = 0; ringSetting < 26; ringSetting++) {
                Rotor rotor = new Rotor(ringSetting, rotorString);
                for(int position = 0; position < 26; position++) {
                    boolean [] result = new boolean[26];
                    for (int input = 0; input < 26; input++) {
                        int index = rotor.forward(input, position);
                        result[index] = !result[index];
                    }
                    for (int i = 0; i < 26; i++) {
                        assertTrue(result[i], "rotor= " + rotorString + " output "
                                + (char)('A' + i) + " was never produced.");
                    }
                }
            }
        }
    }
    @Test
    public void testRoundTripFullUniquePermutationBackward() {
        for (String rotorString : rotorStrings) {
            for(int ringSetting = 0; ringSetting < 26; ringSetting++) {
                Rotor rotor = new Rotor(ringSetting, rotorString);
                for(int position = 0; position < 26; position++) {
                    boolean [] result = new boolean[26];
                    for (int input = 0; input < 26; input++) {
                        int index = rotor.backward(input, position);
                        result[index] = !result[index];
                    }
                    for (int i = 0; i < 26; i++) {
                        assertTrue(result[i], "rotor= " + rotorString + " output "
                                + (char)('A' + i) + " was never produced.");
                    }
                }
            }
        }
    }
}
