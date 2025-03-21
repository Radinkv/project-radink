package ui.gui.components;

import model.exercise.Exercise;
import model.workout.Workout;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This panel handles workout creation. It collects user input to build a workout with a name
 * and a set of selected exercises from the user's self-built ExerciseLibrary.
 */
public class WorkoutCreationPanel extends JPanel {
    
    private JTextField nameField;
    private JList<String> availableExercisesList;
    private JList<String> selectedExercisesList;
    private DefaultListModel<String> availableListModel;
    private DefaultListModel<String> selectedListModel;
    
    private JButton addButton;
    private JButton removeButton;
    private JButton createButton;
    private JButton backButton;

    private JComboBox<String> exerciseTypeFilter;
    
    private List<Exercise> allAvailableExercises;
    private List<Exercise> availableExercises;
    private List<Exercise> selectedExercises;
    
    /**
     * Initialize the workout creation panel.
     */
    public WorkoutCreationPanel() {
        setupPanel();
        initializeLists();
        createComponents();
        layoutComponents();
    }

    // HELPER: for WorkoutCreationPanel
    // EFFECTS: Set up the panel layout and background color with BorderLayout for section organization
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for WorkoutCreationPanel
    // EFFECTS: Initialize the exercise lists to store available, filtered and selected exercises
    //          Create lists for tracking both model objects and display strings
    private void initializeLists() {
        allAvailableExercises = new ArrayList<Exercise>();
        availableExercises = new ArrayList<Exercise>();
        selectedExercises = new ArrayList<Exercise>();
        
        availableListModel = new DefaultListModel<String>();
        selectedListModel = new DefaultListModel<String>();
    }

    // HELPER: for WorkoutCreationPanel
    // EFFECTS: Create all UI components for the panel including name field, lists, and buttons
    //          Set up the input fields required for workout creation
    private void createComponents() {
        nameField = new JTextField(20);
        
        createExerciseLists();
        createButtons();
    }

    // HELPER: for createComponents
    // EFFECTS: Create the exercise list components for available and selected exercises
    //          Set up both list views for exercise selection with proper display models
    private void createExerciseLists() {
        availableExercisesList = createStyledList(availableListModel);
        selectedExercisesList = createStyledList(selectedListModel);
    }

    // HELPER: for createExerciseLists
    // EFFECTS: Create a styled JList with the given model and consistent appearance
    //          Configure list with multiple selection mode and consistent visual styling
    //          Return configured JList component ready for display
    private JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setBackground(SharedGuiComponents.SECONDARY_COLOR);
        list.setForeground(SharedGuiComponents.TEXT_COLOR);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return list;
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons with appropriate listeners for exercise management and navigation
    //          Set up buttons for adding/removing exercises and creating workout
    private void createButtons() {
        addButton = SharedGuiComponents.createStyledButton("Add >");
        addButton.addActionListener(e -> addSelectedExercises());
        
        removeButton = SharedGuiComponents.createStyledButton("< Remove");
        removeButton.addActionListener(e -> removeSelectedExercises());
        
        createButton = SharedGuiComponents.createStyledButton("Create Workout");
        createButton.addActionListener(e -> createWorkout());
        
        backButton = SharedGuiComponents.createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> 
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu"));
    }

