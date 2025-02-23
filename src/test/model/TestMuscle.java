package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.muscle.Muscle;

/** This class tests the IMMUTABLE attributes of the Muscle class. */
public class TestMuscle {
    Muscle muscle1;
    Muscle muscle2;
    Muscle muscle3;
    
    @BeforeEach
    void runBefore() {
        muscle1 = new Muscle("Biceps");
        muscle2 = new Muscle("Quads");
        muscle3 = new Muscle("Heart");
    }

    @Test
    void testGetName() {
        assertEquals(muscle1.getName(), "Biceps");
        assertEquals(muscle2.getName(), "Quads");
        assertEquals(muscle3.getName(), "Heart");
    }
}
