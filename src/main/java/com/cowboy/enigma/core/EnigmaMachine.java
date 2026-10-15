package com.cowboy.enigma.core;

import java.util.EnumSet;
import java.util.Set;

/**
 * A typical three-rotor Enigma I. Rotors are given left to right; the
 * right-hand rotor is the fast one.
 */
public class EnigmaMachine {
    private static final int SIZE = 26;

    private final Rotor [] rotors;
    private final Reflector reflector;
    private final Plugboard plugboard;
    private int rrWindowPosition;
    private int mrWindowPosition;
    private int lrWindowPosition;

    public EnigmaMachine(Rotor[] rotors, Reflector reflector, Plugboard plugboard) {
        if (rotors == null) {
            throw new IllegalArgumentException("Rotors must not be null");
        }
        if (rotors.length != 3 || rotors[0] == null || rotors[1] == null || rotors[2] == null) {
            throw new IllegalArgumentException("Exactly 3 non-null Rotors required");
        }
        Set<RotorType> seen = EnumSet.noneOf(RotorType.class);
        for (Rotor rotor : rotors) {
            if (!seen.add(rotor.getType())) {
                throw new IllegalArgumentException("Duplicate Rotor type: " + rotor.getType());
            }
        }
        if (reflector == null) {
            throw new IllegalArgumentException("Reflector must not be null");
        }
        if (plugboard == null) {
            throw new IllegalArgumentException("Plugboard must not be null");
        }
        this.rotors = rotors.clone(); // the caller's array may change later; ours must not
        this.reflector = reflector;
        this.plugboard = plugboard;
    }

    public void setWindowPositions(WindowPositions windowPositions) {
        if (windowPositions == null) {
            throw new IllegalArgumentException("Window positions must not be null");
        }
        rrWindowPosition = windowPositions.right();
        mrWindowPosition = windowPositions.middle();
        lrWindowPosition = windowPositions.left();
    }

    public WindowPositions getPositions() {
        return new WindowPositions(lrWindowPosition, mrWindowPosition, rrWindowPosition);
    }

    public void stepRotors() {
        boolean rightAtTurnover = rotors[2].isAtTurnover(rrWindowPosition);
        boolean middleAtTurnover = rotors[1].isAtTurnover(mrWindowPosition);

        if (middleAtTurnover) {
            lrWindowPosition = (lrWindowPosition + 1) % SIZE;
        }
        if (middleAtTurnover || rightAtTurnover) {
            mrWindowPosition = (mrWindowPosition + 1) % SIZE;
        }
        rrWindowPosition = (rrWindowPosition + 1) % SIZE;
    }

    /** Passes one letter through the machine without stepping the rotors. */
    public int encode(int value) {
        if (value < 0 || value >= SIZE) {
            throw new IllegalArgumentException("Machine input must be between [0, 25]: " + value);
        }
        int plugF = plugboard.swap(value);
        int r1F = rotors[2].forward(plugF, rrWindowPosition);
        int r2F = rotors[1].forward(r1F, mrWindowPosition);
        int r3F = rotors[0].forward(r2F, lrWindowPosition);
        int rB = reflector.reflect(r3F);
        int r3B = rotors[0].backward(rB, lrWindowPosition);
        int r2B = rotors[1].backward(r3B, mrWindowPosition);
        int r1B = rotors[2].backward(r2B, rrWindowPosition);
        return plugboard.swap(r1B);
    }

    /** A key press: steps the rotors, then encodes the letter. */
    public int encrypt(int value) {
        this.stepRotors();
        return encode(value);
    }

    /**
     * Encrypts (or, Enigma being reciprocal, decrypts) a text of uppercase letters A-Z,
     * one key press per letter. The whole text is checked before any rotor moves, so a
     * rejected call leaves the machine exactly as it was.
     */
    public String encryptText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c < 'A' || c > 'Z') {
                throw new IllegalArgumentException("Text must contain UPPERCASE CHARACTERS ONLY: '" + c + "'");
            }
        }
        StringBuilder result = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            result.append((char) (encrypt(text.charAt(i) - 'A') + 'A'));
        }
        return result.toString();
    }
}
