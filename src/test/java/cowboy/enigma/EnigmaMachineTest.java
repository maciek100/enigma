package cowboy.enigma;

import com.cowboy.enigma.*;
//import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnigmaMachineTest {

    private EnigmaMachine enigmaMachine;

    @BeforeEach
    public void setUp() {
        Rotor[] rotorOrder = new Rotor[] {
                new Rotor(RotorType.I, 0),
                new Rotor(RotorType.II, 0),
                new Rotor(RotorType.III, 0)
        };

        enigmaMachine = new EnigmaMachine(
                rotorOrder,
                new Reflector(ReflectorType.B),
                Plugboard.empty());
    }

    @Test
    public void testEnigmaMachine01() {
        RingPositions expectedBefore = new RingPositions(0, 3, 20);
        RingPositions expectedAfterStep1 = new RingPositions(0, 3, 21);
        RingPositions expectedAfterStep2 = new RingPositions(0, 4, 22);
        RingPositions expectedAfterStep3 = new RingPositions(1, 5, 23);
        RingPositions expectedAfterStep4 = new RingPositions(1, 5, 24);

        enigmaMachine.setPositions(new RingPositions(0, 3, 20));
        assertEquals(expectedBefore, enigmaMachine.getPositions(), "Expected before");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep1, enigmaMachine.getPositions(), "Expected after step 1");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep2, enigmaMachine.getPositions(), "Expected after step 2");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep3, enigmaMachine.getPositions(),"Expected after step 3");

        enigmaMachine.stepRotors();
        assertEquals(expectedAfterStep4, enigmaMachine.getPositions(),"Expected after step 4");
        //System.out.println(enigmaMachine.getPositions());
    }

    @Test
    public void testEnigmaMachine02() {


    }
}
