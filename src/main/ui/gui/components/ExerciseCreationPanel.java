package ui.gui.components;

import model.equipment.Equipment;
import model.exercise.EnduranceExercise;
import model.exercise.Exercise;
import model.exercise.IntervalExercise;
import model.exercise.StrengthExercise;
import model.muscle.MuscleGroup;
import ui.gui.WorkoutAppGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * This panel handles exercise creation. It collects user input to create strength, 
 * endurance, or interval exercises with their associated equipment and muscle groups.
 */
public class ExerciseCreationPanel extends JPanel {
    
    private JTextField nameField;
    private JComboBox<String> exerciseTypeCombo;
    private JPanel exerciseDetailsPanel;
    private JPanel strengthPanel;
    private JPanel endurancePanel;
    private JPanel intervalPanel;
    private JComboBox<String> equipmentCombo;
    private JComboBox<String> muscleGroupCombo;
    
    private JSpinner setsSpinner;
    private JSpinner repsSpinner;
    private JSpinner secondsPerRepSpinner;
    private JSpinner restTimeSpinner;
    
    private JSpinner durationSpinner;
    
    private JSpinner timeOnSpinner;
    private JSpinner timeOffSpinner;
    private JSpinner repetitionsSpinner;
    
    private JButton createButton;
    private JButton backButton;
    
    private CardLayout cardLayout;
    
    // EFFECTS: Instantiate this ExerciseCreationPanel with input fields for different exercise types
    public ExerciseCreationPanel() {
        setupPanel();
        setupComponents();
        layoutComponents();
    }

    // HELPER: for ExerciseCreationPanel
    // EFFECTS: Set up the panel with BorderLayout and background color
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for ExerciseCreationPanel
    // EFFECTS: Initialize all UI components including fields, combo boxes, and buttons
    //          Set up listeners for dynamic UI behavior based on user selections
    private void setupComponents() {
        initializeBasicControls();
        setupComboBoxes();
        
        createExerciseDetailsPanels();
        setupTypeChangeListener();
        
        createActionButtons();
    }
    
    // HELPER: for setupComponents
    // EFFECTS: Create and initialize the name field and exercise type combo box
    //          Prepare the basic input controls for exercise creation
    private void initializeBasicControls() {
        nameField = new JTextField(20);
        
        String[] exerciseTypes = {"Strength Exercise", "Endurance Exercise", "Interval Exercise"};
        exerciseTypeCombo = new JComboBox<>(exerciseTypes);
    }
    
    // HELPER: for setupComponents
    // EFFECTS: Create action buttons with appropriate listeners for form submission and navigation
    //          Attach event handlers to respond to user clicks
    private void createActionButtons() {
        createButton = SharedGuiComponents.createStyledButton("Create Exercise");
        createButton.addActionListener(e -> createExercise());
        
        backButton = SharedGuiComponents.createStyledButton("Back to Main Menu");
        backButton.addActionListener(e -> 
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo("MainMenu"));
    }

    // HELPER: for setupComponents
    // EFFECTS: Populate Equipment and MuscleGroup combo boxes using PredefinedData
    //          Prepare selection controls for user to associate exercises with equipment and muscles
    private void setupComboBoxes() {
        equipmentCombo = createComboBox(SharedGuiComponents.predefinedData.getAllEquipment());
        muscleGroupCombo = createComboBox(SharedGuiComponents.predefinedData.getAllMuscleGroups());
    }


    // HELPER: for setupComboBoxes
    // EFFECTS: Initialize a JComboBox with names (Strings) from the given Map's keys
    //          Return the created combobox populated with appropriate values
    private JComboBox<String> createComboBox(Map<String, ?> dataMap) {
        return new JComboBox<>(dataMap.keySet().toArray(new String[0]));
    }


