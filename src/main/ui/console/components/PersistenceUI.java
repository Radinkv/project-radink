package ui.console.components;

import static ui.console.components.SharedUI.*;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import persistence.JsonManager;

public class PersistenceUI {

    // REQUIRES: exerciseLibrary, workoutLibrary, weeklySchedule, and predefinedData are not null (from SharedUI)
    // EFFECTS: IF user confirms, saves the current state of exerciseLibrary, workoutLibrary, 
    //          and weeklySchedule to file
    //          If save fails, prints error message
    //          If user cancels, prints cancellation message
    public void saveState() {
        System.out.print("\nAre you sure you want to save the current program state? "
                + "This will override any previous save. (y/n): ");
        String confirmation = input.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y")) {
            try {
                Map<String, JSONObject> components = new HashMap<String, JSONObject>();
                components.put("exerciseLibrary", exerciseLibrary.toJson());
                components.put("workoutLibrary", workoutLibrary.toJson());
                components.put("weeklySchedule", weeklySchedule.toJson());
                
                JsonManager.saveData(components);
                System.out.println("Program state saved successfully!");
            } catch (JSONException e) {
                System.out.println("Error saving program state: " + e.getMessage());
            }
        } else {
            System.out.println("Save cancelled.");
        }
        SharedUI.waitForEnter();
    }
    
    // REQUIRES: exerciseLibrary, workoutLibrary, weeklySchedule, and predefinedData are not null (from SharedUI)
    // MODIFIES: exerciseLibrary, workoutLibrary, weeklySchedule (this program) 
    // EFFECTS: IF user confirms, load saved WorkoutApp state from file into exerciseLibrary, workoutLibrary,
    //          and weeklySchedule in that order
    //          If no saved state exists, prints appropriate message
    //          If load fails, prints error message and leaves current state unchanged
    //          If user cancels, prints cancellation message
    public void loadState() {
        System.out.print("\nAre you sure you want to load the previous program state? " 
                + "This will replace all current data. (y/n): ");
        String confirmation = input.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y")) {
            try {
                Map<String, JSONObject> data = JsonManager.loadData();
                
                if (data.isEmpty()) {
                    System.out.println("No saved state found.");
                    SharedUI.waitForEnter();
                    return;
                }
    
                // Load in correct order (ABSOLUTELY NECESSARY)
                // Third dependent on second, second dependent on first
                // Do not execute the next if the previous does not work
                if (data.containsKey("exerciseLibrary")) {
                    exerciseLibrary.fromJson(data.get("exerciseLibrary"), predefinedData);
                    // System.out.println(exerciseLibrary.toJson());
                    if (data.containsKey("workoutLibrary")) {
                        workoutLibrary.fromJson(data.get("workoutLibrary"), exerciseLibrary);
                        // System.out.println(workoutLibrary.toJson());
                        if (data.containsKey("weeklySchedule")) {
                            weeklySchedule.fromJson(data.get("weeklySchedule"), workoutLibrary);
                            // System.out.println(weeklySchedule.toJson());
                        }
                    }
                }

                System.out.println("Program state loaded successfully!");
            } catch (JSONException | IllegalArgumentException e) {
                System.out.println("Error loading program state: " + e.getMessage());
            }
        } else {
            System.out.println("Load cancelled.");
        }
        SharedUI.waitForEnter();
    }
}
