package com.cowboy.enigma.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WindowPositionsTest {

    @Test
    public void testWindowPositionsRightTooLow() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
        () -> new WindowPositions(1,1,-1));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsRightTooHigh() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(1,20,26));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsMiddleTooLow() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(1,-1,1));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsMiddleTooHigh() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(1,26,2));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsLeftTooLow() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(-1,1,1));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsLeftTooHigh() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> new WindowPositions(26,20,20));
        assertTrue(iae.getMessage().contains("Window Position values must be between [0, 25]"));

    }

    @Test
    public void testWindowPositionsJustRight() {
        assertDoesNotThrow(() -> new WindowPositions(0,25,0));
    }
}
