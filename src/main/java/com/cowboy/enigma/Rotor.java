package com.cowboy.enigma;

import java.util.Arrays;

import static java.lang.Math.floorMod;

public class Rotor {
    int size = 26; //26
    int [] wiring;
    int [] reversedWiring;
    int notch;
    final int ringSetting;

    public Rotor(int ringSetting, String wiringString) {
        this.ringSetting = ringSetting;
        wiring = new int[size];
        for (int i = 0; i < wiringString.length(); i++) {
            int c = wiringString.charAt(i) - 'A'; // <- here convert to 0-25
            wiring[i] = c;
        }
        reversedWiring = new int[size];
        for (int i = 0; i < wiring.length; i++) {
            int index = wiring[i];
            reversedWiring[index] = i;
        }
    }

    public int forward(int input, int position) {
        //input = input - 'A';//Math.floorMod(input, size);
        //position = position - 'A';//Math.floorMod(position, size);
        int shift = floorMod((position - ringSetting), 26);
        int coreContact = (input + shift) % size;
        int temp = wiring[coreContact];
        return floorMod(temp - shift, 26);// % size;
    }

    public int backward(int input, int position) {
        //input = input - 'A';
        //position = position - 'A';

        int shift = floorMod(position - ringSetting, 26);
        int coreContact = floorMod(input + shift, 26);
        int temp = reversedWiring[coreContact];
        return floorMod(temp - shift, 26);
    }

    public String expose() {
        StringBuilder buffer = new StringBuilder();
        for (int j : reversedWiring) {
            buffer.append((char) (j + 'A'));
        }
        return buffer.toString();
    }
}
