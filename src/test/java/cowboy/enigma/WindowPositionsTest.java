package cowboy.enigma;

import com.cowboy.enigma.WindowPositions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WindowPositionsTest {

    @Test
    public void testWindowPositions01() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
        () -> new WindowPositions(1,1,-1));
        assertTrue(iae.getMessage().contains("Window Position values must be must be between [0, 25]"));

    }

    @Test
    public void testWindowPositions02() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(1,26,25));
        assertTrue(iae.getMessage().contains("Window Position values must be must be between [0, 25]"));

    }
}
