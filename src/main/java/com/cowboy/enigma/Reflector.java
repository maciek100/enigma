package com.cowboy.enigma;

import java.util.Arrays;

public class Reflector {
    int size = 26;
    int [] array = new int[size];

    public Reflector(String reflectorString) {
        for (int i = 0; i < reflectorString.length(); i++) {
            array[i] = reflectorString.charAt(i) - 'A';
        }
    }

    public int reflect (int position) {
        return array[position];
    }

    public String expose () {
        return Arrays.toString(array);
    }
}
