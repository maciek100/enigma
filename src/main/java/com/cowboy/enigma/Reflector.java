package com.cowboy.enigma;

/**
 * A fixed, factory-wired reflector (Umkehrwalze). Wirings come only from
 * {@link ReflectorType}; their validity is checked by ReflectorTypeTest.
 */
public class Reflector {
    private static final int SIZE = 26;

    private final int [] wiring = new int[SIZE];

    public Reflector(ReflectorType reflectorType) {
        if (reflectorType == null) {
            throw new IllegalArgumentException("Reflector type must not be null");
        }
        String wiringString = reflectorType.getWiring();
        for (int i = 0; i < SIZE; i++) {
            wiring[i] = wiringString.charAt(i) - 'A';
        }
    }

    public int reflect(int input) {
        if (input < 0 || input >= SIZE) {
            throw new IllegalArgumentException("Reflector input must be between [0, 25]: " + input);
        }
        return wiring[input];
    }
}
