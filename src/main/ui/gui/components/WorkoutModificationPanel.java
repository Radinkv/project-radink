package ui.gui.components;

import model.exercise.Exercise;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This panel manages exercise selection for workout creation or editing.
 * It provides a reusable interface for manipulating the exercises in a workout,
 * whether with creating a new workout or modifying an existing one.
 */
public abstract class WorkoutModificationPanel extends JPanel {
    
    protected JList<String> availableExercisesList;
    protected JList<String> selectedExercisesList;
    protected DefaultListModel<String> availableListModel;
    protected DefaultListModel<String> selectedListModel;
    
    protected JButton addButton;
    protected JButton removeButton;
    
    protected JComboBox<String> exerciseTypeFilter;
    
    protected List<Exercise> allAvailableExercises;
    protected List<Exercise> availableExercises;
    protected List<Exercise> selectedExercises;
    
    // EFFECTS: Initialize this WorkoutModificationPanel with all needed components
    //          Set up the UI framework that is common to both creation and editing workflows
    public WorkoutModificationPanel() {
        setupPanel();
        initializeLists();
        createComponents();
        layoutComponents();
    }

    // HELPER: for WorkoutModificationPanel
    // EFFECTS: Set up the panel layout and background color
    protected void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for WorkoutModificationPanel
    // EFFECTS: Initialize the exercise lists for available and selected exercises
    protected void initializeLists() {
        allAvailableExercises = new ArrayList<Exercise>();
        availableExercises = new ArrayList<Exercise>();
        selectedExercises = new ArrayList<Exercise>();
        
        availableListModel = new DefaultListModel<String>();
        selectedListModel = new DefaultListModel<String>();
    }

    // HELPER: for WorkoutModificationPanel
    // EFFECTS: Create all UI components for the panel
    protected void createComponents() {
        createExerciseLists();
        createTransferButtons();
        createActionButtons();
    }
    
    // HELPER: for createComponents
    // EFFECTS: Create the exercise list components for available and selected exercises
    protected void createExerciseLists() {
        availableExercisesList = createStyledList(availableListModel);
        selectedExercisesList = createStyledList(selectedListModel);
    }

    // HELPER: for createExerciseLists
    // EFFECTS: Create a styled JList with the given model and consistent appearance
    protected JList<String> createStyledList(DefaultListModel<String> model) {
        JList<String> list = new JList<>(model);
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        list.setBackground(SharedGuiComponents.SECONDARY_COLOR);
        list.setForeground(SharedGuiComponents.TEXT_COLOR);
        list.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return list;
    }

    // HELPER: for createComponents
    // EFFECTS: Create transfer buttons (add/remove) with appropriate listeners
    protected void createTransferButtons() {
        addButton = SharedGuiComponents.createStyledButton("Add >");
        addButton.addActionListener(e -> addSelectedExercises());
        
        removeButton = SharedGuiComponents.createStyledButton("< Remove");
        removeButton.addActionListener(e -> removeSelectedExercises());
    }

    // HELPER: for createComponents
    // EFFECTS: Create action buttons specific to the implementation (save/cancel)
    protected abstract void createActionButtons();

    // HELPER: for WorkoutModificationPanel
    // EFFECTS: Arrange components on the panel in a visually organized layout
    protected void layoutComponents() {
        JPanel headerPanel = createHeaderPanel();
        JPanel exerciseSelectionPanel = createExerciseSelectionPanel();
        JPanel buttonPanel = createButtonPanel();
        
        add(headerPanel, BorderLayout.NORTH);
        add(exerciseSelectionPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the header panel with title and any required input fields
    protected abstract JPanel createHeaderPanel();

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel for Exercise selection with available and selected Exercise lists
    protected JPanel createExerciseSelectionPanel() {
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
    // EFFECTS: Create a panel with a filter dropdown to allow filtering exercises by type
    protected JPanel createFilterPanel() {
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
    protected void filterExercisesByType() {
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
    protected void addAllExercisesToAvailable() {
        for (Exercise exercise : allAvailableExercises) {
            availableExercises.add(exercise);
            availableListModel.addElement(exercise.getName());
        }
    }

    // HELPER: for filterExercisesByType
    // EFFECTS: Add only exercises of the specified type to the available exercises list
    protected void addExercisesByTypeToAvailable(String exerciseType) {
        for (Exercise exercise : allAvailableExercises) {
            if (exercise.exerciseType().equals(exerciseType)) {
                availableExercises.add(exercise);
                availableListModel.addElement(exercise.getName());
            }
        }
    }

    // HELPER: for createExerciseSelectionPanel
    // EFFECTS: Create the panel containing both Exercise lists side by side
    protected JPanel createListsPanel() {
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
    protected JPanel createListWithLabel(JList<String> list, String labelText) {
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
    protected JPanel createTransferButtonsPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(addButton);
        panel.add(removeButton);
        
        return panel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create the panel with action buttons for saving or canceling
    protected abstract JPanel createButtonPanel();

    // EFFECTS: Update the Exercise list when the panel becomes visible
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            updateExerciseLists();
        }
        super.setVisible(visible);
    }

    // HELPER: for setVisible
    // EFFECTS: Update both exercise lists with current data
    protected abstract void updateExerciseLists();

    // HELPER: for updateExerciseLists
    // EFFECTS: Update the available exercises list with references to exercises from the library
    protected void updateAvailableExercises() {
        allAvailableExercises.clear();
        availableListModel.clear();
        availableExercises.clear();
        
        Map<String, Exercise> exercises = SharedGuiComponents.exerciseLibrary.getAllExercises();
        if (exercises.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                SharedGuiComponents.showError("No exercises available. Please create some exercises first.");
                returnToMainMenu();
            });
            return;
        }
        
        for (Exercise exercise : exercises.values()) {
            allAvailableExercises.add(exercise);
        }

        filterExercisesByType();
    }

    // HELPER: for various error handlers
    // EFFECTS: Navigate back to the main menu
    protected abstract void returnToMainMenu();

    // HELPER: for updateExerciseLists
    // EFFECTS: Clear the selected exercises list
    protected void clearSelectedExercises() {
        selectedListModel.clear();
        selectedExercises.clear();
    }

    // HELPER: for addButton action
    // EFFECTS: Add exercises selected in the available list to the selected list
    protected void addSelectedExercises() {
        int[] selectedIndices = availableExercisesList.getSelectedIndices();
        
        for (int index : selectedIndices) {
            if (index >= 0 && index < availableExercises.size()) {
                Exercise exercise = availableExercises.get(index);
                
                if (!selectedExercises.contains(exercise)) {
                    selectedExercises.add(exercise);
                    selectedListModel.addElement(exercise.getName());
                }
            }
        }
    }

    // HELPER: for removeButton action
    // EFFECTS: Remove exercises selected in the selected list
    protected void removeSelectedExercises() {
        int[] selectedIndices = selectedExercisesList.getSelectedIndices();
        
        // Remove elements in reverse order for index shifting prevention
        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            int index = selectedIndices[i];
            if (index >= 0 && index < selectedExercises.size()) {
                selectedExercises.remove(index);
                selectedListModel.remove(index);
            }
        }
    }
    
    // HELPER: for save operations
    // EFFECTS: Reset the form fields after successful operation
    protected void resetForm() {
        clearSelectedExercises();
    }
    
    // HELPER: for save operations
    // EFFECTS: Check if the list of selected exercises is valid (not empty)
    protected boolean validateExerciseSelection() {
        if (selectedExercises.isEmpty()) {
            SharedGuiComponents.showError("Please select at least one exercise for the workout.");
            return false;
        }
        return true;
    }
}