package model.workout;

import java.util.List;
import java.util.Map;

import model.exercise.Exercise;


/**
 * REPRESENTS: A workout plan abstraction (either a workout or a rest day)
 * 
 * USED BY:
 *      1. WeeklySchedule to track intentional workout or non-workout days
 *      2. UI components to display the rest day or workout
 * 
 * PURPOSE: Provides an explicit abstraction of a workout plan 
 *          that both Workout and RestDay instances follow
 *          Easier for UI to distinguish
 * 
 * MUTABILITY: Immutable
 */
public abstract class WorkoutPlan {

    // EFFECTS: Return name of this workout plan
    public String getName() {
        return ""; // stub
    }

    // EFFECTS: Return total duration of this workout plan in seconds
    public abstract double getDuration();

    // EFFECTS: Return list of exercises this workout plan includes
    public abstract List<Exercise> getExercises();

    // EFFECTS: Return key-value pairs summarizing this workout plan
    public abstract Map<String, Double> getWorkoutSummary();

}
