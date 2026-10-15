package com.cowboy.enigma.core;

/** The three letters visible in the rotor windows, left to right, each 0-25 (A-Z). */
public record WindowPositions(int left, int middle, int right) {
    public WindowPositions {
        check("left", left);
        check("middle", middle);
        check("right", right);
    }

    private static void check(String name, int value) {
        if (value < 0 || value > 25) {
            throw new IllegalArgumentException(
                    "Window Position values must be between [0, 25]: " + name + " = " + value);
        }
    }

    @Override
    public String toString() {
        return String.format("(%c, %c, %c)", left + 'A', middle + 'A', right + 'A');
    }
}
