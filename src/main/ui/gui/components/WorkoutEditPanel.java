package ui.gui.components;

import model.exercise.Exercise;
import model.workout.Workout;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This panel handles workout editing. It extends the WorkoutModificationPanel to provide
 * specific functionality for modifying existing workouts by adding or removing exercises.
 */
public class WorkoutEditPanel extends WorkoutModificationPanel {
    
    private Workout currentWorkout;
    private JLabel workoutNameLabel;
    private JButton saveButton;
    private JButton cancelButton;
    
    // EFFECTS: Instantiate this WorkoutEditPanel 
    public WorkoutEditPanel() {
        super();
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons specific to workout editing
    @Override
    protected void createActionButtons() {
        saveButton = SharedGuiComponents.createStyledButton("Save Changes");
        saveButton.addActionListener(e -> saveWorkoutChanges());
        
        cancelButton = SharedGuiComponents.createStyledButton("Cancel");
        cancelButton.addActionListener(e -> cancelEditing());
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the header panel with title and workout name display
    @Override
    protected JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JLabel titleLabel = createTitleLabel();
        JPanel namePanel = createNamePanel();
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(namePanel, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for createHeaderPanel
    // EFFECTS: Create and configure the title label for the panel with proper styling
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Edit Workout");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return titleLabel;
    }

    // HELPER: for createHeaderPanel
    // EFFECTS: Create the panel for workout name display (non-editable)
    private JPanel createNamePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JLabel namePrefix = SharedGuiComponents.createStyledLabel("Workout Name:");
        workoutNameLabel = SharedGuiComponents.createStyledLabel("");
        workoutNameLabel.setFont(workoutNameLabel.getFont().deriveFont(Font.BOLD));
        
        panel.add(namePrefix);
        panel.add(workoutNameLabel);
        
        return panel;
    }
    
    // HELPER: for layoutComponents
    // EFFECTS: Create the panel with action buttons for saving changes or canceling
    @Override
    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(saveButton);
        panel.add(cancelButton);
        
        return panel;
    }

    // EFFECTS: Initialize the panel with the workout to be edited
    //          Load the workout's exercises into the selectedExercises list
    //          Update the UI to reflect the current workout data
    public void initializeWithWorkout(Workout workout) {
        if (workout == null) {
            return;
        }
        
        this.currentWorkout = workout;
        workoutNameLabel.setText(workout.getName());
        
        // Load the exercises from the workout into the selected list
        loadWorkoutExercises();
    }
    
    // HELPER: for initializeWithWorkout
    // EFFECTS: Load the exercises from the current workout into the selectedExercises list
    private void loadWorkoutExercises() {
        clearSelectedExercises();
        
        if (currentWorkout != null) {
            for (Exercise exercise : currentWorkout.getExercises()) {
                selectedExercises.add(exercise);
                selectedListModel.addElement(exercise.getName());
            }
        }
    }

    // HELPER: for setVisible
    // EFFECTS: Update available exercises when the panel becomes visible
    //          Preserve selected exercises if a workout is loaded
    @Override
    protected void updateExerciseLists() {
        // Only update available exercises, don't touch selected exercises
        // This is critical to preserve the current workout's exercises
        updateAvailableExercises();
    }

    // HELPER: for various error handlers
    // EFFECTS: Navigate back to workout management panel
    @Override
    protected void returnToMainMenu() {
        ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("WorkoutManagement");
    }
    
    // HELPER: for saveButton action
    // MODIFIES: currentWorkout
    // EFFECTS: Update the current workout with the selected exercises
    //          Show success message and navigate back to workout management
    //          Show error message if validation fails
    private void saveWorkoutChanges() {
        if (currentWorkout == null) {
            SharedGuiComponents.showError("No workout is loaded for editing.");
            return;
        }
        
        if (!validateExerciseSelection()) {
            return;
        }
        
        try {
            currentWorkout.setExercises(new ArrayList<>(selectedExercises));
            
            SharedGuiComponents.showInfo("Workout '" + currentWorkout.getName() + "' updated successfully!");
            
            // Navigate back to workout management
            returnToMainMenu();
        } catch (IllegalArgumentException e) {
            SharedGuiComponents.showError("Error updating workout: " + e.getMessage());
        }
    }

    // HELPER: for cancelButton action
    // EFFECTS: Cancel editing and return to workout management without saving changes
    //          Ask for confirmation before discarding changes
    private void cancelEditing() {
        boolean confirmed = SharedGuiComponents.showConfirmation(
                "Are you sure you want to cancel? Any unsaved changes will be lost.");
        
        if (confirmed) {
            returnToMainMenu();
        }
    }
}