package persistence;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * REPRESENTS: A class for uniform JSON data saving and/or loading
 * 
 * USED BY:
 *      1. Model classes (ExerciseLibrary, WorkoutLibrary, WeeklySchedule) 
 *         that implement JSONObject to save/load their state
 * 
 * PURPOSE: Ensure consistent persistence structure and behavior across model components
 *          Maintain separation between persistence logic and model logic
 * 
 * Mutability: Static
 */
public class JsonManager {
    private static final String DEFAULT_SAVE_PATH = "./data/workout-data.json";

    // EFFECTS: Throw AssertionError for instantiation avoidance (static utility class)
    public JsonManager() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    // EFFECTS: Save JSONObject objects' toJson states to a single JSON file with the given String as id
    //          Create or overwrite the JSON file at DEFAULT_SAVE_PATH
    //          Throw JSONException if any component from components cannot be properly saved
    // NOTE: This method signature is for TYPICAL USAGE purposes. It saves JSONObject object's JSON data
    //       (with a given String as a key) to the DEFAULT path.
    public static void saveData(Map<String, JSONObject> components) throws JSONException {
        saveData(components, DEFAULT_SAVE_PATH);
    }

    // EFFECTS: Save JSONObject objects' toJson states to a single JSON file at the specified path
    //          Create or overwrite the JSON file at the given savePath
    //          Throw JSONException if any component from components cannot be properly saved
    // NOTE: This method signature is for TEST purposes. It allows for temporary path re-direction
    //       To prevent the modification of the actual data persistence file the program uses.
    public static void saveData(Map<String, JSONObject> components, String savePath) throws JSONException {
        // Ensure directory exists
        String directoryPath = new File(savePath).getParent();
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        JSONObject json = new JSONObject();
        
        // Save each component's state
        // NOTE: JSONObject objects are model objects that implement JSONObject
        //       They have fromJson and toJson methods
        for (Map.Entry<String, JSONObject> entry : components.entrySet()) {
            // Key corresponds to the sought-for key by the respective model component in parsing its encapsulated data
            // Given by WorkoutApp, but must match what the respective model class looks for
            json.put(entry.getKey(), entry.getValue());
        }

        // Write to file
        try (PrintWriter writer = new PrintWriter(new FileWriter(savePath))) {
            writer.println(json.toString(4));
        } catch (IOException e) {
            throw new JSONException("Unable to write to file: " + e.getMessage());
        }
    }

    // EFFECTS: Load and reconstruct JSONObject objects from JSON at DEFAULT_SAVE_PATH
    //          Return an empty Map if JSON file does not exist
    //          Throw JSONException if data cannot be properly loaded
    // NOTE: This method signature is for TYPICAL USAGE purposes. It loads JSONObject object's JSON data
    //       (with each given with a String as a key) into the DEFAULT path file.
    public static Map<String, JSONObject> loadData() throws JSONException {
        return loadData(DEFAULT_SAVE_PATH);
    }

    // EFFECTS: Load and reconstruct JSONObject objects from JSON at the specified path
    //          Return an empty Map if JSON file does not exist
    //          Throw JSONException if data cannot be properly loaded
    // NOTE: This method signature is for TEST purposes. It allows for temporary path re-direction
    //       To prevent the modification of the actual data persistence file the program uses. 
    public static Map<String, JSONObject> loadData(String savePath) throws JSONException {
        Map<String, JSONObject> loadedData = new HashMap<String, JSONObject>();
        
        // Create file if it does not exist
        File dataFile = new File(savePath);
        if (!dataFile.exists()) {
            return loadedData;
        }

        try {
            // Read the entire file content
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
            }

            // Extract and store component data in a JSONObject for each component
            // Each model component that requires saved data to load has a corresponding key and data
            JSONObject json = new JSONObject(content.toString());
            for (String key : json.keySet()) {
                loadedData.put(key, json.getJSONObject(key));
            }

        } catch (IOException e) {
            throw new JSONException("Unable to read from file: " + e.getMessage());
        }
        
        // Loaded data given to UI component to re-initialize each corresponding model components with its data
        return loadedData;
    }
}