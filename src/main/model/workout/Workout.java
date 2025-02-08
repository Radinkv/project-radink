package model.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exercise.Exercise;

/**
 * REPRESENTS: a structured sequence of exercises performed in a specific order
 * 
 * USED BY:
 *      1. WeeklySchedule to assign workouts to specific days
 *      2. WorkoutLibrary to store predefined workouts
 * 
 * PURPOSE: Defines a workout routine with a fixed order of exercises
 *          Ensures workout immutability once created
 * 
 * MUTABILITY: Immutable
 */
public class Workout extends WorkoutPlan {

    // EFFECTS: Create a rest day with the given recovery note
    public Workout(String workoutName, List<Exercise> exercises) {

    }

    // EFFECTS: Return the total duration of this workout in seconds
    @Override
    public double getDuration() {
        return 0.0; // stub
    }

    // EFFECTS: Return list of exercises this workout includes
    @Override
    public List<Exercise> getExercises() {
        return new ArrayList<Exercise>(); // stub
    }

    // EFFECTS: Return key-value pairs summarizing this workout
    @Override
    public Map<String, Double> getWorkoutSummary() {
        return new HashMap<String, Double>(); // stub
    }
}
