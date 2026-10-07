package com.cowboy.enigma;

public class EnigmaMachine {
    /**
     * This is a typical enigma machine, with three rotors, and standard settings.
     */
    private final Rotor [] rotors;
    private final Reflector reflector;
    private final Plugboard plugboard;
    private int rrWindowPosition;
    private int mrWindowPosition;
    private int lrWindowPosition;

    public EnigmaMachine(Rotor[] rotors, Reflector reflector, Plugboard plugboard) {
        this.rotors = rotors;
        this.reflector = reflector;
        this.plugboard = plugboard;
    }

    public void setWindowPositions(WindowPositions ringPositions) {
        rrWindowPosition = ringPositions.right();
        mrWindowPosition = ringPositions.middle();
        lrWindowPosition = ringPositions.left();
    }

    public WindowPositions getPositions() {
        return new WindowPositions(lrWindowPosition, mrWindowPosition, rrWindowPosition);
    }

    public void stepRotors() {
        boolean rightAtTurnover = rotors[2].isAtTurnover(rrWindowPosition);
        boolean middleAtTurnover = rotors[1].isAtTurnover(mrWindowPosition);

        if (middleAtTurnover) {
            lrWindowPosition = (lrWindowPosition + 1) % 26;
        }
        if (middleAtTurnover || rightAtTurnover) {
            mrWindowPosition = (mrWindowPosition + 1) % 26;
        }
        rrWindowPosition = (rrWindowPosition + 1) % 26;
    }

    public int encode(int value) {
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

    public int encrypt(int value) {
        this.stepRotors();
        return encode(value);
    }
}
