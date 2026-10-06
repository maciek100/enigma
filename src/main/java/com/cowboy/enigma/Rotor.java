package com.cowboy.enigma;

import java.util.Arrays;

import static java.lang.Math.floorMod;

public class Rotor {
    int size = 26; //26
    int [] wiring;
    int [] reversedWiring;
    int turnover;
    final int ringSetting;

    public Rotor(RotorType rType, int ringSetting) {
        this.ringSetting = ringSetting;
        this.turnover = rType.getTurnover();
        wiring = new int[size];
        String wiringString = rType.getWiring();
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
        int shift = floorMod((position - ringSetting), 26);
        int coreContact = (input + shift) % size;
        int temp = wiring[coreContact];
        return floorMod(temp - shift, 26);// % size;
    }

    public int backward(int input, int position) {
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

    public boolean isAtTurnover(int position) {
        return position == turnover;
    }
}
