package model.exercise;

import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.muscle.MuscleGroup;

/**
 * REPRESENTS: an endurance-based exercise performed for a set duration
 * 
 * USED BY:
 *      1. Workouts that include endurance exercises
 *      2. MuscleGroups that track endurance-based exercise impacts
 *      3. Equipment that categorizes exercises based on usage
 * 
 * PURPOSE: Models an endurance exercise where the primary variable is the total duration
 *          spent performing it. Tracks exercise name, duration, equipment used, and targeted muscles.
 * 
 * MUTABILITY: Immutable
 */
public class EnduranceExercise extends Exercise {

    // EFFECTS: Create an instance of this endurance exercise, initializing:
    //          1. Name of this exercise
    //          2. Total duration of this endurance exercise, in minutes
    //          3. Equipment used in performing this endurance exercise
    //          4. Muscle group (muscles) targeted by this endurance-based exercise
    //          Limit totalDuration to avoid overflow and keep it above 0 if totalDuration < 0
    public EnduranceExercise(String name, double totalDuration, 
            Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        super(name, "Endurance", equipmentUsed, musclesTargeted);

        // Ensure totalDuration is within safe limits
        // Use a safe maximum that will not overflow when multiplied by 60 (for seconds conversion)
        double maxSafeDuration = Double.MAX_VALUE / 120; // Division by 120 provides double the extra safety margin
        double safeDuration = Math.max(Double.MIN_NORMAL, 
                Math.min(totalDuration, maxSafeDuration));

        exerciseInfo.put("duration", safeDuration);
    }

    // FOR EXERCISE ABSTRACTION TESTING PURPOSES
    public EnduranceExercise(int testNum) {
        super(null, (testNum == 1) ? null : "", null, null);
    }

    // EFFECTS: Return the endurance exercise's duration in seconds
    @Override
    public double getDuration() {
        return exerciseInfo.get("duration") * 60;
    }

    // EFFECTS: Return key-value pairs of the raw endurance exercise information
    //          (In this case, just the "duration" value as provided.)
    @Override
    public Map<String, Double> getInfo() {
        Map<String, Double> info = new HashMap<String, Double>(exerciseInfo);
        double durationSeconds = info.get("duration") * 60;
        info.put("totalDuration", durationSeconds);
        return info;
    }

    // EFFECTS: Converts the raw endurance exercise info into the aggregated metrics expected by ExerciseAssociator
    //          Includes:
    //              1. "totalEnduranceDuration"
    //              2. "totalDuration"
    public Map<String, Double> convertInfoToAssociatorFormat() {
        Map<String, Double> associatorMetrics = new HashMap<String, Double>();
        associatorMetrics.put("totalEnduranceDuration", getDuration());
        associatorMetrics.put("totalDuration", getDuration());
        return associatorMetrics;
    }
}
