package com.cowboy.enigma;

/**
 * Turns free text into machine-ready text (A-Z only), for {@link EnigmaMachine#encryptText}.
 *
 * Rules, applied character by character:
 * - letters a-z / A-Z are kept and uppercased;
 * - digits are spelled out in English, digit by digit ("18" -> "ONEEIGHT");
 * - whitespace (space, tab, newline) marks a word break;
 * - everything else (punctuation, accented letters, symbols) is dropped.
 * Then leading and trailing word breaks are removed, and each run of breaks
 * becomes a single {@link #SPACE_MARK}.
 */
public final class MessagePreparer {

    /** Stands in for a word break, as in Wehrmacht practice. */
    public static final char SPACE_MARK = 'X';

    private static final String[] DIGIT_WORDS = {
            "ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE"
    };

    private MessagePreparer() {
        // static utility class: no instances
    }

    public static String prepare(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }
        StringBuilder result = new StringBuilder(text.length());
        boolean pendingBreak = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            String piece;
            if (c >= 'A' && c <= 'Z') {
                piece = String.valueOf(c);
            } else if (c >= 'a' && c <= 'z') {
                piece = String.valueOf((char) (c - 'a' + 'A'));
            } else if (c >= '0' && c <= '9') {
                piece = DIGIT_WORDS[c - '0'];
            } else {
                if (Character.isWhitespace(c)) {
                    pendingBreak = true;
                }
                continue; // whitespace is remembered, everything else is dropped
            }
            // emit one SPACE_MARK for any run of breaks, but never at the very start
            if (pendingBreak && !result.isEmpty()) {
                result.append(SPACE_MARK);
            }
            pendingBreak = false;
            result.append(piece);
        }
        return result.toString();
    }
}
