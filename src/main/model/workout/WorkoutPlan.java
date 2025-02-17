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
public interface WorkoutPlan {

    // EFFECTS: Return name of this workout plan
    public String getName();

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Facilitate the addition of copies of each Exercise's getInfo from their respective Equipment
    //          and MuscleGroup under this WorkoutPlan if this is a Workout. If already present or this is a 
    //          RestDay, make no changes
    public void activateMetrics(String context);

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Facilitate the removal of copies of each Exercise's getInfo from their respective Equipment
    //          and MuscleGroup under this WorkoutPlan if this is a Workout. If not already present or this  
    //          is a RestDay, make no changes
    public void deactivateMetrics(String context);

    // EFFECTS: Return total duration of this workout plan in seconds
    public double getDuration();

    // EFFECTS: Return list of exercises this workout plan includes
    public List<Exercise> getExercises();

    // EFFECTS: Return key-value pairs summarizing this workout plan
    public Map<String, Double> getWorkoutSummary();

}