    // HELPER: for WorkoutCreationPanel
    // EFFECTS: Arrange components on the panel in a visually organized layout
    //          Position components in logical order for workout creation workflow
    private void layoutComponents() {
        JLabel titleLabel = createTitleLabel();
        JPanel namePanel = createNamePanel();
        JPanel exerciseSelectionPanel = createExerciseSelectionPanel();
        JPanel buttonPanel = createButtonPanel();
        
        add(titleLabel, BorderLayout.NORTH);
        add(namePanel, BorderLayout.NORTH);
        add(exerciseSelectionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create and configure the title label for the panel with proper styling
    //          Return a styled heading label for the panel
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Create New Workout");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        return titleLabel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel for workout name input with label and text field
    //          Return a panel containing the workout name input field
    private JPanel createNamePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JLabel nameLabel = SharedGuiComponents.createStyledLabel("Workout Name:");
        panel.add(nameLabel);
        panel.add(nameField);
        
        return panel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel for Exercise selection with available and selected Exercise lists
    //          Return a panel with exercise selection interface including filter controls and transfer buttons
    private JPanel createExerciseSelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel filterPanel = createFilterPanel();
        JPanel listsPanel = createListsPanel();
        JPanel transferButtonsPanel = createTransferButtonsPanel();
        
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(listsPanel, BorderLayout.CENTER);
        panel.add(transferButtonsPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // HELPER: for createExerciseSelectionPanel
    // EFFECTS: Create the panel containing both Exercise lists side by side
    //          Return a panel with available and selected exercise lists in a grid layout
    private JPanel createListsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JPanel availablePanel = createListWithLabel(
                availableExercisesList, "Available Exercises");
        JPanel selectedPanel = createListWithLabel(
                selectedExercisesList, "Selected Exercises");
        
        panel.add(availablePanel);
        panel.add(selectedPanel);
        
        return panel;
    }

    // HELPER: for createListsPanel
    // EFFECTS: Create a panel with a label and a scrollable list for displaying Exercise objects
    //          Return a labeled, scrollable panel containing the specified list
    private JPanel createListWithLabel(JList<String> list, String labelText) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        JLabel label = SharedGuiComponents.createStyledLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(200, 250));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // HELPER: for createExerciseSelectionPanel
    // EFFECTS: Create the panel with transfer buttons for moving Exercise objects between lists
    //          Return a panel with add/remove buttons for transferring exercises
    private JPanel createTransferButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(addButton);
        panel.add(removeButton);
        
        return panel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel with action buttons for creating a Workout or returning to main menu
    //          Return a panel with the primary action buttons
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(createButton);
        panel.add(backButton);
        
        return panel;
    }

    // HELPER: for createExerciseSelectionPanel
    // EFFECTS: Create a panel with a filter dropdown to allow filtering exercises by type
    //          Return a panel containing the filter controls with consistent styling
    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        String[] filterOptions = {"All Exercises", "Strength Exercise", "Endurance Exercise", "Interval Exercise"};
        exerciseTypeFilter = new JComboBox<>(filterOptions);
        exerciseTypeFilter.setSelectedIndex(0);
        exerciseTypeFilter.addActionListener(e -> filterExercisesByType());
        
        panel.add(SharedGuiComponents.createStyledLabel("Filter by type:"));
        panel.add(exerciseTypeFilter);
        
