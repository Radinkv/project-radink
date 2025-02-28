package persistence;

import org.json.*;

/**
 * REPRESENTS: An abstraction for JSON persistence capability
 * 
 * USED BY:
 *      1. Model classes (ExerciseLibrary, WorkoutLibrary, WeeklySchedule) that need to save/load their state
 *      2. JsonManager to handle data persistence operations
 * 
 * PURPOSE: To define a standard interface for class-JSON data saving and loading 
 *          Ensure consistent persistence structure and behavior across model components
 *          Maintain separation between persistence logic and model logic
 */
public interface Writable {
    // EFFECTS: Return a JSON representation of this object's complete state
    //          Implementation must ensure all relevant data is captured
    JSONObject toJson();

    // EFFECTS: Reconstruct this object's state from the provided JSON data
    //          Implementation restores the complete state of the object
    //          Throw JSONException if data is invalid or cannot be properly loaded
    // NOTE:    The collection parameter provides access to previously instantiated objects that are required for state
    //          reconstruction:
    //              - ExerciseLibrary uses PredefinedData to access Equipment, Muscle, and MuscleGroup objects
    //              - WorkoutLibrary uses ExerciseLibrary to appoint Exercise references to WorkoutPlan objects by name
    //              - WeeklySchedule uses WorkoutLibrary to place WorkoutPlan objects within the proper schedule slots
    //          This approach allows precise object resolution and construction during JSON deserialization
    void fromJson(JSONObject json, Object collection) throws IllegalArgumentException;
}