package persistence;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * REPRESENTS: A class for uniform JSON data saving and/or loading
 * 
 * USED BY:
 *      1. Model classes (ExerciseLibrary, WorkoutLibrary, WeeklySchedule) 
 *         that implement Writable to save/load their state
 * 
 * PURPOSE: Ensure consistent persistence structure and behavior across model components
 *          Maintain separation between persistence logic and model logic
 */
public class JsonManager {
    private static final String SAVE_PATH = "./data/workout-data.json";

    // EFFECTS: Save Writable objects' states to a single JSON file
    //          Create or overwrite the JSON file at SAVE_PATH
    //          Throw JSONException if any component from components cannot be properly saved
    public void saveData(Map<String, Writable> components) throws JSONException {
        return; // stub
    }

    // EFFECTS: Load and reconstruct Writable objects from JSON
    //          Creates new instances if JSON file doesn't exist
    //          Throw JSONException if data cannot be properly loaded 
    public Map<String, JSONObject> loadData() throws JSONException {
        return new HashMap<String, JSONObject>(); // stub
    }
}