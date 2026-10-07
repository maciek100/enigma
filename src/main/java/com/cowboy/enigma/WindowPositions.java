package com.cowboy.enigma;

public record WindowPositions(int left, int middle, int right) {
    public String toString() {
        return String.format("(%c, %c, %c)", left + 'A', middle + 'A', right + 'A');
    }
}
