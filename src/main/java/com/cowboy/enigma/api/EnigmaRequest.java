package com.cowboy.enigma.api;

import java.util.List;

/**
 * Body of POST /api/enigma. Every field is required; letters may be in either case.
 *
 * @param rotors    three rotor names, left to right, e.g. ["I", "II", "III"]
 * @param rings     three ring-setting letters, left to right, e.g. "AAA"
 * @param positions three window letters, left to right, e.g. "AAA"
 * @param reflector reflector name, e.g. "B"
 * @param plugboard letter pairs, e.g. "AT BL"; "" means no cables
 * @param text      free text; prepared by MessagePreparer before encryption
 */
public record EnigmaRequest(
        List<String> rotors,
        String rings,
        String positions,
        String reflector,
        String plugboard,
        String text) {
}