    // HELPER: for setupComponents
    // EFFECTS: Create separate panels for each exercise type and add them to a card layout
    //          Set the default visible panel to a StrengthExercise panel
    private void createExerciseDetailsPanels() {
        cardLayout = new CardLayout();
        exerciseDetailsPanel = new JPanel(cardLayout);
        exerciseDetailsPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        createStrengthPanel();
        createEndurancePanel();
        createIntervalPanel();
        
        exerciseDetailsPanel.add(strengthPanel, "Strength");
        exerciseDetailsPanel.add(endurancePanel, "Endurance");
        exerciseDetailsPanel.add(intervalPanel, "Interval");
        
        cardLayout.show(exerciseDetailsPanel, "Strength");
    }

    // HELPER: for setupComponents
    // EFFECTS: Add a listener to the exercise type combo box that shows the appropriate
    //          detail panel when a different exercise type is selected
    private void setupTypeChangeListener() {
        exerciseTypeCombo.addActionListener(e -> {
            String selected = (String) exerciseTypeCombo.getSelectedItem();
            updateExerciseTypePanel(selected);
        });
    }

    // HELPER: for setupTypeChangeListener
    // EFFECTS: Show the appropriate detail panel based on the selected exercise type
    //          Switch between different input forms for different exercise types
    //          Do nothing if the selected type is not recognized
    private void updateExerciseTypePanel(String selected) {
        if (selected.equals("Strength Exercise")) {
            cardLayout.show(exerciseDetailsPanel, "Strength");
        } else if (selected.equals("Endurance Exercise")) {
            cardLayout.show(exerciseDetailsPanel, "Endurance");
        } else if (selected.equals("Interval Exercise")) {
            cardLayout.show(exerciseDetailsPanel, "Interval");
        }
    }

    // HELPER: for ExerciseCreationPanel constructor
    // EFFECTS: Arrange all components on this panel with title at the top, 
    //          form in the center, and buttons at the bottom
    private void layoutComponents() {
        JLabel titleLabel = createTitleLabel();
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        
        add(titleLabel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create a styled title label with appropriate spacing and alignment
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Create New Exercise");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create a form panel that contains all input fields arranged in a grid
    //          Include fields for shared Exercise properties with a section for Exercise type-specific fields
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        GridBagConstraints gbc = createStandardGridBagConstraints();
        
        addLabelAndComponent(panel, "Exercise Name:", nameField, gbc, 0);
        addLabelAndComponent(panel, "Exercise Type:", exerciseTypeCombo, gbc, 1);
        addLabelAndComponent(panel, "Equipment:", equipmentCombo, gbc, 2);
        addLabelAndComponent(panel, "Muscle Group:", muscleGroupCombo, gbc, 3);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(exerciseDetailsPanel, gbc);
        
        return panel;
    }

    // HELPER: for createFormPanel
    // EFFECTS: Create standard grid constraints for consistent spacing and fill behavior
    //          Return configured GridBagConstraints object ready for layout
    private GridBagConstraints createStandardGridBagConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        return gbc;
    }

