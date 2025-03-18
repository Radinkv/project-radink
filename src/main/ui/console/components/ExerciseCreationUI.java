package ui.console.components;

import static ui.console.components.SharedUI.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.equipment.Equipment;
import model.exercise.EnduranceExercise;
import model.exercise.Exercise;
import model.exercise.IntervalExercise;
import model.exercise.StrengthExercise;
import model.muscle.MuscleGroup;

/** This UI component handles exercise creation. It collects user input to create strength, 
 * endurance, or interval exercises (one at a time) with their user-selected associated equipment
 * and muscle groups
 */
public class ExerciseCreationUI {

    // MODIFIES: ExerciseLibrary  
    // EFFECTS: First, prompt the user for an exercise name and return if empty/null or user cancels
    //          Then, present the exercise type selection menu for strength/endurance/interval exercises
    //          After, collect type-specific parameters and required selections, such as used Equipment
    //          and targeted MuscleGroup
    //          Finally, add the created Exercise to Exerciselibrary 
    //          Display confirmation message if creation is successful
    public void createExercise() {
        clearScreen();
        System.out.println("\n=== Create New Exercise ===");
        String name = readExerciseName();
        if (name == null || name == "") { // Not necessarily necessary, but just-in-case
            return;
        }
        Exercise exercise = createExerciseByType(name);
        if (exercise != null) { // Same thing
            exerciseLibrary.addExercise(exercise);
            System.out.println("\nExercise '" + name + "' created successfully!");
        }
        waitForEnter();
    }

