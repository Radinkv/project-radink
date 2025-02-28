package model.exercise;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.equipment.Equipment;
import model.muscle.MuscleGroup;
import persistence.Writable;
import utility.PredefinedData;

/**
 * REPRESENTS: A centralized collection of all available exercises.
 * 
 * USED BY:
 *      1. Workouts that reference stored exercises
 *      2. MuscleGroups that track exercise impacts
 *      3. Equipment that organizes exercises by usage
 * 
 * PURPOSE: Manage the storage, retrieval, and removal of exercises from a central hub/library
 *          As exercises are immutable ExerciseLibrary tracks various exercises consistently 
 *          across the program.
 * 
 * MUTABILITY: Mutable 
 */
public class ExerciseLibrary implements Writable {

    Map<String, Exercise> library;
    
    public ExerciseLibrary() {
        library = new HashMap<String, Exercise>();
    }

    // ExerciseLibrary will move to this constructor
    // It needs PredefinedData to reconstruct Exercise objects with the 
    // Equipment and MuscleGroup instantiations of PredefinedData (shared within the program)
    public ExerciseLibrary(PredefinedData predefinedData) {
        library = new HashMap<String, Exercise>();
    }

    // MODIFIES: this
    // EFFECTS: Adds the given exercise to the library if not already present.
    //          Returns true if the exercise was added, false otherwise.
    public boolean addExercise(Exercise exercise) {
        if (exercise == null || library.containsKey(exercise.getName())) {
            return false;
        } else {
            library.put(exercise.getName(), exercise);
            return true;
        }
    }

    // MODIFIES: this
    // EFFECTS: Removes the exercise with the given name from the library if present.
    //          Returns true if the exercise was removed, false otherwise.
    public boolean removeExercise(String exerciseName) {
        if (containsExercise(exerciseName)) {
            library.remove(exerciseName);
            return true;
        }
        return false;
    }

    // EFFECTS: Return the exercise with the given name, or null if not found.
    public Exercise getExercise(String exerciseName) {
        return library.get(exerciseName);
    }

    // EFFECTS: Returns true if an exercise with the given name exists in the library, false otherwise.
    public boolean containsExercise(String exerciseName) {
        return library.containsKey(exerciseName);
    }


    // EFFECTS: Return all stored exercises and their names.
    public Map<String, Exercise> getAllExercises() {
        return new HashMap<String, Exercise>(library); // Defensive copy
    }

    // EFFECTS: Return a JSON representation of this ExerciseLibrary containing
    //          all exercises and their complete state
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray exercisesJson = new JSONArray();
        