    // HELPER: for createFormPanel
    // EFFECTS: Add a label and its associated input component to the panel at the specified row
    //          The label appears in first column, input component in second screen column
    private void addLabelAndComponent(JPanel panel, String labelText, JComponent component, 
                                     GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(SharedGuiComponents.createStyledLabel(labelText), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        panel.add(component, gbc);
    }

    // HELPER: for createExerciseDetailsPanels
    // EFFECTS: Create a panel with input fields specific to StrengthExercises
    //          Include fields for sets, reps, seconds per rep, and rest time
    private void createStrengthPanel() {
        strengthPanel = new JPanel(new GridBagLayout());
        strengthPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        GridBagConstraints gbc = createStandardGridBagConstraints();
        
        setsSpinner = createNumberSpinner(3, 1, 100, 1);
        repsSpinner = createNumberSpinner(10, 1, 100, 1);
        secondsPerRepSpinner = createDecimalSpinner(3.0, 0.1, 60.0, 0.1);
        restTimeSpinner = createDecimalSpinner(1.0, 0.1, 10.0, 0.1);
        
        addLabelAndComponent(strengthPanel, "Sets:", setsSpinner, gbc, 0);
        addLabelAndComponent(strengthPanel, "Reps:", repsSpinner, gbc, 1);
        addLabelAndComponent(strengthPanel, "Seconds per Rep:", secondsPerRepSpinner, gbc, 2);
        addLabelAndComponent(strengthPanel, "Rest Time (minutes):", restTimeSpinner, gbc, 3);
    }

    // HELPER: for createExerciseDetailsPanels
    // EFFECTS: Create a panel with input fields specific to EnduranceExercises
    //          Include a field for duration (in minutes)
    private void createEndurancePanel() {
        endurancePanel = new JPanel(new GridBagLayout());
        endurancePanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        GridBagConstraints gbc = createStandardGridBagConstraints();
        
        durationSpinner = createDecimalSpinner(20.0, 0.1, 180.0, 0.1);
        
        addLabelAndComponent(endurancePanel, "Duration (minutes):", durationSpinner, gbc, 0);
    }

    // HELPER: for createExerciseDetailsPanels
    // EFFECTS: Create a panel with input fields specific to IntervalExercises
    //          Include fields for active time, rest time, and number of repetitions
    private void createIntervalPanel() {
        intervalPanel = new JPanel(new GridBagLayout());
        intervalPanel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        GridBagConstraints gbc = createStandardGridBagConstraints();
        
        timeOnSpinner = createDecimalSpinner(30.0, 0.1, 300.0, 0.1);
        timeOffSpinner = createDecimalSpinner(15.0, 0.1, 300.0, 0.1);
        repetitionsSpinner = createNumberSpinner(10, 1, 100, 1);
        
        addLabelAndComponent(intervalPanel, "Active Time (seconds):", timeOnSpinner, gbc, 0);
        addLabelAndComponent(intervalPanel, "Rest Time (seconds):", timeOffSpinner, gbc, 1);
        addLabelAndComponent(intervalPanel, "Repetitions:", repetitionsSpinner, gbc, 2);
    }

    // HELPER: for createStrengthPanel, createIntervalPanel
    // EFFECTS: Create a spinner for integer values with specified minimum, maximum, and step
    //          Return a JSpinner configured with appropriate number model and constraints
    private JSpinner createNumberSpinner(int value, int min, int max, int step) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
        return new JSpinner(model);
    }

    // HELPER: for createStrengthPanel, createEndurancePanel, createIntervalPanel
    // EFFECTS: Create a spinner for decimal values with specified minimum, maximum, and step
    //          Return a JSpinner configured with appropriate floating-point model and constraints
    private JSpinner createDecimalSpinner(double value, double min, double max, double step) {
        SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
        return new JSpinner(model);
    }

    // HELPER: for layoutComponents
    // EFFECTS: Create a panel containing 'create' and 'back' buttons, arranged horizontally with consistent spacing
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        panel.add(createButton);
        panel.add(backButton);
        
