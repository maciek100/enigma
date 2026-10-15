package com.cowboy.enigma.core;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessagePreparerTest {

    @ParameterizedTest
    @CsvSource(delimiter = '|', value = {
            "Hello | HELLO",
            "Hello  world   | HELLOXWORLD",
            "'  HELLO    '  | HELLO",
            "HELLO,WORLD    | HELLOWORLD",
            "HELLO , WORLD  | HELLOXWORLD",
            "HELLO\tWORLD   | HELLOXWORLD",
            "'HELLO\nWORLD'   | HELLOXWORLD",
            "HELLO \t WORLD | HELLOXWORLD",
            "345            | THREEFOURFIVE",
            "A1             | AONE",
            "1:23           | ONETWOTHREE",
            "XRAY           | XRAY",
            "Müller         | MLLER",
            "!!!!!          | ''",
            "0123456789     | ZEROONETWOTHREEFOURFIVESIXSEVENEIGHTNINE",
            "''|''",
            "ROCKY   BOTTOM | ROCKYXBOTTOM",
            "#!@!~ && $ |''",
            "Hello ,  World | HELLOXWORLD"
    })
    void prepares(String input, String expected) {
        assertEquals(expected, MessagePreparer.prepare(input));
    }

    @Test
    public void testMessagePreparerNullInput() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MessagePreparer.prepare(null));
        assertTrue(iae.getMessage().contains("Text must not be null"));
    }

    @Test
    public void testMessageOutputsOnlyValidOutput() {
        for (String s : List.of("Hello, World!", "  18:30\t", "Müller & Söhne", "!!!", "")) {
            String prepared = MessagePreparer.prepare(s);
            assertTrue(prepared.matches("[A-Z]*"), "not machine-ready: " + prepared);
        }
    }

}
