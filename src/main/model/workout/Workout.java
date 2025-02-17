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
public class Workout implements WorkoutPlan {
    private String workoutName;
    private List<Exercise> exercises;

    // EFFECTS: Create a workout with the given name and exercises
    //          If workoutName is null or exercises is null or contains null elements,
    //          throw IllegalArgumentException
    public Workout(String workoutName, List<Exercise> exercises) {
        if (workoutName == null || exercises == null) {
            throw new IllegalArgumentException("Workout name and exercises list cannot be null.");
        }
        if (exercises.contains(null)) {
            throw new IllegalArgumentException("Exercise list cannot contain null elements.");
        }
        this.workoutName = workoutName;
        this.exercises = new ArrayList<Exercise>(exercises); // Defensive copy
    }

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Add each exercise's metrics from its equipment and muscle groups under the given context
    @Override
    public void activateMetrics(String context) {
        for (Exercise exercise : exercises) {
            exercise.activateMetrics(context);
        }
    }

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Remove each exercise's metrics from its equipment and muscle groups under the given context
    @Override
    public void deactivateMetrics(String context) {
        for (Exercise exercise : exercises) {
            exercise.deactivateMetrics(context); // NOTE: ExerciseAssociator ultimately handles
        }                                        //       all ill-advised metrics or metric values 
    }

    // EFFECTS: Return this workout's name
    @Override
    public String getName() {
        return workoutName;
    }

    // EFFECTS: Return the total duration of this workout in seconds
    @Override
    public double getDuration() {
        int sum = 0;
        for (Exercise e : exercises) {
            sum += e.getDuration();
        }
        return sum;
    }

    // EFFECTS: Return a list of exercises this workout includes
    @Override
    public List<Exercise> getExercises() {
        return new ArrayList<Exercise>(exercises); // Defensive copy
    }

    // EFFECTS: Calculate cumulative metrics across all exercises
    //          Initialize all possible metric types to 0.0
    //          Sum corresponding metrics from each exercise
    //          Return map of aggregated workout statistics
    @Override
    public Map<String, Double> getWorkoutSummary() {
        Map<String, Double> cumulativeWorkoutStats = new HashMap<String, Double>();
        // All possible metrics from ExerciseAssociator
        cumulativeWorkoutStats.put("totalSets", 0.0);
        cumulativeWorkoutStats.put("totalReps", 0.0);
        cumulativeWorkoutStats.put("totalIntervalDuration", 0.0);
        cumulativeWorkoutStats.put("totalEnduranceDuration", 0.0);
        cumulativeWorkoutStats.put("totalStrengthDuration", 0.0);
        cumulativeWorkoutStats.put("totalRestTimeBetween", 0.0);
        cumulativeWorkoutStats.put("totalDuration", 0.0);        
        for (Exercise exercise : exercises) {
            Map<String, Double> exerciseInfo = exercise.convertInfoToAssociatorFormat();
            for (Map.Entry<String, Double> entry : exerciseInfo.entrySet()) {
                cumulativeWorkoutStats.merge(entry.getKey(), entry.getValue(), 
                        (thisVal, cumulativeVal) -> thisVal + cumulativeVal);
            }
        }
        return cumulativeWorkoutStats;
    }
}
