package ui.gui.components;

import model.exercise.Exercise;
import model.workout.Workout;
import model.workout.WorkoutPlan;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * This panel manages existing workouts within WorkoutLibrary.
 * It allows users to view workout details, edit workout exercises, and delete workouts.
 */
public class WorkoutManagementPanel extends JPanel {
    
    private JList<String> workoutList;
    private DefaultListModel<String> listModel;
    private JPanel detailsPanel;
    private JButton viewButton;
    private JButton editButton; 
    private JButton deleteButton;
    private JButton backButton;
    
    // EFFECTS: Instantiate this WorkoutManagementPanel with complete UI for workout management
    public WorkoutManagementPanel() {
        setupPanel();
        createComponents();
        layoutComponents();
    }

    // HELPER: for WorkoutManagementPanel
    // EFFECTS: Set up the panel layout and background with BorderLayout for organized section arrangement
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for WorkoutManagementPanel
    // EFFECTS: Create all panel components including workout list, details panel, and buttons
    //          Prepare UI elements for user to select and manage workouts
    private void createComponents() {
        createWorkoutList();
        createDetailsPanel();
        createButtons();
    }

    // HELPER: for createComponents
    // EFFECTS: Create the workout list component with styled appearance and single selection mode
    //          Set up list to display all available workouts from the library
    private void createWorkoutList() {
        listModel = new DefaultListModel<>();
        workoutList = new JList<>(listModel);
        workoutList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        workoutList.setBackground(SharedGuiComponents.SECONDARY_COLOR);
        workoutList.setForeground(SharedGuiComponents.TEXT_COLOR);
        workoutList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    // HELPER: for createComponents
    // EFFECTS: Create the details panel for displaying workout information with placeholder text
    //          Prepare a blank panel that will later show detailed workout properties
    private void createDetailsPanel() {
        detailsPanel = new JPanel();
        detailsPanel.setLayout(new BorderLayout());
        detailsPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Initially show a message
        JLabel placeholderLabel = SharedGuiComponents.createStyledLabel(
                "Select a workout to view details");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(placeholderLabel, BorderLayout.CENTER);
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons for viewing details, editing, deletion, and navigation
    //          Attach event handlers to respond to user clicks
    private void createButtons() {
        viewButton = SharedGuiComponents.createStyledButton("View Details");
        viewButton.addActionListener(e -> viewWorkoutDetails());
        
        editButton = SharedGuiComponents.createStyledButton("Edit Workout");
        editButton.addActionListener(e -> editWorkout());
        
        deleteButton = SharedGuiComponents.createStyledButton("Delete Workout");
        deleteButton.addActionListener(e -> deleteWorkout());
        
        backButton = SharedGuiComponents.createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> 
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu"));
    }

    // HELPER: for WorkoutManagementPanel
    // EFFECTS: Arrange components on the panel in a visually organized layout
    //          Position title at top, workout list to left, details panel in center, and buttons at bottom
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
    // EFFECTS: Create and configure the title label for the panel with proper styling
    //          Return a JLabel with centered heading text
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Manage Workouts");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel containing the workout list with a scrollable view
    //          Return a JPanel with labeled, scrollable list of available workouts
    private JPanel createListPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setPreferredSize(new Dimension(250, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        
        JLabel listLabel = SharedGuiComponents.createStyledLabel("Available Workouts:");
        listLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JScrollPane scrollPane = new JScrollPane(workoutList);
        scrollPane.setPreferredSize(new Dimension(200, 300));
        
        panel.add(listLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel containing action buttons with consistent spacing
    //          Return a JPanel with horizontally arranged action buttons
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(viewButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(backButton);
        
        return panel;
    }

    // EFFECTS: Update the workout list from the library when the panel becomes visible
    //          Refresh display to reflect any changes in WorkoutLibrary
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updateWorkoutList();
        }
        super.setVisible(visible);
    }

    // HELPER: for setVisible
    // EFFECTS: Update the workout list with current workouts from the library
    //          Navigate back to main menu if no workouts are available
    //          Update list display with all available workouts
    private void updateWorkoutList() {
        listModel.clear();
        List<WorkoutPlan> workouts = SharedGuiComponents.workoutLibrary.getAllWorkouts();
        
        if (workouts.isEmpty()) {
            // If no workouts, navigate back to main menu
            SwingUtilities.invokeLater(() -> {
                SharedGuiComponents.showInfo("No workouts available. Please create some workouts first.");
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu");
            });
            return;
        }
        
        for (WorkoutPlan workout : workouts) {
            listModel.addElement(workout.getName());
        }
    }

    // HELPER: for createButtons (viewButton action)
    // EFFECTS: Display details of the selected workout in the details panel
    //          Show error message if no workout is selected
    //          Retrieve and display information about the selected workout
    private void viewWorkoutDetails() {
        String selectedName = workoutList.getSelectedValue();
        if (selectedName == null) {
            SharedGuiComponents.showError("Please select a workout to view.");
            return;
        }
        
        WorkoutPlan workout = SharedGuiComponents.workoutLibrary.getWorkout(selectedName);
        displayWorkoutDetails(workout);
    }

    // HELPER: for createButtons (editButton action)
    // EFFECTS: Open the workout edit panel for the selected workout
    //          Show error message if no workout is selected or if the selected workout is not editable
    private void editWorkout() {
        String selectedName = workoutList.getSelectedValue();
        if (selectedName == null) {
            SharedGuiComponents.showError("Please select a workout to edit.");
            return;
        }
        
        WorkoutPlan workoutPlan = SharedGuiComponents.workoutLibrary.getWorkout(selectedName);
        
        if (workoutPlan instanceof Workout) {
            Workout workout = (Workout) workoutPlan;
            
            WorkoutAppGUI appGUI = (WorkoutAppGUI) SwingUtilities.getWindowAncestor(this);
            WorkoutEditPanel editPanel = appGUI.getWorkoutEditPanel();
            
            // Initialize panel with workout BEFORE navigating
            editPanel.initializeWithWorkout(workout);
            
            // Then navigate to it
            appGUI.navigateTo("WorkoutEdit");
        } else {
            SharedGuiComponents.showError("This workout type cannot be edited.");
        }
    }

    // HELPER: for viewWorkoutDetails
    // EFFECTS: Create and display a panel with detailed information about the selected workout
    //          Update the details panel to show workout-specific information
    private void displayWorkoutDetails(WorkoutPlan workout) {
        detailsPanel.removeAll();
        
        JPanel infoPanel = createWorkoutInfoPanel(workout);
        detailsPanel.add(infoPanel, BorderLayout.CENTER);
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }

    // HELPER: for displayWorkoutDetails
    // EFFECTS: Create a panel with workout information including name, duration, and exercises
    //          Return a JPanel containing all workout properties in a formatted layout
    private JPanel createWorkoutInfoPanel(WorkoutPlan workout) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JPanel headerPanel = createWorkoutHeaderPanel(workout);
        JPanel exercisesPanel = createExercisesPanel(workout);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(exercisesPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for createWorkoutInfoPanel
    // EFFECTS: Create the header panel with basic workout information (name and duration)
    //          Return a panel with the workout's name and total duration information
    private JPanel createWorkoutHeaderPanel(WorkoutPlan workout) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel nameLabel = SharedGuiComponents.createTitleLabel("Workout: " + workout.getName());
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel durationLabel = SharedGuiComponents.createStyledLabel(
                "Total Duration: " + SharedGuiComponents.formatDuration(Math.round(workout.getDuration()), false));
        durationLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(durationLabel);
        
        return panel;
    }

    // HELPER: for createWorkoutInfoPanel
    // EFFECTS: Create the panel displaying the exercises in the workout
    //          Return a panel containing the list of exercises with section header
    private JPanel createExercisesPanel(WorkoutPlan workout) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JLabel exercisesLabel = SharedGuiComponents.createStyledLabel("Exercises:");
        exercisesLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel listPanel = createExerciseListPanel(workout);
        
        panel.add(exercisesLabel, BorderLayout.NORTH);
        panel.add(listPanel, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for createExercisesPanel
    // EFFECTS: Create a panel with the list of exercises in the workout
    //          Display a message if the workout has no exercises
    //          Return a panel displaying all exercises contained in the workout
    private JPanel createExerciseListPanel(WorkoutPlan workout) {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        List<Exercise> exercises = workout.getExercises();
        
        if (exercises.isEmpty()) {
            JLabel emptyLabel = SharedGuiComponents.createStyledLabel("No exercises in this workout.");
            emptyLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            contentPanel.add(emptyLabel);
        } else {
            addExercisesToPanel(contentPanel, exercises);
        }
        
        // Workout Content (listed Exercises) is scrollable
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16); // Faster scrolling
        
        // Container panel to hold the scroll pane
        JPanel containerPanel = new JPanel(new BorderLayout());
        containerPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        containerPanel.add(scrollPane, BorderLayout.CENTER);
        
        return containerPanel;
    }

    // HELPER: for createExerciseListPanel
    // EFFECTS: Add exercise items to the panel with consistent formatting
    //          Display each exercise with index number, name, duration and exercise type
    private void addExercisesToPanel(JPanel panel, List<Exercise> exercises) {
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            
            JPanel exercisePanel = new JPanel(new BorderLayout());
            exercisePanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
            exercisePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            exercisePanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            
            JLabel indexLabel = SharedGuiComponents.createStyledLabel((i + 1) + ".");
            indexLabel.setPreferredSize(new Dimension(25, 20));
            
            // Include formatted Exercise Duration
            String durationText = SharedGuiComponents.formatDuration(Math.round(exercise.getDuration()), true);
            JLabel nameLabel = SharedGuiComponents.createStyledLabel(
                    exercise.getName() + " (" + durationText + ", " + exercise.exerciseType() + ")");
            
            exercisePanel.add(indexLabel, BorderLayout.WEST);
            exercisePanel.add(nameLabel, BorderLayout.CENTER);
            
            panel.add(exercisePanel);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
        }
    }

    // HELPER: for createButtons (deleteButton action)
    // MODIFIES: WorkoutLibrary, WeeklySchedule
    // EFFECTS: Delete the selected workout after user confirmation
    //          Remove the workout from the weekly schedule if it's scheduled
    //          Show error message if no workout is selected
    private void deleteWorkout() {
        String selectedName = workoutList.getSelectedValue();
        if (selectedName == null) {
            SharedGuiComponents.showError("Please select a workout to delete.");
            return;
        }
        
        boolean confirmed = SharedGuiComponents.showConfirmation(
                "Are you sure you want to delete '" + selectedName + "'?");
        
        if (confirmed) {
            removeWorkoutFromSchedule(selectedName);
            SharedGuiComponents.workoutLibrary.removeWorkout(selectedName);
            updateWorkoutList();
            clearDetailsPanel();
            SharedGuiComponents.showInfo("Workout deleted successfully!");
        }
    }

    // HELPER: for deleteWorkout
    // MODIFIES: WeeklySchedule
    // EFFECTS: Remove the workout from the weekly schedule if it's scheduled on any day
    //          Iterate through all days and remove any WeeklySchedule-Workout references 
    //          to the deleted Workout
    private void removeWorkoutFromSchedule(String workoutName) {
        for (int i = 0; i < 7; i++) {
            WorkoutPlan plan = SharedGuiComponents.weeklySchedule.getScheduleForDay(i);
            if (plan != null && plan.getName().equals(workoutName)) {
                SharedGuiComponents.weeklySchedule.clearScheduleForDay(i);
            }
        }
    }

    // HELPER: for deleteWorkout
    // EFFECTS: Clear the details panel and show the default message
    //          Reset the details view to its initial state after workout deletion
    private void clearDetailsPanel() {
        detailsPanel.removeAll();
        
        JLabel placeholderLabel = SharedGuiComponents.createStyledLabel(
                "Select a workout to view details");
        placeholderLabel.setHorizontalAlignment(SwingConstants.CENTER);
        detailsPanel.add(placeholderLabel, BorderLayout.CENTER);
        
        detailsPanel.revalidate();
        detailsPanel.repaint();
    }
}