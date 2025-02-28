package model.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.exercise.Exercise;
import model.exercise.ExerciseLibrary;
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
    //          Throw IllegalArgumentException if workoutName is null
    public WorkoutPlan getWorkout(String workoutName) throws IllegalArgumentException {
        if (workoutName == null) {
            throw new IllegalArgumentException();
        }

        WorkoutPlan workoutPlan = library.get(workoutName);
        return workoutPlan;
    }

    // EFFECTS: Return a list of all created and stored workout plans
    public List<WorkoutPlan> getAllWorkouts() {
        ArrayList<WorkoutPlan> workouts = new ArrayList<WorkoutPlan>(); // Defensive copy
        
        for (Map.Entry<String, WorkoutPlan> workoutPair : library.entrySet()) {
            workouts.add(workoutPair.getValue());
        }

        return workouts;
    }

    // HELPER: for addWorkout, removeWorkout
    // EFFECTS: Return true if an exercise with the given name exists in the library, false otherwise
    private boolean containsWorkout(String workoutName) {
        return library.containsKey(workoutName);
    }

    // EFFECTS: Return a JSON representation of this WorkoutLibrary containing
    //          all WorkoutPlan object names and their list of Exercises by name
    // NOTE: This data is sufficient for full program state reconstruction
    //       WorkoutLibrary ENFORCES WorkoutPlan objects never associate with null Exercises
    //       ExerciseLibrary ENFORCES unique Exercise names for each Exercise 
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray workoutsArray = new JSONArray();
        
        for (WorkoutPlan workoutPlan : library.values()) {
            workoutsArray.put(createWorkoutJson(workoutPlan));
        }
        json.put("workouts", workoutsArray);
        return json;
    }
    
    // HELPER: for toJson
    // EFFECTS: Create a JSON object representing the given WorkoutPlan
    //          If WorkoutPlan is a Workout, include exercise names
    //          If WorkoutPlan is a RestDay, include only name and type
    //          UI removes all references to an Exercise object from each Workout in WorkoutLibrary
    //          IFF the user deletes an exercise from ExerciseLibrary
    private JSONObject createWorkoutJson(WorkoutPlan workoutPlan) {
        JSONObject workoutJson = new JSONObject();
        workoutJson.put("name", workoutPlan.getName());
        
        // Workout in WorkoutLibrary is never null
        if (workoutPlan instanceof RestDay) {
            workoutJson.put("type", "RestDay");
            // fromJson treats a type Workout without exercises as a RestDay (bug handling)
        } else {
            workoutJson.put("type", "Workout");
            workoutJson.put("exercises", createExerciseNamesArray((Workout) workoutPlan));
        }
        
        return workoutJson;
    }
    
    // HELPER: for createWorkoutJson
    // EFFECTS: Create a JSON array of exercise names from the given Workout
    private JSONArray createExerciseNamesArray(Workout workout) {
        JSONArray exerciseNames = new JSONArray();
        for (Exercise exercise : workout.getExercises()) {
            exerciseNames.put(exercise.getName());
        }
        return exerciseNames;
    }
    
    // REQUIRES: toJson's output is not modified to this program's persistence is not modified
    // MODIFIES: this
    // EFFECTS: Reconstruct this WorkoutLibrary's state from the provided JSON data
    //          Throw JSONException if data is invalid or incomplete
    // NOTE: The REQUIRES clause is necessary for fromJson to function correctly. However,  
    //       there is extensive error handling, exception throwing, and default value 
    //       employing for missing or corrupted fields/data structures. Ultimately, even if 
    //       the REQUIRES clause is not met, fromJson handles these issues with default or fallback
    //       states without exposing errors or exceptions to the user interface.
    //       Exceptions are handled at three levels: this, JsonManager, PersistenceUI
    @Override
    public void fromJson(JSONObject json, Object data) throws JSONException {
        if (data == null || !(data instanceof ExerciseLibrary)) {
            throw new IllegalArgumentException("ExerciseLibrary required for state reconstruction");
        }
        ExerciseLibrary exerciseLibrary = (ExerciseLibrary) data;
        
        // Clear library and reconstruct from loaded data
        library.clear();
        if (json == null || !json.has("workouts")) {
            return; // Non-existent loading data
        }
    
        JSONArray workoutsArray = json.getJSONArray("workouts");
        reconstructWorkouts(workoutsArray, exerciseLibrary);
    }
    
    // HELPER: for fromJson
    // MODIFIES: this
    // EFFECTS: Reconstruct WorkoutPlan objects from JSON array and add to library
    //          Skip invalid workout entries
    private void reconstructWorkouts(JSONArray workoutsArray, ExerciseLibrary exerciseLibrary) {
        for (int i = 0; i < workoutsArray.length(); i++) {
            JSONObject workoutJson = workoutsArray.getJSONObject(i);
            WorkoutPlan workoutPlan = createWorkoutPlan(workoutJson, exerciseLibrary);
            if (workoutPlan != null) {
                library.put(workoutPlan.getName(), workoutPlan);
            }
        }
    }
    
    // HELPER: for reconstructWorkouts
    // EFFECTS: Create a WorkoutPlan from JSON data
    //          Return null if required fields are missing or type is invalid
    private WorkoutPlan createWorkoutPlan(JSONObject workoutJson, ExerciseLibrary exerciseLibrary) {
        String name = workoutJson.optString("name", null);
        String type = workoutJson.optString("type", null);
    
        if (name == null || type == null) {
            return null;
        }
    
        if (type.equals("RestDay")) {
            return new RestDay(name);
        } else if (type.equals("Workout")) {
            return createWorkout(name, workoutJson, exerciseLibrary);
        }
        
        return null;
    }
    
    // HELPER: for createWorkoutPlan
    // EFFECTS: Create a Workout from JSON data and exercise library
    //          Return a Workout with valid exercises from the exercise library
    private Workout createWorkout(String name, JSONObject workoutJson, ExerciseLibrary exerciseLibrary) {
        List<Exercise> exercises = new ArrayList<Exercise>();
        
        if (workoutJson.has("exercises")) {
            JSONArray exerciseNames = workoutJson.getJSONArray("exercises");
            exercises = reconstructExercises(exerciseNames, exerciseLibrary);
        }
        
        return new Workout(name, exercises);
    }
    
    // HELPER: for createWorkout
    // EFFECTS: Create list of exercises from JSON array of exercise names
    //          Only include exercises that exist in the exercise library
    private List<Exercise> reconstructExercises(JSONArray exerciseNames, ExerciseLibrary exerciseLibrary) {
        List<Exercise> exercises = new ArrayList<Exercise>();
        
        for (int i = 0; i < exerciseNames.length(); i++) {
            String exerciseName = exerciseNames.getString(i);
            // ExerciseLibrary's fromJson GUARANTEES no null Exercise objects during reconstruction
            // The program at runtime does not allow for the user to create a null Exercise
            Exercise exercise = exerciseLibrary.getExercise(exerciseName);
            exercises.add(exercise);
        }
        return exercises;
    }
    
}
