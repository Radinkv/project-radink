package model.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import persistence.Writable;

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
public class WorkoutLibrary implements Writable {
    private Map<String, WorkoutPlan> library;

    public WorkoutLibrary() {
        library = new HashMap<String, WorkoutPlan>();
    }

    // MODIFIES: this
    // EFFECTS: Add the given workout plan to the library if no workout with the same name exists
    //          Throw IllegalArgumentException if workoutPlan is null 
    //          Throw IllegalArgumentException if a workout with the same name already exists
    public void addWorkout(WorkoutPlan workoutPlan) throws IllegalArgumentException {
        if (workoutPlan == null || containsWorkout(workoutPlan.getName())) {
            throw new IllegalArgumentException();
        } else {
            library.put(workoutPlan.getName(), workoutPlan);
        }
    }

    // MODIFIES: this
    // EFFECTS: Remove the workout with the given name if present
    //          Throw IllegalArgumentException if workoutName is null 
    //          Throw IllegalArgumentException if no workout with this name exists
    public void removeWorkout(String workoutName) throws IllegalArgumentException {
        if (workoutName == null || !containsWorkout(workoutName)) {
            throw new IllegalArgumentException();
        } else {
            library.remove(workoutName);
        }
    }

    // EFFECTS: Return the workout plan with the given name
    //          Throw IllegalArgumentException if workoutName is null or does not exist
    public WorkoutPlan getWorkout(String workoutName) throws IllegalArgumentException {
        if (workoutName == null) {
            throw new IllegalArgumentException();
        }

        WorkoutPlan workoutPlan = library.get(workoutName);

        if (workoutPlan == null) {
            throw new IllegalArgumentException();
        }

        return workoutPlan;
    }

    // EFFECTS: Return a list of all created and stored workout plans
    public List<WorkoutPlan> getAllWorkouts() {
        ArrayList<WorkoutPlan> workouts = new ArrayList<WorkoutPlan>();
        
        for (Map.Entry<String, WorkoutPlan> workoutPair : library.entrySet()) {
            workouts.add(workoutPair.getValue());
        }

        return workouts;
    }

    // EFFECTS: Return true if an exercise with the given name exists in the library, false otherwise
    private boolean containsWorkout(String workoutName) {
        return library.containsKey(workoutName);
    }

    // EFFECTS: Return a JSON representation of this WorkoutLibrary containing
    //          all workouts and their complete state
    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    // MODIFIES: this
    // EFFECTS: Reconstruct this WorkoutLibrary's state from the provided JSON data
    //          Throws JSONException if data is invalid or incomplete
    @Override
    public void fromJson(JSONObject json) throws JSONException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
    
}
