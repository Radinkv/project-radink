package ui.gui.components;

import model.exercise.Exercise;
import model.workout.Workout;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This panel handles workout creation. It extends the WorkoutModificationPanel to provide
 * specific functionality for creating new workouts with a set of selected exercises.
 */
public class WorkoutCreationPanel extends WorkoutModificationPanel {
    
    private JTextField nameField;
    private JButton createButton;
    private JButton backButton;
    
    // EFFECTS: Instantiate this WorkoutCreationPanel 
    public WorkoutCreationPanel() {
        super();
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons specific to workout creation
    @Override
    protected void createActionButtons() {
        createButton = SharedGuiComponents.createStyledButton("Create Workout");
        createButton.addActionListener(e -> createWorkout());
        
        backButton = SharedGuiComponents.createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> returnToMainMenu());
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the header panel with title and name input field
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
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Create New Workout");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return titleLabel;
    }

    // HELPER: for createHeaderPanel
    // EFFECTS: Create the panel for workout name input with label and text field
    private JPanel createNamePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        nameField = new JTextField(20);
        
        JLabel nameLabel = SharedGuiComponents.createStyledLabel("Workout Name:");
        panel.add(nameLabel);
        panel.add(nameField);
        
        return panel;
    }
    
    // HELPER: for layoutComponents
    // EFFECTS: Create the panel with action buttons for creating a Workout or returning to main menu
    @Override
    protected JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(createButton);
        panel.add(backButton);
        
        return panel;
    }

    // HELPER: for setVisible
    // EFFECTS: Update both exercise lists with current data from ExerciseLibrary
    @Override
    protected void updateExerciseLists() {
        updateAvailableExercises();
        clearSelectedExercises();
    }

    // HELPER: for various error handlers
    // EFFECTS: Navigate back to the main menu
    @Override
    protected void returnToMainMenu() {
        ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu");
    }
    
    // HELPER: for createButton action
    // MODIFIES: WorkoutLibrary
    // EFFECTS: Create a workout with the selected exercises and add it to the workout library
    //          Show success message and navigate to main menu on success
    //          Show error message if validation fails or workout creation throws an exception
    private void createWorkout() {
        String name = nameField.getText().trim();
        
        if (!validateWorkoutName(name)) {
            return;
        }
        
        if (!validateExerciseSelection()) {
            return;
        }
        
        try {
            Workout workout = new Workout(name, new ArrayList<Exercise>(selectedExercises));
            SharedGuiComponents.workoutLibrary.addWorkout(workout);
            
            SharedGuiComponents.showInfo("Workout '" + name + "' created successfully!");
            resetForm();
            nameField.setText(""); 
            
            // Navigate back to main menu
            returnToMainMenu();
        } catch (IllegalArgumentException e) {
            SharedGuiComponents.showError("Error creating workout: " + e.getMessage());
        }
    }

    // HELPER: for createWorkout
    // EFFECTS: Check if the workout name is valid (not empty and not already in use)
    //          Return true if name is valid, false otherwise
    //          Display appropriate error message if name is invalid
    private boolean validateWorkoutName(String name) {
        if (name.isEmpty()) {
            SharedGuiComponents.showError("Workout name cannot be empty.");
            return false;
        }
        
        if (SharedGuiComponents.workoutLibrary.getWorkout(name) != null) {
            SharedGuiComponents.showError("A workout with this name already exists.");
            return false;
        }
        
        return true;
    }
}