        for (Exercise exercise : library.values()) {
            JSONObject exerciseJson = new JSONObject();

            exerciseJson.put("name", exercise.getName());
            exerciseJson.put("type", exercise.exerciseType());

            // Names are sufficient for storing Equipment and MuscleGroup because they always come from PredefinedData
            // In other words, can be reconstructed solely by name
            // Check for null equipment and muscle group before calling their methods
            String equipmentName = (exercise.getRequiredEquipment() != null) // null Equipment is "valid"
                    ? exercise.getRequiredEquipment().getEquipmentName() : "Bodyweight"; // Default value
            String muscleGroupName = (exercise.getMusclesTargeted() != null) // null MuscleGroup is "valid"
                    ? exercise.getMusclesTargeted().getName() : "Unnamed MuscleGroup"; // Default value
            
            exerciseJson.put("equipmentName", equipmentName);
            exerciseJson.put("muscleGroupName", muscleGroupName);

            /* Note: Metrics are NOT stored for each Exercise. Once ExerciseLibrary, WorkoutLibrary, and 
             * WeeklySchedule state is restored, WeeklySchedule will activate metrics for WorkoutPlan
             * objects that are Workouts and in WeeklySchedule */
            
            // Exercise-specific info for reconstructing Exercise objects
            JSONObject exerciseInfo = new JSONObject();
            for (Map.Entry<String, Double> entry : exercise.getInfo().entrySet()) {
                exerciseInfo.put(entry.getKey(), entry.getValue());
            }
            exerciseJson.put("info", exerciseInfo);
            exercisesJson.put(exerciseJson);
        }
        json.put("exercises", exercisesJson);
        return json;
    }

    // REQUIRES: toJson's output is not modified to this program's persistence is not modified
    // MODIFIES: this
    // EFFECTS: Reconstruct this ExerciseLibrary's state from the provided JSON data
    //          Throw JSONException if data is invalid or incomplete
    // NOTE: The REQUIRES clause is necessary for fromJson to function correctly. However,  
    //       there is extensive error handling, exception throwing, and default value 
    //       employing for missing or corrupted fields/data structures. Ultimately, even if 
    //       the REQUIRES clause is not met, fromJson handles these issues with default or fallback
    //       states without exposing errors or exceptions to the user interface.
    //       Exceptions are handled at three levels: this, JsonManager, PersistenceUI
    @Override
    public void fromJson(JSONObject json, Object data) throws IllegalArgumentException {
        if (!(data instanceof PredefinedData)) {
            throw new IllegalArgumentException("ExerciseLibrary requires PredefinedData to reconstruct.");
        } 
        
        if (json == null || !json.has("exercises")) {
            return; // Empty library is valid state
        }

        PredefinedData predefinedData = (PredefinedData) data;

        // Clear existing library to replace previous data with loaded data
        library.clear();

        JSONArray exercisesJson = json.getJSONArray("exercises");
        for (int i = 0; i < exercisesJson.length(); i++) {
            JSONObject exerciseJson = exercisesJson.getJSONObject(i);

            // Shared trivial information between each Exercise
            String name = exerciseJson.optString("name", null);
            String type = exerciseJson.optString("type", null);
            String equipmentName = exerciseJson.optString("equipmentName", "Unnamed Equipment");
            String muscleGroupName = exerciseJson.optString("muscleGroupName", "Unnamed MuscleGroup");

            // Shared non-trivial information between each Exercise
            // Get equipment and muscle group from predefined data
            // Defaults to Bodyweight and Unnamed MuscleGroup if equipment or muscleGroup not found
            Equipment equipment = predefinedData.findEquipment(equipmentName);
            MuscleGroup muscleGroup = predefinedData.findMuscleGroup(muscleGroupName);

            JSONObject exerciseInfo = exerciseJson.optJSONObject("info", new JSONObject());

            // Create appropriate exercise type based on the stored Exercise type
            // Exercise object instantiations automatically handle null type, name
            // As mentioned, PredefinedData gives default Equipment and MuscleGroup should they not be found
            // createExercise elicits default Exercise instantiation values for any invalid values
            Exercise exercise = createExercise(type, name, exerciseInfo, equipment, muscleGroup);

            library.put(exercise.getName(), exercise);
        }
    }

    // HELPER: for fromJson
    // EFFECTS: Create the exercise based on the given type
    //          Construct a default EnduranceExercise (least info attributes for an Exercise subclass)
    //          if type is not one of: "Strength", "Endurance", or "Interval"
    private Exercise createExercise(String type, String name, JSONObject exerciseInfo, 
            Equipment equipment, MuscleGroup muscleGroup) {
        switch (type) {
            case "Strength":
                return reconstructStrengthExercise(name, exerciseInfo, equipment, muscleGroup);
            case "Endurance":
                return reconstructEnduranceExercise(name, exerciseInfo, equipment, muscleGroup);
            case "Interval":
                return reconstructIntervalExercise(name, exerciseInfo, equipment, muscleGroup);
            default:
                return reconstructEnduranceExercise(name, exerciseInfo, equipment, muscleGroup); // Default
        }
    }


    // HELPER: for fromJson
    // EFFECTS: Instantiate a StrengthExercise object with the given saved data
    //          Throw Runtime JSONException if any of sets, reps, timePerRep, or restTime are unknown attributes
    private StrengthExercise reconstructStrengthExercise(String name, JSONObject info, 
            Equipment equipment, MuscleGroup muscleGroup) {
        return new StrengthExercise(
            name,
            info.optInt("sets", -1),
            info.optInt("reps", -1),
            info.optDouble("timePerRep", -1.0), // Negative values won't be allowed by the exercise
            info.optDouble("restTime", -1.0),  // Will be set to whatever default is specified
            equipment,                            // Same goes for each Exercise subclass
            muscleGroup
        );
    }

    // HELPER: for fromJson
    // EFFECTS: Instantiate a EnduranceExercise object with the given saved data
    //          Throw Runtime JSONException if duration is an unknown attribute
    private EnduranceExercise reconstructEnduranceExercise(String name, JSONObject info,
            Equipment equipment, MuscleGroup muscleGroup) {
        return new EnduranceExercise(
            name,
            info.optDouble("duration", -1.0), // Negative for instantiated 0 default
            equipment,
            muscleGroup
        );
    }

    // HELPER: for fromJson
    // EFFECTS: Instantiate a IntervalExercise object with the given saved data
    //          Throw Runtime JSONException if any of timeOn, timeOff, or repetitions are unknown attributes
    private IntervalExercise reconstructIntervalExercise(String name, JSONObject info,
            Equipment equipment, MuscleGroup muscleGroup) throws JSONException {
        return new IntervalExercise(
            name,
            info.optDouble("timeOn", -1.0), // Negatives for instantiated 0 default
            info.optDouble("timeOff", -1.0),
            info.optInt("repititions", -1),
            equipment,
            muscleGroup
        );
    }
}