        return panel;
    }

    // HELPER: for createButton's ActionListener
    // MODIFIES: ExerciseLibrary
    // EFFECTS: Ensure valid user input and create an exercise based on user input values
    //          Add the created exercise to ExerciseLibrary if validation succeeds
    //          Show success message and reset the form if an Exercise is created successfully
    //          Show error message if validation fails or Exercise creation throws an exception
    private void createExercise() {
        String name = nameField.getText().trim();
        
        if (!validateExerciseName(name)) {
            return;
        }
        
        try {
            Exercise exercise = createExerciseByType(name);
            if (exercise != null) {
                SharedGuiComponents.exerciseLibrary.addExercise(exercise);
                SharedGuiComponents.showInfo("Exercise '" + name + "' created successfully!");
                resetForm();
            }
        } catch (IllegalArgumentException e) {
            SharedGuiComponents.showError("Invalid input: " + e.getMessage());
        }
    }

    // HELPER: for createExercise
    // EFFECTS: Check if the exercise name is valid (not empty and not already in use)
    //          Show an error message and return false if name is invalid
    //          Return true if name is valid
    private boolean validateExerciseName(String name) {
        if (name.isEmpty()) {
            SharedGuiComponents.showError("Exercise name cannot be empty.");
            return false;
        }
        
        if (SharedGuiComponents.exerciseLibrary.containsExercise(name)) {
            SharedGuiComponents.showError("An exercise with this name already exists.");
            return false;
        }
        
        return true;
    }

    // HELPER: for createExercise
    // EFFECTS: Create an exercise of the selected type with the provided parameters
    //          Throw IllegalArgumentException if equipment or muscle group selection is invalid
    //          Return null if the selected exercise type is not recognized
    private Exercise createExerciseByType(String name) throws IllegalArgumentException {
        String type = (String) exerciseTypeCombo.getSelectedItem();
        
        Equipment equipment = getSelectedEquipment();
        MuscleGroup muscleGroup = getSelectedMuscleGroup();
        
        if (equipment == null || muscleGroup == null) {
            throw new IllegalArgumentException("Invalid equipment or muscle group selection.");
        }
        
        if (type.equals("Strength Exercise")) {
            return createStrengthExercise(name, equipment, muscleGroup);
        } else if (type.equals("Endurance Exercise")) {
            return createEnduranceExercise(name, equipment, muscleGroup);
        } else if (type.equals("Interval Exercise")) {
            return createIntervalExercise(name, equipment, muscleGroup);
        }
        
        return null;
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Retrieve the Equipment object corresponding to the selected item in the equipment combo box
    //          Return null if no equipment is selected or the selection is invalid
    private Equipment getSelectedEquipment() {
        String equipmentName = (String) equipmentCombo.getSelectedItem();
        return SharedGuiComponents.predefinedData.getAllEquipment().get(equipmentName);
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Retrieve the MuscleGroup object corresponding to the selected item in the muscle group combo box
    //          Return null if no muscle group is selected or the selection is invalid
    private MuscleGroup getSelectedMuscleGroup() {
        String muscleGroupName = (String) muscleGroupCombo.getSelectedItem();
        return SharedGuiComponents.predefinedData.getAllMuscleGroups().get(muscleGroupName);
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Create and return a StrengthExercise with values from the strength panel spinners
    //          and the provided name, equipment, and muscle group
    private StrengthExercise createStrengthExercise(String name, Equipment equipment, MuscleGroup muscleGroup) {
        int sets = (Integer) setsSpinner.getValue();
        int reps = (Integer) repsSpinner.getValue();
        double secondsPerRep = (Double) secondsPerRepSpinner.getValue();
        double restTime = (Double) restTimeSpinner.getValue();
        
        return new StrengthExercise(name, sets, reps, secondsPerRep, restTime, equipment, muscleGroup);
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Create and return an EnduranceExercise with the value from the duration spinner
    //          and the provided name, equipment, and muscle group
    private EnduranceExercise createEnduranceExercise(String name, Equipment equipment, MuscleGroup muscleGroup) {
        double duration = (Double) durationSpinner.getValue();
        return new EnduranceExercise(name, duration, equipment, muscleGroup);
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Create and return an IntervalExercise with values from the interval panel spinners
    //          and the provided name, equipment, and muscle group
    private IntervalExercise createIntervalExercise(String name, Equipment equipment, MuscleGroup muscleGroup) {
        double timeOn = (Double) timeOnSpinner.getValue();
        double timeOff = (Double) timeOffSpinner.getValue();
        int repetitions = (Integer) repetitionsSpinner.getValue();
        
        return new IntervalExercise(name, timeOn, timeOff, repetitions, equipment, muscleGroup);
    }

    // HELPER: for createExercise
    // EFFECTS: Clear all form fields and reset them to their default values
    //          Prepare the form for creating another exercise
    private void resetForm() {
        nameField.setText("");
        exerciseTypeCombo.setSelectedIndex(0);
        equipmentCombo.setSelectedIndex(0);
        muscleGroupCombo.setSelectedIndex(0);
        
        resetSpinners();
    }

    // HELPER: for resetForm
    // EFFECTS: Reset all spinner values to their default initial values
    private void resetSpinners() {
        setsSpinner.setValue(3);
        repsSpinner.setValue(10);
        secondsPerRepSpinner.setValue(3.0);
        restTimeSpinner.setValue(1.0);
        
        durationSpinner.setValue(20.0);
        
        timeOnSpinner.setValue(30.0);
        timeOffSpinner.setValue(15.0);
        repetitionsSpinner.setValue(10);
    }
}