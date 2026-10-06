package com.cowboy.enigma;

public class EnigmaMachine {
    private Rotor [] rotors;
    private Reflector reflector;
    private Plugboard plugboard;
    private int rightRotorPosition;
    private int middleRotorPosition;
    private int leftRotorPosition;

    public EnigmaMachine(Rotor[] rotors, Reflector reflector, Plugboard plugboard) {
        this.rotors = rotors;
        this.reflector = reflector;
        this.plugboard = plugboard;
    }

    public void setPositions(RingPositions ringPositions) {
        rightRotorPosition = ringPositions.right();
        middleRotorPosition = ringPositions.middle();
        leftRotorPosition = ringPositions.left();
    }

    public RingPositions getPositions() {
        return new RingPositions (leftRotorPosition, middleRotorPosition, rightRotorPosition);
    }

    public void stepRotors() {
        boolean rightAtTurnover = rotors[2].isAtTurnover(rightRotorPosition);
        boolean middleAtTurnover = rotors[1].isAtTurnover(middleRotorPosition);

        if (middleAtTurnover) {
            leftRotorPosition = (leftRotorPosition + 1) % 26;
        }
        if (middleAtTurnover || rightAtTurnover) {
            middleRotorPosition = (middleRotorPosition + 1) % 26;
        }
        rightRotorPosition = (rightRotorPosition + 1) % 26;
    }
        //rightRotorPosition = (rightRotorPosition + 1) % 26;
        //if (rotors[2].isAtTurnover(rightRotorPosition)) {
        //    middleRotorPosition = (middleRotorPosition + 1) % 26;
        //}
        //if (rotors[1].isAtTurnover(middleRotorPosition)) {
        //    leftRotorPosition = (leftRotorPosition + 1) % 26;
        //}
    //}

    //public int encrypt(char key) {
    //    int index = key - 'A';
    //    stepRotors();
//
  //  }
}
