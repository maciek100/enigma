package com.cowboy.enigma;

import static java.lang.Math.floorMod;

public class Rotor {
    private static final int SIZE = 26;

    private final int [] wiring;
    private final int [] reversedWiring;
    private final int turnover;
    private final int ringSetting;

    public Rotor(RotorType rType, int ringSetting) {
        if (rType == null) {
            throw new IllegalArgumentException("Rotor type must not be null");
        }
        if (!inRange(ringSetting)) {
            throw new IllegalArgumentException("Ring setting must be between [0, 25]: " + ringSetting);
        }
        this.ringSetting = ringSetting;
        this.turnover = rType.getTurnover();
        wiring = new int[SIZE];
        String wiringString = rType.getWiring();
        for (int i = 0; i < wiringString.length(); i++) {
            wiring[i] = wiringString.charAt(i) - 'A'; // convert to 0-25
        }
        reversedWiring = new int[SIZE];
        for (int i = 0; i < wiring.length; i++) {
            reversedWiring[wiring[i]] = i;
        }
    }

    public int forward(int input, int position) {
        return pass(wiring, input, position);
    }

    public int backward(int input, int position) {
        return pass(reversedWiring, input, position);
    }

    /** Shifts into the rotor core's frame, applies the wiring, and shifts back. */
    private int pass(int [] map, int input, int position) {
        checkInput(input);
        checkPosition(position);
        int shift = floorMod(position - ringSetting, SIZE);
        int coreContact = floorMod(input + shift, SIZE);
        return floorMod(map[coreContact] - shift, SIZE);
    }

    public String expose() {
        StringBuilder buffer = new StringBuilder();
        for (int j : wiring) {
            buffer.append((char) (j + 'A'));
        }
        return buffer.toString();
    }

    public boolean isAtTurnover(int position) {
        checkPosition(position);
        return position == turnover;
    }

    private static boolean inRange(int value) {
        return value >= 0 && value < SIZE;
    }

    private static void checkInput(int input) {
        if (!inRange(input)) {
            throw new IllegalArgumentException("Rotor input must be between [0, 25]: " + input);
        }
    }

    private static void checkPosition(int position) {
        if (!inRange(position)) {
            throw new IllegalArgumentException("Rotor position must be between [0, 25]: " + position);
        }
    }
}
