package com.cowboy.enigma;

public enum ReflectorType {
    A ("EJMZALYXVBWFCRQUONTSPIKHGD"),
    B ("YRUHQSLDPXNGOKMIEBFZCWVJAT"),
    C ("FVPJIAOYEDRZXWGCTKUQSBNMHL");

    private final String wiring;
    ReflectorType(String wiring) {
        this.wiring = wiring;
    }

    public String getWiring() {
        return wiring;
    }
}