        return panel;
    }

    // HELPER: for exerciseTypeFilter's ActionListener and updateAvailableExercises
    // EFFECTS: Filter the available exercises list based on the selected exercise type
    //          Populate availableExercises and availableListModel with exercises matching the filter
    //          If "All Exercises" is selected, include all exercises from allAvailableExercises
    private void filterExercisesByType() {
        String selectedFilter = (String) exerciseTypeFilter.getSelectedItem();
        
        availableListModel.clear();
        availableExercises.clear();
        
        if (selectedFilter.equals("All Exercises")) {
            addAllExercisesToAvailable();
        } else {
            addExercisesByTypeToAvailable(selectedFilter.split(" ")[0]);
        }
    }

    // HELPER: for filterExercisesByType
    // EFFECTS: Add all exercises from allAvailableExercises to the available exercises list
    //          Populate both availableExercises collection and availableListModel
    private void addAllExercisesToAvailable() {
        for (Exercise exercise : allAvailableExercises) {
            availableExercises.add(exercise);
            availableListModel.addElement(exercise.getName());
        }
    }

    // HELPER: for filterExercisesByType
    // EFFECTS: Add only exercises of the specified type to the available exercises list
    //          Filter exercises from allAvailableExercises based on their exercise type
    //          Populate both availableExercises collection and availableListModel with matching exercises
    private void addExercisesByTypeToAvailable(String exerciseType) {
        for (Exercise exercise : allAvailableExercises) {
            if (exercise.exerciseType().equals(exerciseType)) {
                availableExercises.add(exercise);
                availableListModel.addElement(exercise.getName());
            }
        }
    }    

    // EFFECTS: Update the Exercise list when the panel becomes visible
    //          Load available exercises from ExerciseLibrary when panel is displayed
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updateExerciseLists();
        }
        super.setVisible(visible);
    }

    // HELPER: for setVisible
    // EFFECTS: Update both exercise lists with current data from ExerciseLibrary
    //          Refresh available exercises and clear selected exercises
    private void updateExerciseLists() {
        updateAvailableExercises();
        clearSelectedExercises();
    }

    // HELPER: for updateExerciseLists
    // EFFECTS: Update the available exercises list with references to exercises from the library
    //          Store all exercises in allAvailableExercises and apply the current filter
    //          Navigate back to main menu if no exercises are available
    private void updateAvailableExercises() {
        allAvailableExercises.clear();
        availableListModel.clear();
        availableExercises.clear();
        
        Map<String, Exercise> exercises = SharedGuiComponents.exerciseLibrary.getAllExercises();
        if (exercises.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                SharedGuiComponents.showError("No exercises available. Please create some exercises first.");
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu");
            });
            return;
        }
        
        for (Exercise exercise : exercises.values()) {
            allAvailableExercises.add(exercise);
        }

        filterExercisesByType();
    }

    // HELPER: for updateExerciseLists
    // EFFECTS: Clear the selected exercises list and selected exercises collection
    //          Reset the workout exercise selection to empty
    private void clearSelectedExercises() {
        selectedListModel.clear();
        selectedExercises.clear();
    }

    // HELPER: for createButtons (addButton action)
    // EFFECTS: Add exercises selected in the available list to the selected list
    //          Only add exercises that aren't already in the selected list
    //          Transferred exercises from available to selected list remain in available panel
    private void addSelectedExercises() {
        int[] selectedIndices = availableExercisesList.getSelectedIndices();
        
        for (int index : selectedIndices) {
            if (index >= 0 && index < availableExercises.size()) {
                Exercise exercise = availableExercises.get(index);
                
                // Can be done by object reference (no overridden equals) because no two Exercises have the same name
                if (!selectedExercises.contains(exercise)) {
                    selectedExercises.add(exercise);
                    selectedListModel.addElement(exercise.getName());
                }
            }
        }
        // filterExercisesByType();
    }

    // HELPER: for createButtons (removeButton action)
    // EFFECTS: Remove exercises selected in the selected list
    //          Remove exercises from the workout in reverse index order to prevent index shifting issues
    private void removeSelectedExercises() {
        int[] selectedIndices = selectedExercisesList.getSelectedIndices();
        
        // Remove elements in reverse order for index shifting prevention
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            int index = selectedIndices[i];
            if (index >= 0 && index < selectedExercises.size()) {
                selectedExercises.remove(index);
                selectedListModel.remove(index);
            }
        }
        // filterExercisesByType();
    }
    
    // HELPER: for createButtons (createButton action)
    // MODIFIES: WorkoutLibrary
    // EFFECTS: Create a workout with the selected exercises and add it to the workout library
    //          Show success message and navigate to main menu on success
    //          Show error message if validation fails or workout creation throws an exception
    private void createWorkout() {
        String name = nameField.getText().trim();
        
        if (!validateWorkoutName(name)) {
            return;
        }
        
        if (selectedExercises.isEmpty()) {
            SharedGuiComponents.showError("Please select at least one exercise for the workout.");
            return;
        }
        
        try {
            Workout workout = new Workout(name, selectedExercises);
            SharedGuiComponents.workoutLibrary.addWorkout(workout);
            
            SharedGuiComponents.showInfo("Workout '" + name + "' created successfully!");
            resetForm();
            
            // Navigate back to main menu
            ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu");
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

    // HELPER: for createWorkout
    // EFFECTS: Reset the form fields after successful workout creation
    //          Clear the input field and selected exercises list
    private void resetForm() {
        nameField.setText("");
        clearSelectedExercises();
    }
}