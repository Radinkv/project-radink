package model.muscle;

import model.association.ExerciseAssociator;

/*
 * This class represents a specific muscle.
 * 
 * Shared instances of this class will join to create instances of MuscleGroup, which represent 
 * the muscle groups that a given exercise uses. 
 */
public class Muscle extends ExerciseAssociator {

    public Muscle(String muscleName) {
        // stub
    }

    public String getMuscleName() {
        return ""; // stub
    }
}
