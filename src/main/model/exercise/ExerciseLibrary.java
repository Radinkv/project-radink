package model.exercise;

import java.util.HashMap;
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

    Map<String, Exercise> library;
    
    public ExerciseLibrary() {
        library = new HashMap<String, Exercise>();
    }

    // MODIFIES: this
    // EFFECTS: Adds the given exercise to the library if not already present.
    //          Returns true if the exercise was added, false otherwise.
    public boolean addExercise(Exercise exercise) {
        if (exercise == null || library.containsKey(exercise.getName())) {
            return false;
        } else {
            library.put(exercise.getName(), exercise);
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the exercise with the given name from the library if present.
    //          Returns true if the exercise was removed, false otherwise.
    public boolean removeExercise(String exerciseName) {
        if (containsExercise(exerciseName)) {
            library.remove(exerciseName);
            return true;
        }
        return false;
    }

    // EFFECTS: Return the exercise with the given name, or null if not found.
    public Exercise getExercise(String exerciseName) {
        return library.get(exerciseName);
    }

    // EFFECTS: Returns true if an exercise with the given name exists in the library, false otherwise.
    public boolean containsExercise(String exerciseName) {
        return library.containsKey(exerciseName);
    }


    // EFFECTS: Return all stored exercises and their names.
    public Map<String, Exercise> getAllExercises() {
        return library;
    }
}
