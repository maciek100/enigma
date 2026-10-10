package cowboy.enigma;

import com.cowboy.enigma.Plugboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlugboardTest {
    private static final String PAIRS = "AT BL DF GJ HM NW OP QY RZ VX";
    @Test
    public void testPlugboard01() {
        Plugboard plugboard = new Plugboard("");
        for (int i = 0; i < 26; i++) {
            assertEquals(i, plugboard.swap(i));
            assertEquals(i, plugboard.swap(plugboard.swap(i)));
        }
    }

    @Test
    public void testPlugboard02() {
        Plugboard plugboard = new Plugboard(PAIRS);
        for (int i = 0; i < 26; i++) {
            assertEquals(i, plugboard.swap(plugboard.swap(i)));
        }
    }
    @Test
    public void testPlugboard03() {
        Plugboard plugboard = new Plugboard(PAIRS);
        assertEquals(0, plugboard.swap(19));
        assertEquals(19, plugboard.swap(0));
        assertEquals(idx('V'), plugboard.swap(idx('X')));
        assertEquals(idx('X'), plugboard.swap(idx('V')));

    }

    @Test
    public void testPlugboard04() {
        Plugboard plugboard = new Plugboard(PAIRS);
        assertEquals(2, plugboard.swap(2));
        assertEquals(20, plugboard.swap(20));

    }

    @Test
    public void testRejectStartingWithSpace() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard(" AB CD"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: ' '"));
    }
    @Test
    public void testRejectIncorrectCharacter() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("ABC"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: 'C'"));
    }
    @Test
    public void testRejectDoubleSpace() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB  CD"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: ' '"));
    }
    @Test
    public void testRejectTrailingSpace() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB CD "));
        assertTrue(iae.getMessage().contains("Incorrect character detected: ' '"));
    }

    @Test
    public void testRejectLetterUsedTwiceFirstPosition() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB AC"));
        assertTrue(iae.getMessage().contains("Duplicate character detected: 'A'"));
    }
    @Test
    public void testRejectIncompletePair() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB C"));
        assertTrue(iae.getMessage().contains("Incomplete pair detected: 'C'"));
    }
    @Test
    public void testRejectLetterUsedTwiceSecondPosition() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB VB"));
        assertTrue(iae.getMessage().contains("Duplicate character detected: 'B'"));
    }
    @Test
    public void testRejectInvalidCharacter() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("aB aC"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: 'a'"));
        iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB 3C"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: '3'"));
    }
    @Test
    public void testRejectInvalidSeparator() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("A:B H:C"));
        assertTrue(iae.getMessage().contains("Incorrect character detected: ':'"));
    }
    @Test
    public void acceptsValidPairsTest() {
        Plugboard p = assertDoesNotThrow(() -> new Plugboard("AD FM"));
        assertEquals('D' - 'A', p.swap('A' - 'A'));
        assertEquals('A' - 'A', p.swap('D' - 'A'));
        assertEquals('B' - 'A', p.swap('B' - 'A'));
    }
    @Test
    public void acceptsEmptyPairTest() {
        Plugboard p = assertDoesNotThrow(() -> new Plugboard(""));
        assertEquals('D' - 'A', p.swap('D' - 'A'));
        assertEquals('A' - 'A', p.swap('A' - 'A'));
        assertEquals('Z' - 'A', p.swap('Z' - 'A'));
    }
    @Test
    public void acceptsFullPairingTest() {
        Plugboard p = assertDoesNotThrow(() -> new Plugboard("AB CD EF GH IJ KL MN OP QR ST UV WX YZ"));
        assertEquals('E' - 'A', p.swap('F' - 'A'));
        assertEquals('Q' - 'A', p.swap('R' - 'A'));
        assertEquals('P' - 'A', p.swap('O' - 'A'));
    }
    @Test
    public void testRejectLetterPluggedToItself() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new Plugboard("AB HH"));
        assertTrue(iae.getMessage().contains("Letter plugged to itself detected: 'H'"));
    }

    @Test
    public void testSwapTooLow() {
        Plugboard plugboard = new Plugboard(PAIRS);
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> plugboard.swap(-1));
        assertTrue(iae.getMessage().contains("Window positions must not be null"));
    }
    private static int idx(char c) {
        return (int)c - 'A';
    }

    /*
    Empty plugboard: no pairs means every letter maps to itself.
    This is the plugboard you'll use for the first full machine test (AAAAA → BDZGO).
     */
}
