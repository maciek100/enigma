package com.cowboy.enigma;

import java.util.stream.IntStream;

public class Plugboard {
    private int [] plugs;
    public Plugboard(String pairs) {
        plugs = new int[26];
        IntStream.range(0, 26).forEach(i -> plugs[i] = i);
        String [] temp = pairs.split("\\s+");
        for (String pair : temp) {
            if (pair.length() == 2) {
                char c1 = pair.charAt(0);
                char c2 = pair.charAt(1);
                if (c1 == c2)
                    continue;
                plugs[c1 - 'A'] = c2 - 'A';
                plugs[c2 - 'A'] = c1 - 'A';
            }
        }
    }

    public int swap(int letter) {
        return plugs[letter];
    }
}
