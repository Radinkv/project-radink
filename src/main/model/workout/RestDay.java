package model.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exercise.Exercise;

/**
 * REPRESENTS: a designated rest day in a weekly workout plan
 * 
 * USED BY:
 *      1. WeeklySchedule to track intentional non-workout days
 *      2. UI components to display rest days clearly
 * 
 * PURPOSE: Provides an explicit representation of rest days
 *          Prevents null-based ambiguity in schedules
 * 
 * MUTABILITY: Immutable
 */
public class RestDay extends WorkoutPlan {

    // EFFECTS: Create a rest day with the given recovery note
    public RestDay(String recoveryNote) {

    }

    // EFFECTS: Return 0 as the total duration of this rest day
    @Override
    public double getDuration() {
        return 0.0; // stub
    }

    // EFFECTS: Return empty list of exercises for this rest day
    public List<Exercise> getExercises() {
        return new ArrayList<Exercise>(); // stub
    }

    // EFFECTS: Return empty map
    @Override
    public Map<String, Double> getWorkoutSummary() {
        return new HashMap<String, Double>(); // stub
    }
}
