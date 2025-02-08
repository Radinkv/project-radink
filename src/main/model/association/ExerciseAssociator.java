package model.association;

import java.util.HashMap;
import java.util.Map;

/**
 * REPRESENTS: an entity that tracks associated exercises.
 * 
 * USED BY:
 *      1. Equipment (i.e. Dumbbell, Treadmill, Barbell)
 *      2. Muscle (Muscle class instantiations)
 * 
 * PURPOSE: This abstraction allows tracking exercise volume and duration across different types of workouts.
 */
public abstract class ExerciseAssociator {

    // MODIFIES: this
    // EFFECTS: Add the given exercise's data if it is not already stored and return true.
    //          Otherwise, return false and makes no changes.
    public boolean registerExercise(String exerciseName, Map<String, Double> exerciseInfo) {
        return false; // stub
    }

    // MODIFIES: this
    // EFFECTS: Remove the given exercise by name if it is present and return true.
    //          Otherwise, return false and make no changes.
    public boolean unregisterExercise(String exerciseName) {
        return true; // stub
    }

    // EFFECTS: Returns aggregated metrics across all associated exercises.
    //          1. "totalSets": total sets across strength exercises
    //          2. "totalReps": total reps across strength exercises
    //          3. "totalIntervalDuration": total time in interval-based exercises
    //          4. "totalEnduranceDuration": total time in duration-based exercises
    //          4. "totalDuration": total duration across all exercises
    //          Key values are 0 if this entity does not have any exercise(s) that contribute to that metric.
    public Map<String, Double> getAggregatedExerciseMetrics() {
        return new HashMap<String, Double>();
    }
    
    // EFFECTS: Returns the number of associated exercises.
    public int getNumAssociatedExercises() {
        return 0; // stub
    }
}
