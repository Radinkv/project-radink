package model.exercise;

import java.time.Duration;
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
 * PURPOSE: Models an endurance exercise where the primary variable is time spent performing it
 *          Tracks exercise name, duration, equipment used, and targeted muscles
 * 
 * MUTABILITY: Immutable
 */
public class EnduranceExercise extends Exercise {

    // REQUIRES: totalDuration > 0
    // EFFECTS: Create an instance of this endurance exercise, initializing:
    //          1. Name of this exercise
    //          2. Total duration of this endurance exercise, in minutes
    //          3. Equipment used in performing this endurance exercise
    //          4. Muscle group (muscles) targeted by this endurance-based exercise
    public EnduranceExercise(String name, double totalDuration, 
                            Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        // stub
    }

    // EFFECTS: Return total duration of this endurance exercise, in minutes
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0); // stub
    }

    // EFFECTS: Return key-value pair with 'duration' representing this endurance exercise's total duration:
    //          1. 'totalDuration' (duration in seconds)
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<>(); // stub
    }
}
