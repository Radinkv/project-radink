package model.muscle;

import model.association.ExerciseAssociator;

/**
 * REPRESENTS: a single muscle that is utilized within exercises
 * 
 * USED BY:
 *      1. MuscleGroup, which tracks multiple muscles
 *      2. Exercise objects that target specific muscles among their MuscleGroup
 * 
 * PURPOSE: Stores and tracks exercise impact on an individual muscle
 *          Used to calculate statistics on muscle workload
 * 
 * MUTABILITY: Immutable 
 */
public class Muscle extends ExerciseAssociator {

    // EFFECTS: Create an instance of this Muscle
    public Muscle(String muscleName) {
        // stub
    }

    // EFFECTS: Get this muscle's name
    public String getName() {
        return ""; // stub
    }
}
