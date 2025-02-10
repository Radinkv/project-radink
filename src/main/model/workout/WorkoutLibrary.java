package model.workout;

import java.util.ArrayList;
import java.util.List;

/**
 * REPRESENTS: a collection of workout plans (both workouts and rest days)
 * 
 * USED BY:
 *      1. WeeklySchedule to assign workouts to specific days
 *      2. UI components that display stored workouts
 * 
 * PURPOSE: Stores and manages a set of named workout plans
 *          Ensures each workout has a unique name
 *          Allows retrieval and removal of stored workouts
 * 
 * MUTABILITY: Mutable
 */
public class WorkoutLibrary {

    // MODIFIES: this
    // EFFECTS: Add the given workout plan to the library if no workout with the same name exists
    //          Throw IllegalArgumentException if workoutPlan is null 
    //          Throw IllegalArgumentException if a workout with the same name already exists
    public void addWorkout(WorkoutPlan workoutPlan) throws IllegalArgumentException {
        return; // stub
    }

    // MODIFIES: this
    // EFFECTS: Remove the workout with the given name if present
    //          Throw IllegalArgumentException if workoutName is null 
    //          Throw IllegalArgumentException if no workout with this name exists
    public void removeWorkout(String workoutName) throws IllegalArgumentException {
        return; // stub
    }

    // EFFECTS: Return the workout plan with the given name
    //          Throw IllegalArgumentException if workoutName is null or does not exist
    public WorkoutPlan getWorkout(String workoutName) throws IllegalArgumentException {
        return new RestDay(""); // stub
    }

    // EFFECTS: Return a list of all created and stored workout plans
    public List<WorkoutPlan> getAllWorkouts() {
        return new ArrayList<>(); // stub
    }
}
