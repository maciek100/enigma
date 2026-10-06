package com.cowboy.enigma;

public enum RotorType {
    I  ("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q'),
    II ("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E'),
    III("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V'),
    IV ("ESOVPZJAYQUIRHXLNFTGKDCMWB", 'J'),
    V  ("VZBRGITYUPSDNHLXAWMJQOFECK", 'Z');

    private final String wiring;
    private final int turnover;
    RotorType(String wiring, char turnover) {
        this.wiring = wiring;
        this.turnover = turnover - 'A';
    }

    public String getWiring() { return wiring; }
    public int getTurnover() { return turnover; }
}