    // HELPER: for createExercise
    // REQUIRES: name is not null
    // EFFECTS: Create an exercise of a specified type based on the provided name and user selection
    private Exercise createExerciseByType(String name) {
        displayExerciseTypeMenu();
        String type = input.nextLine().trim();
        try {
            switch (type) {
                case "1":
                    return createStrengthExercise(name);
                case "2":
                    return createEnduranceExercise(name);
                case "3":
                    return createIntervalExercise(name);
                case "4":
                    return null;
                default:
                    System.out.println("Invalid exercise type selected.");
                    return null;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input: " + e.getMessage());
            return null;
        }
    }

    // HELPER: for createExerciseByType
    // EFFECTS: Display the exercise type selection menu
    private void displayExerciseTypeMenu() {
        System.out.println("\nSelect exercise type:");
        System.out.println("[1] Strength Exercise (sets & reps based)");
        System.out.println("[2] Endurance Exercise (duration based)");
        System.out.println("[3] Interval Exercise (intervals based)");
        System.out.println("[4] Back to Main Menu");
    }

    // HELPER: for createExercise
    // EFFECTS: Read the exercise name from user input; return null if the user cancels
    private String readExerciseName() {
        while (true) {
            System.out.print("\nEnter exercise name " + BACK_OPTION + ": ");
            String name = input.nextLine().trim();
            if (name.equalsIgnoreCase("b")) {
                return null;
            }
            if (name.isEmpty()) {
                System.out.println("Exercise name cannot be empty.");
                continue;
            }
            if (exerciseLibrary.containsExercise(name)) {
                System.out.println("An exercise with this name already exists.");
                continue;
            }
            return name;
        }
    }

    // HELPER: for createExerciseByType (case "1")
    // REQUIRES: name is not null
    // EFFECTS: Create a strength exercise by reading the required parameters from the user
    private StrengthExercise createStrengthExercise(String name) {
        System.out.println("\n=== Strength Exercise Details ===");
        int sets = readIntInput("Enter number of sets: ", 1, 100); // One of the many guards that all UI component 
        int reps = readIntInput("Enter number of reps: ", 1, 100); // implementations assume and rely on
        double secondsPerRep = readPositiveDouble("Enter seconds per rep: "); // Has a positive number guard
        double restTime = readPositiveDouble("Enter rest time (minutes): ");
        Equipment equipment = selectEquipment();
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment selection cancelled.");
        }
        MuscleGroup muscleGroup = selectMuscleGroup();
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Muscle group selection cancelled.");
        }
        return new StrengthExercise(name, sets, reps, secondsPerRep, restTime, equipment, muscleGroup);
    }

    // HELPER: for createExerciseByType (case "2")
    // REQUIRES: name is not null
    // EFFECTS: Create an endurance exercise by reading the required parameters from the user.
    private EnduranceExercise createEnduranceExercise(String name) {
        System.out.println("\n=== Endurance Exercise Details ===");
        double duration = readPositiveDouble("Enter total duration (minutes): ");
        Equipment equipment = selectEquipment();
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment selection cancelled.");
        }
        MuscleGroup muscleGroup = selectMuscleGroup();
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Muscle group selection cancelled.");
        }
        return new EnduranceExercise(name, duration, equipment, muscleGroup);
    }

    // HELPER: for createExerciseByType (case "3")
    // REQUIRES: name is not null
    // EFFECTS: Create an interval exercise by reading the required parameters from the user.
    private IntervalExercise createIntervalExercise(String name) {
        System.out.println("\n=== Interval Exercise Details ===");
        double timeOn = readPositiveDouble("Enter active time per interval (seconds): ");
        double timeOff = readPositiveDouble("Enter rest time per interval (seconds): ");
        int repetitions = readIntInput("Enter number of repetitions: ", 1, 100);
        Equipment equipment = selectEquipment();
        if (equipment == null) {
            throw new IllegalArgumentException("Equipment selection cancelled.");
        }
        MuscleGroup muscleGroup = selectMuscleGroup();
        if (muscleGroup == null) {
            throw new IllegalArgumentException("Muscle group selection cancelled.");
        }
        return new IntervalExercise(name, timeOn, timeOff, repetitions, equipment, muscleGroup);
    }

    // HELPER: for createStrengthExercise, createEnduranceExercise, createIntervalExercise
    // EFFECTS: Handle equipment selection from predefined options and return the selected Equipment,
    //          or return null if the user cancels
    private Equipment selectEquipment() {
        while (true) {

            displayEquipmentOptions();
            String userInput = input.nextLine().trim();
            if (userInput.equalsIgnoreCase("b")) {
                return null;
            }
            Equipment selected = processEquipmentSelection(userInput);
            if (selected != null) {
                return selected;
            }
            System.out.println("Please select a valid option.");
        }
    }

    // HELPER: for selectEquipment
    // EFFECTS: Display available equipment options
    private void displayEquipmentOptions() {
        System.out.println("\n=== Select Equipment ===");
        Map<String, Equipment> equipment = predefinedData.getAllEquipment();
        int index = 0;
        for (Map.Entry<String, Equipment> entry : equipment.entrySet()) {
            System.out.printf("[%d] %s\n", index, entry.getKey());
            index++;
        }
        System.out.print("\nEnter selection " + BACK_OPTION + ": ");
    }

    // HELPER: for selectEquipment
    // REQUIRES: input is not null
    // EFFECTS: Process the equipment selection input and return the selected Equipment,
    //          or return null if the input is invalid
    private Equipment processEquipmentSelection(String input) {
        try {
            int selection = Integer.parseInt(input);
            List<Equipment> equipmentList = new ArrayList<Equipment>(predefinedData.getAllEquipment().values());
            if (selection >= 0 && selection < equipmentList.size()) {
                return equipmentList.get(selection);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
        return null;
    }

    // HELPER: for createStrengthExercise, createEnduranceExercise, createIntervalExercise
    // EFFECTS: Handle muscle group selection from predefined options and return the selected MuscleGroup,
    //          or return null if the user cancels
    private MuscleGroup selectMuscleGroup() {
        while (true) {

            displayMuscleGroupOptions();
            String userInput = input.nextLine().trim();
            if (userInput.equalsIgnoreCase("b")) {
                return null;
            }
            MuscleGroup selected = processMuscleGroupSelection(userInput);
            if (selected != null) {
                return selected;
            }
            System.out.println("Please select a valid option.");
        }
    }

    // HELPER: for selectMuscleGroup
    // EFFECTS: Display available muscle group options
    private void displayMuscleGroupOptions() {
        System.out.println("\n=== Select Muscle Group ===");
        Map<String, MuscleGroup> muscleGroups = predefinedData.getAllMuscleGroups();
        int index = 0;
        for (Map.Entry<String, MuscleGroup> entry : muscleGroups.entrySet()) {
            System.out.printf("[%d] %s\n", index, entry.getKey());
            index++;
        }
        System.out.print("\nEnter selection " + BACK_OPTION + ": ");
    }

    // HELPER: for selectMuscleGroup
    // REQUIRES: input is not null
    // EFFECTS: Process the muscle group selection input and return the selected MuscleGroup,
    //          or return null if the input is invalid
    private MuscleGroup processMuscleGroupSelection(String input) {
        try {
            int selection = Integer.parseInt(input);
            // Map of MuscleGroups to List for easier iteration and user selection (numbered and ordered)
            List<MuscleGroup> groupList = new ArrayList<MuscleGroup>(predefinedData.getAllMuscleGroups().values());
            if (selection >= 0 && selection < groupList.size()) {
                return groupList.get(selection);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
        return null;
    }
}
