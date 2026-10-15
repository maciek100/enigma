package com.cowboy.enigma.api;

import com.cowboy.enigma.EnigmaMachine;
import com.cowboy.enigma.MessagePreparer;
import com.cowboy.enigma.Plugboard;
import com.cowboy.enigma.Reflector;
import com.cowboy.enigma.ReflectorType;
import com.cowboy.enigma.Rotor;
import com.cowboy.enigma.RotorType;
import com.cowboy.enigma.WindowPositions;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;

/**
 * The lenient edge in front of the strict core: accepts lowercase letters, turns
 * letters into 0-25 values, and leaves every machine rule to the core classes.
 * Each request gets a fresh machine, so requests never affect each other.
 */
@RestController
public class EnigmaController {

    @PostMapping("/api/enigma")
    public EnigmaResponse encrypt(@RequestBody EnigmaRequest request) {
        List<String> rotorNames = required(request.rotors(), "rotors");
        int[] rings = threeLetters(required(request.rings(), "rings"), "Rings");
        int[] positions = threeLetters(required(request.positions(), "positions"), "Positions");
        String reflectorName = required(request.reflector(), "reflector");
        String plugboardPairs = required(request.plugboard(), "plugboard");
        String text = required(request.text(), "text");

        EnigmaMachine machine = new EnigmaMachine(
                rotors(rotorNames, rings),
                new Reflector(reflectorType(reflectorName)),
                new Plugboard(plugboardPairs.toUpperCase(Locale.ROOT)));
        machine.setWindowPositions(new WindowPositions(positions[0], positions[1], positions[2]));

        String prepared = MessagePreparer.prepare(text);
        String output = machine.encryptText(prepared);
        return new EnigmaResponse(output, prepared, letters(machine.getPositions()));
    }

    private static <T> T required(T value, String field) {
        if (value == null) {
            throw new IllegalArgumentException("Field '" + field + "' is required");
        }
        return value;
    }

    /** "aBc" -> {0, 1, 2}; anything but exactly three letters A-Z (any case) is rejected. */
    private static int[] threeLetters(String value, String what) {
        String upper = value.toUpperCase(Locale.ROOT);
        if (!upper.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException(what + " must be 3 letters A-Z: " + value);
        }
        return new int[] {upper.charAt(0) - 'A', upper.charAt(1) - 'A', upper.charAt(2) - 'A'};
    }

    /**
     * Builds one Rotor per name, with its ring setting. A null name becomes a null Rotor,
     * and a list of the wrong size becomes an array of that size, so the core reports
     * both with its own "Exactly 3 non-null Rotors required".
     */
    private static Rotor[] rotors(List<String> names, int[] rings) {
        Rotor[] rotors = new Rotor[names.size()];
        if (rotors.length != rings.length) {
            return rotors;
        }
        for (int i = 0; i < rotors.length; i++) {
            String name = names.get(i);
            rotors[i] = name == null ? null : new Rotor(rotorType(name), rings[i]);
        }
        return rotors;
    }

    private static RotorType rotorType(String name) {
        try {
            return RotorType.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Rotor: " + name);
        }
    }

    private static ReflectorType reflectorType(String name) {
        try {
            return ReflectorType.valueOf(name.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown Reflector: " + name);
        }
    }

    private static String letters(WindowPositions p) {
        return "" + (char) ('A' + p.left()) + (char) ('A' + p.middle()) + (char) ('A' + p.right());
    }
}
