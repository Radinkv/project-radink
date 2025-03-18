package ui.gui.components;

import model.exercise.Exercise;
import model.workout.Workout;
import model.workout.WorkoutPlan;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * This panel manages Exercise viewing, details display, and deletion based on user choice.
 */
public class ExerciseManagementPanel extends JPanel {
    
    private JList<String> exerciseList;
    private DefaultListModel<String> listModel;
    private JPanel detailsPanel;
    private JButton viewButton;
    private JButton deleteButton;
    private JButton backButton;
    
    // EFFECTS: Instantiate this ExerciseManagementPanel.
    public ExerciseManagementPanel() {
        setupPanel();
        createComponents();
        layoutComponents();
    }

    // HELPER: for ExerciseManagementPanel
    // EFFECTS: Set up the panel layout and background
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for ExerciseManagementPanel
    // EFFECTS: Create all panel components including exercise list, details panel, and buttons
    private void createComponents() {
        createExerciseList();
        createDetailsPanel();
        createButtons();
    }

    // HELPER: for createComponents
    // EFFECTS: Create the exercise list component with styled appearance
    private void createExerciseList() {
        listModel = new DefaultListModel<>();
        exerciseList = new JList<>(listModel);
        exerciseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        exerciseList.setBackground(SharedGuiComponents.SECONDARY_COLOR);
        exerciseList.setForeground(SharedGuiComponents.TEXT_COLOR);
        exerciseList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    // HELPER: for createComponents
    // EFFECTS: Create the details panel for displaying exercise information
    //          Initially show a placeholder message
    private void createDetailsPanel() {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initially show a message
        JLabel placeholderLabel = SharedGuiComponents.createStyledLabel(
                "Select an exercise to view details");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(placeholderLabel, BorderLayout.CENTER);
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons for viewing details, deletion, and navigation
    //          Add action listeners to handle button clicks
    private void createButtons() {
        viewButton = SharedGuiComponents.createStyledButton("View Details");
        viewButton.addActionListener(e -> viewExerciseDetails());
        
        deleteButton = SharedGuiComponents.createStyledButton("Delete Exercise");
        deleteButton.addActionListener(e -> deleteExercise());
        
        backButton = SharedGuiComponents.createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> 
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu"));
    }

    // HELPER: for ExerciseManagementPanel
    // EFFECTS: Arrange components on the panel in a visually organized layout
    private void layoutComponents() {
        JLabel titleLabel = createTitleLabel();
        JPanel listPanel = createListPanel();
        JPanel buttonPanel = createButtonPanel();
        
        add(titleLabel, BorderLayout.NORTH);
        add(listPanel, BorderLayout.WEST);
        add(detailsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create and configure the title label for the panel
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Manage Exercises");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel containing the exercise list with a scrollable view
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel listLabel = SharedGuiComponents.createStyledLabel("Available Exercises:");
        listLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JScrollPane scrollPane = new JScrollPane(exerciseList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        
        panel.add(listLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel containing action buttons with consistent spacing
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(viewButton);
        panel.add(deleteButton);
        panel.add(backButton);
        
        return panel;
    }

    // EFFECTS: Update the exercise list from the library when the panel becomes visible
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updateExerciseList();
        }
        super.setVisible(visible);
    }

    // HELPER: for setVisible
    // EFFECTS: Update the exercise list with current exercises from the library
    private void updateExerciseList() {
        listModel.clear();
        Map<String, Exercise> exercises = SharedGuiComponents.exerciseLibrary.getAllExercises();
        
        for (String name : exercises.keySet()) {
            listModel.addElement(name);
        }
    }

    // HELPER: for createButtons (viewButton action)
    // EFFECTS: Display details of the selected exercise in the details panel
    //          Show error message if no exercise is selected
    private void viewExerciseDetails() {
        String selectedName = exerciseList.getSelectedValue();
        if (selectedName == null) {
            SharedGuiComponents.showError("Please select an exercise to view.");
            return;
        }
        
        Exercise exercise = SharedGuiComponents.exerciseLibrary.getExercise(selectedName);
        displayExerciseDetails(exercise);
    }

    // HELPER: for viewExerciseDetails
    // EFFECTS: Create and display a panel with detailed information about the selected exercise
    private void displayExerciseDetails(Exercise exercise) {
        detailsPanel.removeAll();
        
        JPanel infoPanel = createExerciseInfoPanel(exercise);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    // HELPER: for displayExerciseDetails
    // EFFECTS: Create a panel with exercise information including type-specific details
    private JPanel createExerciseInfoPanel(Exercise exercise) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        addBasicExerciseInfo(panel, exercise);
        addExerciseTypeSpecificInfo(panel, exercise);
        
        return panel;
    }

    // HELPER: for createExerciseInfoPanel
    // EFFECTS: Add basic exercise information (name and type) to the panel
    private void addBasicExerciseInfo(JPanel panel, Exercise exercise) {
        JLabel nameLabel = SharedGuiComponents.createTitleLabel("Exercise: " + exercise.getName());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel typeLabel = SharedGuiComponents.createStyledLabel("Type: " + exercise.exerciseType());
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(typeLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
    }

    // HELPER: for createExerciseInfoPanel
    // EFFECTS: Add exercise type-specific information to the panel
    //          Display different information based on exercise type (strength, interval, endurance)
    private void addExerciseTypeSpecificInfo(JPanel panel, Exercise exercise) {
        Map<String, Double> info = exercise.getInfo();
        
        if (info.isEmpty()) {
            panel.add(SharedGuiComponents.createStyledLabel("No additional information available."));
            return;
        }
        
        // Display total duration first
        if (info.containsKey("totalDuration")) {
            addInfoRow(panel, "Total Duration", 
                    SharedGuiComponents.formatDuration(Math.round(info.get("totalDuration"))));
        }
        
        if (info.containsKey("sets") && info.containsKey("reps")) {
            addStrengthInfo(panel, info);
        } else if (info.containsKey("timeOn")) {
            addIntervalInfo(panel, info);
        } else if (info.containsKey("duration")) {
            addEnduranceInfo(panel, info);
        }
    }

    // HELPER: for addExerciseTypeSpecificInfo
    // EFFECTS: Add strength exercise specific information to the panel
    private void addStrengthInfo(JPanel panel, Map<String, Double> info) {
        JLabel headerLabel = SharedGuiComponents.createStyledLabel("Training Details:");
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addInfoRow(panel, "Sets", String.format("%.0f", info.get("sets")));
        addInfoRow(panel, "Reps", String.format("%.0f", info.get("reps")));
        
        if (info.containsKey("timePerRep")) {
            addInfoRow(panel, "Time Per Rep", 
                    String.format("%.1f seconds", info.get("timePerRep")));
        }
        
        if (info.containsKey("restTime")) {
            addInfoRow(panel, "Rest Time (Between Each Set)", 
                    String.format("%.1f minutes", info.get("restTime")));
        }
    }

    // HELPER: for addExerciseTypeSpecificInfo
    // EFFECTS: Add interval exercise specific information to the panel
    private void addIntervalInfo(JPanel panel, Map<String, Double> info) {
        JLabel headerLabel = SharedGuiComponents.createStyledLabel("Interval Details:");
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addInfoRow(panel, "Active Time", 
                String.format("%.1f seconds", info.get("timeOn")));
        addInfoRow(panel, "Rest Time (Between Each Active Portion)", 
                String.format("%.1f seconds", info.get("timeOff")));
        addInfoRow(panel, "Repetitions", 
                String.format("%.0f", info.get("repititions")));
    }

    // HELPER: for addExerciseTypeSpecificInfo
    // EFFECTS: Add endurance exercise specific information to the panel
    private void addEnduranceInfo(JPanel panel, Map<String, Double> info) {
        JLabel headerLabel = SharedGuiComponents.createStyledLabel("Endurance Details:");
        headerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(headerLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        addInfoRow(panel, "Duration", 
                String.format("%.1f minutes", info.get("duration")));
    }

    // HELPER: for addStrengthInfo, addIntervalInfo, addEnduranceInfo
    // EFFECTS: Add a row with label and value to the panel with consistent formatting
    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        rowPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel labelComponent = SharedGuiComponents.createStyledLabel(label + ":");
        JLabel valueComponent = SharedGuiComponents.createStyledLabel(value);
        
        rowPanel.add(labelComponent, BorderLayout.WEST);
        rowPanel.add(valueComponent, BorderLayout.EAST);
        
        panel.add(rowPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    // HELPER: for createButtons (deleteButton action)
    // MODIFIES: ExerciseLibrary and potentially Workout instances
    // EFFECTS: Delete the selected exercise after user confirmation
    //          Remove the exercise from any workouts that contain it
    //          Show error message if no exercise is selected
    private void deleteExercise() {
        String selectedName = exerciseList.getSelectedValue();
        if (selectedName == null) {
            SharedGuiComponents.showError("Please select an exercise to delete.");
            return;
        }
        
        boolean confirmed = SharedGuiComponents.showConfirmation(
                "Are you sure you want to delete '" + selectedName + "'?");
        
        if (confirmed) {
            removeExerciseFromWorkouts(selectedName);
            SharedGuiComponents.exerciseLibrary.removeExercise(selectedName);
            updateExerciseList();
            clearDetailsPanel();
            SharedGuiComponents.showInfo("Exercise deleted successfully!");
        }
    }

    // HELPER: for deleteExercise
    // MODIFIES: Workout instances
    // EFFECTS: Remove the exercise from all workouts that contain it
    private void removeExerciseFromWorkouts(String exerciseName) {
        List<WorkoutPlan> workouts = SharedGuiComponents.workoutLibrary.getAllWorkouts();
        
        for (WorkoutPlan workoutPlan : workouts) {
            if (workoutPlan instanceof Workout) {
                ((Workout) workoutPlan).removeExercise(exerciseName);
            }
        }
    }

    // HELPER: for deleteExercise
    // EFFECTS: Clear the details panel and show the default message
    private void clearDetailsPanel() {
        detailsPanel.removeAll();
        
        JLabel placeholderLabel = SharedGuiComponents.createStyledLabel(
                "Select an exercise to view details");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
}