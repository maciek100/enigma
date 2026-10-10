package cowboy.enigma;

import com.cowboy.enigma.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnigmaMachineTest {

    private EnigmaMachine enigmaMachine;

    @BeforeEach
    public void setUp() {
        Rotor[] rotorOrder = new Rotor[] {
                new Rotor(RotorType.I, 0),
                new Rotor(RotorType.II, 0),
                new Rotor(RotorType.III, 0)
        };

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                Plugboard.empty());
    }

    @Test
    public void testEnigmaMachine01() {
        WindowPositions expectedBefore = new WindowPositions(0, 3, 20);
        WindowPositions expectedAfterStep1 = new WindowPositions(0, 3, 21);
        WindowPositions expectedAfterStep2 = new WindowPositions(0, 4, 22);
        WindowPositions expectedAfterStep3 = new WindowPositions(1, 5, 23);
        WindowPositions expectedAfterStep4 = new WindowPositions(1, 5, 24);

        enigmaMachine.setWindowPositions(new WindowPositions(0, 3, 20));
        assertEquals(expectedBefore, enigmaMachine.getPositions(), "Expected before");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep1, enigmaMachine.getPositions(), "Expected after step 1");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep2, enigmaMachine.getPositions(), "Expected after step 2");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep3, enigmaMachine.getPositions(),"Expected after step 3");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep4, enigmaMachine.getPositions(),"Expected after step 4");
    }

    @Test
    public void testEnigmaEncoding() {
        String input = "AAAAA";
        StringBuilder result = new StringBuilder();
        enigmaMachine.setWindowPositions(new WindowPositions(0, 0, 0));
        for (int i = 0; i < input.length(); i++) {
            enigmaMachine.stepRotors();
            result.append((char)(enigmaMachine.encode(input.charAt(i) - 'A') + 'A'));
        }
        assertEquals("BDZGO", result.toString());
    }

    @Test
    public void testEnigmaEncryption01() {
        String input = "AAAAA";
        String expectedMid = "BDZGO";
        WindowPositions rPositions = new WindowPositions(0, 0, 0);
        enigmaMachine.setWindowPositions(rPositions);
        StringBuilder midway = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            midway.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertNotEquals(input, midway.toString());
        for (int i = 0; i < expectedMid.length(); i++) {
            assertNotEquals(input.charAt(i), midway.charAt(i));
        }
        enigmaMachine.setWindowPositions(rPositions);
        for (char character : midway.toString().toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(input, result.toString());
    }
    @Test
    public void testEnigmaEncryption02() {
        String input = "ELANAANDLUPAARETHEBESTFRIENDS";
        WindowPositions rPositions = new WindowPositions(0, 0, 0);
        enigmaMachine.setWindowPositions(rPositions);
        StringBuilder midway = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            midway.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        enigmaMachine.setWindowPositions(rPositions);
        for (char character : midway.toString().toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(input, result.toString());
    }

    @Test
    public void testEnigmaEncryption03() {
        String input = "AAAAA";
        String expectedMid = "EWTYX";

        Rotor[] rotorOrder = new Rotor[] {
                new Rotor(RotorType.I, 1),
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1)
        };

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                Plugboard.empty());

        WindowPositions rPositions = new WindowPositions(0, 0, 0);
        enigmaMachine.setWindowPositions(rPositions);
        StringBuilder midway = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            midway.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(expectedMid, midway.toString());
        enigmaMachine.setWindowPositions(rPositions);
        for (char character : midway.toString().toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(input, result.toString());
    }

    @Test
    public void testEnigmaEncryption04() {
        String input = "AAAAA";
        String expectedMid = "PGQPW";
        WindowPositions rPositions = new WindowPositions(1, 1, 1);
        enigmaMachine.setWindowPositions(rPositions);
        StringBuilder midway = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            midway.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(expectedMid, midway.toString());
        enigmaMachine.setWindowPositions(rPositions);
        for (char character : midway.toString().toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(input, result.toString());
    }

    @Test
    public void testEnigmaEncryptionWithPlugboard() {
        String input = "HELLOWORLD";
        String expected = "CPQZNUMKFJ";
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.I, 1),
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1)
        };
        Plugboard plugboard = new Plugboard("AT BL DF GJ HM NW OP QY RZ VX");

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                plugboard);
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(expected, result.toString());
    }

    @Test
    public void testEnigmaDecryptionWithPlugboard() {
        String input = "CPQZNUMKFJ";
        String expected = "HELLOWORLD";
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.I, 1),
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1)
        };
        Plugboard plugboard = new Plugboard("AT BL DF GJ HM NW OP QY RZ VX");

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                plugboard);
        StringBuilder result = new StringBuilder();
        for (char character : input.toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(expected, result.toString());
    }

    @Test
    public void testEnigmaWarMessage() {
        String ciphertext = "EDPUDNRGYSZRCXNUYTPOMRMBOFKTBZREZKMLXLVEFGUEYSIOZVEQMIKUBPMMYLKLTTDEISMDICAGYKUACTCDOMOHWXMUUIAUBSTSLRNBZSZWNRFXWFYSSXJZVIJHIDISHPRKLKAYUPADTXQSPINQMATLPIFSVKDASCTACDPBOPVHJK";
        String plaintext  = "AUFKLXABTEILUNGXVONXKURTINOWAXKURTINOWAXNORDWESTLXSEBEZXSEBEZXUAFFLIEGERSTRASZERIQTUNGXDUBROWKIXDUBROWKIXOPOTSCHKAXOPOTSCHKAXUMXEINSAQTDREINULLXUHRANGETRETENXANGRIFFXINFXRGTX";
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.IV, 20),
                new Rotor(RotorType.V, 11)
        };
        Plugboard plugboard = new Plugboard("AV BS CG DL FU HZ IN KM OW RX");

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                plugboard);
        WindowPositions rPositions = new WindowPositions(1, 11, 0);
        enigmaMachine.setWindowPositions(rPositions);
        StringBuilder result = new StringBuilder();
        for (char character : ciphertext.toCharArray()) {
            result.append((char)(enigmaMachine.encrypt(character - 'A') + 'A'));
        }
        assertEquals(plaintext, result.toString());
    }

    @Test
    public void testConstructorNoNullRotors() {
        Reflector reflector = new Reflector(ReflectorType.A);
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(null,reflector, plugboard));
        assertTrue(iae.getMessage().contains("Rotors must not be null"));
    }

    @Test
    public void testConstructorNoNullReflector() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1),
                new Rotor(RotorType.IV, 20)
        };
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,null, plugboard));
        assertTrue(iae.getMessage().contains("Reflector must not be null"));
    }

    @Test
    public void testConstructorNoNullPlugboard() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1),
                new Rotor(RotorType.IV, 20)
        };
        Reflector reflector = new Reflector(ReflectorType.A);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,reflector, null));
        assertTrue(iae.getMessage().contains("Plugboard must not be null"));
    }

    @Test
    public void testConstructorAllRotorsCorrect() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1),
                null
        };
        Reflector reflector = new Reflector(ReflectorType.A);
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,reflector, plugboard));
        assertTrue(iae.getMessage().contains("Exactly 3 non-null Rotors required"));
    }

    @Test
    public void testConstructorOnlyTwoRotors() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1)
        };
        Reflector reflector = new Reflector(ReflectorType.A);
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,reflector, plugboard));
        assertTrue(iae.getMessage().contains("Exactly 3 non-null Rotors required"));
    }

    @Test
    public void testConstructorFourRotors() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.I, 1),
                new Rotor(RotorType.II, 1),
                new Rotor(RotorType.III, 1),
                new Rotor(RotorType.IV, 20)
        };
        Reflector reflector = new Reflector(ReflectorType.A);
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,reflector, plugboard));
        assertTrue(iae.getMessage().contains("Exactly 3 non-null Rotors required"));
    }

    @Test
    public void testCorrectWindowPositions() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> enigmaMachine.setWindowPositions(null));
        assertTrue(iae.getMessage().contains("Window positions must not be null"));
    }

    @Test
    public void testConstructorCorrectUniqueRotors() {
        Rotor[] rotorOrder = new Rotor[]{
                new Rotor(RotorType.III, 1),
                new Rotor(RotorType.IV, 20),
                new Rotor(RotorType.IV, 11)
        };
        Reflector reflector = new Reflector(ReflectorType.A);
        Plugboard plugboard = new Plugboard("AB CD");
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new EnigmaMachine(rotorOrder,reflector, plugboard));
        assertTrue(iae.getMessage().contains("Duplicate Rotor type"));
    }

    @Test
    public void testEncodeTooLow() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> enigmaMachine.encode(-1));
        assertTrue(iae.getMessage().contains("Window positions must not be null"));
    }

    @Test
    public void testEncodeTooHigh() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> enigmaMachine.encode(26));
        assertTrue(iae.getMessage().contains("Window positions must not be null"));
    }


}
