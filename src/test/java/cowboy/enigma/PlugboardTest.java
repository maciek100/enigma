package cowboy.enigma;

import com.cowboy.enigma.Plugboard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    private static int idx(char c) {
        return (int)c - 'A';
    }

    /*
    Empty plugboard: no pairs means every letter maps to itself.
    This is the plugboard you'll use for the first full machine test (AAAAA → BDZGO).
     */
}
