package com.cowboy.enigma;

import java.util.stream.IntStream;

/**
 * The plugboard (Steckerbrett).
 *
 * Accepts exactly one format: uppercase letter pairs separated by single spaces,
 * e.g. "AD FM". The empty string means no cables are plugged.
 * Anything else is rejected with an IllegalArgumentException.
 */
public class Plugboard {
    private final int [] plugs;

    public Plugboard(String pairs) {
        if (pairs == null) {
            throw new IllegalArgumentException("Plugboard pairs must not be null");
        }
        validateFormat(pairs);

        plugs = new int[26];
        IntStream.range(0, 26).forEach(i -> plugs[i] = i);
        for (int i = 0; i < pairs.length(); i += 3) {
            char c1 = pairs.charAt(i);
            char c2 = pairs.charAt(i + 1);
            if (c1 == c2) {
                throw new IllegalArgumentException("Letter plugged to itself detected: '" + c1 + "'");
            }
            for (char c : new char[] {c1, c2}) {
                if (plugs[c - 'A'] != c - 'A') {
                    throw new IllegalArgumentException("Duplicate character detected: '" + c + "'");
                }
            }
            plugs[c1 - 'A'] = c2 - 'A';
            plugs[c2 - 'A'] = c1 - 'A';
        }
    }

    /**
     * Checks the layout "XY XY XY": in every group of three characters, the first
     * two must be A-Z and the third a single space. The first character that breaks
     * the pattern is reported.
     */
    private static void validateFormat(String pairs) {
        for (int i = 0; i < pairs.length(); i++) {
            char c = pairs.charAt(i);
            boolean separatorSlot = i % 3 == 2;
            boolean valid = separatorSlot ? c == ' ' : c >= 'A' && c <= 'Z';
            if (!valid) {
                throw new IllegalArgumentException("Incorrect character detected: '" + c + "'");
            }
        }
        int remainder = pairs.length() % 3;
        if (remainder == 0 && !pairs.isEmpty()) {
            // ends with a separator: "AB CD "
            throw new IllegalArgumentException("Incorrect character detected: ' '");
        }
        if (remainder == 1) {
            // one letter left over: "AB C"
            throw new IllegalArgumentException(
                    "Incomplete pair detected: '" + pairs.charAt(pairs.length() - 1) + "'");
        }
    }

    public static Plugboard empty() {
        return new Plugboard("");
    }

    public int swap(int letter) {
        return plugs[letter];
    }
}
