package com.cowboy.enigma.api;

/**
 * Result of POST /api/enigma.
 *
 * @param output         the encrypted (or decrypted) text
 * @param prepared       the text actually typed on the machine, after MessagePreparer
 * @param finalPositions the window letters after the last key press, left to right
 */
public record EnigmaResponse(String output, String prepared, String finalPositions) {
}
