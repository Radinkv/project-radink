package model.muscle;

import model.association.ExerciseAssociator;

import java.util.HashSet;
import java.util.Set;

/**
 * REPRESENTS: A collection of muscles that are trained together
 * 
 * USED BY:
 *      1. Exercise classes that target multiple muscles
 *      2. Workout classes that cumulate Exercise objects
 * 
 * PURPOSE: Tracks the impact of exercises on a group of muscles
 *          Calls register/unregister on each individual muscle
 * 
 * MUTABILITY: Immutable
 */
public class MuscleGroup extends ExerciseAssociator {

    // EFFECTS: Returns the set of muscles contained within this group
    public Set<Muscle> getMuscles() {
        return new HashSet<Muscle>(); // stub
    }
}
