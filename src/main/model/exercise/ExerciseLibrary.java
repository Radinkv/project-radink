package model.exercise;

import java.util.Map;

/**
 * REPRESENTS: a centralized collection of all available exercises.
 * 
 * USED BY:
 *      1. Workouts that reference stored exercises
 *      2. MuscleGroups that track exercise impacts
 *      3. Equipment that organizes exercises by usage
 * 
 * PURPOSE: Manages the storage, retrieval, and removal of exercises from a central hub/library. 
 *          As exercises are immutable ExerciseLibrary tracks various exercises consistently 
 *          across the program.
 * 
 * MUTABILITY: Mutable 
 */
public class ExerciseLibrary {

    // EFFECTS: Returns true if an exercise with the given name exists in the library, false otherwise.
    public boolean containsExercise(String exerciseName) {
        return false; // stub
    }

    // MODIFIES: this, MuscleGroup, Equipment
    // EFFECTS: Adds the given exercise to the library if not already present.
    //          Registers the exercise's impact with its associated MuscleGroup.
    //          Registers the exercise's usage with its associated Equipment.
    //          Returns true if the exercise was added, false otherwise.
    public boolean addExercise(Exercise exercise) {
        return false; // stub
    }

    // EFFECTS: Return the exercise with the given name, or null if not found.
    public Exercise getExercise(String exerciseName) {
        return null; // stub
    }

    // MODIFIES: this, MuscleGroup, Equipment
    // EFFECTS: Removes the exercise with the given name from the library if present.
    //          Unregisters the exercise from its associated MuscleGroup.
    //          Unregisters the exercise from its associated Equipment.
    //          Returns true if the exercise was removed, false otherwise.
    public boolean removeExercise(String exerciseName) {
        return false; // stub
    }

    // EFFECTS: Return all stored exercises and their names.
    public Map<String, Exercise> getAllExercises() {
        return null; // stub
    }
}
