package model.exercise;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import persistence.Writable;

/**
 * REPRESENTS: A centralized collection of all available exercises.
 * 
 * USED BY:
 *      1. Workouts that reference stored exercises
 *      2. MuscleGroups that track exercise impacts
 *      3. Equipment that organizes exercises by usage
 * 
 * PURPOSE: Manage the storage, retrieval, and removal of exercises from a central hub/library
 *          As exercises are immutable ExerciseLibrary tracks various exercises consistently 
 *          across the program.
 * 
 * MUTABILITY: Mutable 
 */
public class ExerciseLibrary implements Writable {

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
        return new HashMap<String, Exercise>(library); // Defensive copy
    }

    // EFFECTS: Return a JSON representation of this ExerciseLibrary containing
    //          all exercises and their complete state
    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    // MODIFIES: this
    // EFFECTS: Reconstruct this ExerciseLibrary's state from the provided JSON data
    //          Throw JSONException if data is invalid or incomplete
    @Override
    public void fromJson(JSONObject json) throws JSONException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
        // REMINDER: Needs PredefinedData
    }
}
