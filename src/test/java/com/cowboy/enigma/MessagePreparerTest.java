package com.cowboy.enigma;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MessagePreparerTest {

    @Test
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
        assertEquals("MILLER", MessagePreparer.prepare("M¨ller"));
        assertEquals("", MessagePreparer.prepare("!!!!"));
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                () -> MessagePreparer.prepare(null));
        assertTrue(iae.getMessage().contains("Text must not be null"));
        assertEquals("ZEROONETWOTHREEFOURFIVESIXSEVENEIGHTNINE",
                MessagePreparer.prepare("0123456789"));
    }
}
