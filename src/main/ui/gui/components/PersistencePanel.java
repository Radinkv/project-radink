package ui.gui.components;

import org.json.JSONException;
import org.json.JSONObject;
import persistence.JsonManager;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This panel handles saving and loading the program state.
 * It provides functionality to save and load exercise library, workout library, and weekly schedule data.
 */
public class PersistencePanel extends JPanel {
    
    // EFFECTS: Instantiate this persistence panel
    public PersistencePanel() {
        setupPanel();
    }

    // HELPER: for PersistencePanel
    // EFFECTS: Sets up the panel layout and background
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // EFFECTS: Saves the current state of the application to file if user confirms
    // the action, displays appropriate success or error messages
    public void saveState() {
        boolean confirmed = showSaveConfirmationDialog();
        
        if (confirmed) {
            performSaveOperation();
        }
    }
    
    // HELPER: for saveState
    // EFFECTS: Shows a confirmation dialog for saving and returns user's decision
    private boolean showSaveConfirmationDialog() {
        return SharedGuiComponents.showConfirmation(
                "Are you sure you want to save the current program state? "
                + "This will override any previous save.");
    }
    
    // HELPER: for saveState
    // EFFECTS: Performs the actual save operation and shows result messages
    private void performSaveOperation() {
        try {
            Map<String, JSONObject> components = collectComponentData();
            JsonManager.saveData(components);
            SharedGuiComponents.showInfo("Program state saved successfully!");
        } catch (JSONException e) {
            SharedGuiComponents.showError("Error saving program state: " + e.getMessage());
        }
    }
    
    // HELPER: for performSaveOperation 
    // EFFECTS: Collects JSON data from all application components and returns as a map
    private Map<String, JSONObject> collectComponentData() {
        Map<String, JSONObject> components = new HashMap<String, JSONObject>();
        components.put("exerciseLibrary", SharedGuiComponents.exerciseLibrary.toJson());
        components.put("workoutLibrary", SharedGuiComponents.workoutLibrary.toJson());
        components.put("weeklySchedule", SharedGuiComponents.weeklySchedule.toJson());
        
        return components;
    }
    
    // EFFECTS: Loads previously saved state if user confirms the action,
    // displays appropriate success or error messages
    public void loadState() {
        boolean confirmed = showLoadConfirmationDialog(); 
        if (confirmed) {
            performLoadOperation();
        }
    }
    
    // HELPER: for loadState
    // EFFECTS: Shows a confirmation dialog for loading and returns user's decision
    private boolean showLoadConfirmationDialog() {
        return SharedGuiComponents.showConfirmation(
                "Are you sure you want to load the previous program state? "
                + "This will replace all current data.");
    }
    
    // MODIFIES: WorkoutLibrary, ExerciseLibrary, WeeklySchedule
    // HELPER: for loadState
    // EFFECTS: Performs the actual load operation and shows result messages
    private void performLoadOperation() {
        try {
            Map<String, JSONObject> data = JsonManager.loadData();
            
            if (data.isEmpty()) {
                SharedGuiComponents.showInfo("No saved state found.");
                return;
            }

            loadDataInOrder(data);
            
            SharedGuiComponents.showInfo("Program state loaded successfully!");
        } catch (JSONException | IllegalArgumentException e) {
            SharedGuiComponents.showError("Error loading program state: " + e.getMessage());
        }
    }

    // MODIFIES: WorkoutLibrary, ExerciseLibrary, WeeklySchedule
    // HELPER: for performLoadOperation
    // EFFECTS: Loads data in the correct order to maintain dependencies
    private void loadDataInOrder(Map<String, JSONObject> data) {
        if (data.containsKey("exerciseLibrary")) {
            SharedGuiComponents.exerciseLibrary.fromJson(
                    data.get("exerciseLibrary"), 
                    SharedGuiComponents.predefinedData
            );
            if (data.containsKey("workoutLibrary")) {
                SharedGuiComponents.workoutLibrary.fromJson(
                        data.get("workoutLibrary"), 
                        SharedGuiComponents.exerciseLibrary
                );
                if (data.containsKey("weeklySchedule")) {
                    SharedGuiComponents.weeklySchedule.fromJson(
                            data.get("weeklySchedule"), 
                            SharedGuiComponents.workoutLibrary
                    );
                }
            }
        }
    }

    // EFFECTS: Prompts the user to save before exiting the application
    public void promptSaveOnExit() {
        boolean wantToSave = SharedGuiComponents.showConfirmation(
                "Would you like to save your progress before exiting?");
        if (wantToSave) {
            saveState();
        }
    }
}