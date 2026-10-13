package com.cowboy.enigma;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
        System.out.println("[" + input + "] -> [" + expected + "]");
        assertEquals(expected, MessagePreparer.prepare(input));
    }
    /*
    public void testMessagePreparerSimple() {
        assertEquals("HELLO", MessagePreparer.prepare("Hello"));
        assertEquals("HELLOXWORLD", MessagePreparer.prepare("Hello  world"));
        assertEquals("HELLO", MessagePreparer.prepare("  HELLO   "));
        assertEquals("HELLOWORLD", MessagePreparer.prepare("HELLO,WORLD"));
        assertEquals("HELLOXWORLD", MessagePreparer.prepare("HELLO , WORLD"));
        assertEquals("HELLOXWORLD", MessagePreparer.prepare("HELLO\tWORLD"));
        assertEquals("HELLOXWORLD", MessagePreparer.prepare("HELLO\nWORLD"));
        assertEquals("HELLOXWORLD", MessagePreparer.prepare("HELLO \t WORLD"));
        assertEquals("THREEFOURFIVE", MessagePreparer.prepare("345"));
        assertEquals("AONE", MessagePreparer.prepare("A1"));
        assertEquals("ONETWOTHREE", MessagePreparer.prepare("1:23"));
        assertEquals("XRAY", MessagePreparer.prepare("XRAY"));
        assertEquals("MILLER", MessagePreparer.prepare("Müller"));
        assertEquals("", MessagePreparer.prepare("!!!!"));

        assertEquals("ZEROONETWOTHREEFOURFIVESIXSEVENEIGHTNINE",
                MessagePreparer.prepare("0123456789"));
    }
    */
    @Test
    public void testMessagePreparerNullInput() {
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MessagePreparer.prepare(null));
        assertTrue(iae.getMessage().contains("Text must not be null"));
    }

}
