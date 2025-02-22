package ui.components;

import java.util.ArrayList;
import java.util.List;
import static ui.components.SharedUI.*;
import model.exercise.Exercise;
import model.workout.Workout;
import model.workout.WorkoutPlan;

/** This UI component handles workout creation. It collects user input to build a workout with a name
 *  and a set of selected exercises from the user's self-built ExerciseLibrary
 */
public class WorkoutCreationUI {

    // MODIFIES: WorkoutLibrary
    // EFFECTS: Handle the workout creation process flow:
    //          1. Clear screen and check for available exercises, exit if none exist
    //          2. Display available programs, and prompt the user for the workout name
    //             Exit if the name is empty/null or thhe user cancels
    //          3. Present the exercise selection menu for adding/removing exercises and prompt 
    //             completion/cancellation checks after each addition or removal
    //          4. Save the created Workout to WorkoutLibrary if exercises were selected and confirmed
    //          5. Display workout summary and confirmation message if creation is successful
    public void createWorkout() {
        clearScreen();
        List<Exercise> availableExercises = viewExercises();
        if (availableExercises.isEmpty()) {
            System.out.println("\nNo exercises available to create a workout.");
            System.out.println("Please create some exercises first.");
            waitForEnter();
            return;
        }
        createWorkoutFromExercises(availableExercises);
    }

    // HELPER: for createWorkout
    // REQUIRES: availableExercises != null and contains no null elements
    // EFFECTS: Create a workout from available exercises if the workout name is valid
    private void createWorkoutFromExercises(List<Exercise> availableExercises) {
        String name = readWorkoutName();
        if (name == null) {
            return;
        }
        List<Exercise> selectedExercises = selectExercisesForWorkout(availableExercises);
        if (selectedExercises.isEmpty()) {
            System.out.println("Workout creation cancelled. No exercises selected.");
            waitForEnter();
            return;
        }
        saveAndDisplayWorkout(name, selectedExercises);
    }

    // MODIFIES: WorkoutLibrary
    // REQUIRES: name != null; selectedExercises != null and contains no null elements
    // EFFECTS: Save the workout and display its summary
    private void saveAndDisplayWorkout(String name, List<Exercise> selectedExercises) {
        try {
            Workout workout = new Workout(name, selectedExercises);
            workoutLibrary.addWorkout(workout);
            System.out.println("\nWorkout '" + name + "' created successfully!");
            displayWorkoutSummary(workout);
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating workout: " + e.getMessage());
        }
        waitForEnter();
    }

    // HELPER: for createWorkoutFromExercises
    // EFFECTS: Read and validate a workout name from user input, return null if cancelled
    private String readWorkoutName() {
        while (true) {
            System.out.print("\nEnter workout name " + BACK_OPTION + ": ");
            String name = input.nextLine().trim();
            if (name.equalsIgnoreCase("b")) {
                return null;
            }
            if (name.isEmpty()) {
                System.out.println("Workout name cannot be empty.");
                continue;
            }
            if (isWorkoutNameValid(name)) {
                return name;
            }
        }
    }

    // HELPER: for readWorkoutName
    // EFFECTS: Check if the workout name is valid (not already in use or not null) return false if invalid
    private boolean isWorkoutNameValid(String name) {
        WorkoutPlan workout;
        workout = workoutLibrary.getWorkout(name);
        return (workout == null);
    }

    // HELPER: for createWorkoutFromExercises
    // REQUIRES: availableExercises != null and contains no null elements
    // EFFECTS: Manage the exercise selection process for workout creation and return the selected exercises
    private List<Exercise> selectExercisesForWorkout(List<Exercise> availableExercises) {
        List<Exercise> selectedExercises = new ArrayList<Exercise>();
        while (true) {
            displayCurrentSelection(selectedExercises);
            displaySelectionOptions();
            String choice = input.nextLine().trim().toUpperCase();
            if (!processSelectionChoice(choice, availableExercises, selectedExercises)) {
                return selectedExercises;
            }
        }
    }

    // HELPER: for selectExercisesForWorkout
    // REQUIRES: selectedExercises != null.
    // EFFECTS: Display currently selected exercises
    private void displayCurrentSelection(List<Exercise> selectedExercises) {
        System.out.print("\nCurrent selection: ");
        if (selectedExercises.isEmpty()) {
            System.out.println("None");
            return;
        }
        // List exercise names separated by a comma, and print them
        String exercises = generateExerciseNames(selectedExercises);
        System.out.println(exercises);
    }

    // HELPER: for selectExercisesForWorkout
    // EFFECTS: Display exercise selection options.
    private void displaySelectionOptions() {
        System.out.println("\nOptions:");
        System.out.println("[A] Add exercises");
        System.out.println("[R] Remove exercises");
        System.out.println("[C] Continue to create workout");
        System.out.println("[B] Cancel workout creation");
        System.out.print("\nSelect option: ");
    }

    // HELPER: for selectExercisesForWorkout
    // REQUIRES: choice != null; availableExercises and selectedExercises are not null and contain no null elements.
    // EFFECTS: Process user's selection choice; return false if selection is complete.
    private boolean processSelectionChoice(String choice, List<Exercise> availableExercises, 
            List<Exercise> selectedExercises) {
        switch (choice) {
            case "A":
                addExercisesToSelection(availableExercises, selectedExercises);
                return true;
            case "R":
                removeExercisesFromSelection(selectedExercises);
                return true;
            case "C":
                return !handleContinueChoice(selectedExercises);
            case "B":
                selectedExercises.clear();
                return false;
            default:
                System.out.println("Invalid option selected.");
                return true;
        }
    }

    // HELPER: for processSelectionChoice
    // REQUIRES: selectedExercises != null
    // EFFECTS: Handle continue choice, return true if selection should continue (user has selected exercises)
    private boolean handleContinueChoice(List<Exercise> selectedExercises) {
        if (selectedExercises.isEmpty()) {
            System.out.println("Select at least one exercise.");
            return false;
        }
        return true;
    }

    // HELPER: for processSelectionChoice (case "A")
    // REQUIRES: availableExercises and selectedExercises are not null and contain no null elements
    // EFFECTS: Add exercises to the selection based on user input
    private void addExercisesToSelection(List<Exercise> availableExercises, List<Exercise> selectedExercises) {
        clearScreen();
        viewExercises();
        System.out.println("\nEnter exercise numbers separated by spaces (i.e. '0 2 5')");
        System.out.print("Selection: ");
        String[] selections = input.nextLine().trim().split("\\s+"); // Would've used Arraylist<String>
        processExerciseAdditions(selections, availableExercises, selectedExercises);
    }

    // HELPER: for addExercisesToSelection
    // REQUIRES: selections, availableExercises, selectedExercises are 
    //           not null and availableExercises contains no null elements
    // EFFECTS: Process exercise additions from user input
    private void processExerciseAdditions(String[] selections, List<Exercise> availableExercises, 
            List<Exercise> selectedExercises) {
        for (String s : selections) {
            try {
                int index = Integer.parseInt(s);
                if (index >= 0 && index < availableExercises.size()) {
                    Exercise exercise = availableExercises.get(index);
                    if (!selectedExercises.contains(exercise)) {
                        selectedExercises.add(exercise);
                    }
                }
            } catch (NumberFormatException ignore) {
                continue; // Skip the invalid numbers
            }
        }
    }

    // HELPER: for processSelectionChoice (case "R")
    // REQUIRES: selectedExercises != null and contains no null elements
    // EFFECTS: Remove exercises from the selection based on user input
    private void removeExercisesFromSelection(List<Exercise> selectedExercises) {
        if (selectedExercises.isEmpty()) {
            System.out.println("No exercises selected to remove.");
            return;
        }
        displayRemovalOptions(selectedExercises);
        processExerciseRemovals(selectedExercises);
    }

    // HELPER: for removeExercisesFromSelection
    // REQUIRES: selectedExercises != null and contains no null elements
    // EFFECTS: Display exercise removal options
    private void displayRemovalOptions(List<Exercise> selectedExercises) {
        System.out.println("\nCurrently selected exercises:");
        for (int i = 0; i < selectedExercises.size(); i++) {
            System.out.printf("[%d] %s\n", i, selectedExercises.get(i).getName());
        }
        System.out.println("\nEnter exercise numbers to remove (separate by spaces):");
    }

    // HELPER: for removeExercisesFromSelection
    // REQUIRES: selectedExercises != null and contains no null elements
    // EFFECTS: Process exercise removals from user input
    private void processExerciseRemovals(List<Exercise> selectedExercises) {
        String[] selections = input.nextLine().trim().split("\\s+");
        List<Integer> indicesToRemove = new ArrayList<Integer>();
        for (String s : selections) {
            try {
                int index = Integer.parseInt(s);
                if (index >= 0 && index < selectedExercises.size()) {
                    indicesToRemove.add(index);
                }
            } catch (NumberFormatException ignored) {
                // Skip invalid numbers
            }
        }
        removeSelectedExercises(selectedExercises, indicesToRemove);
    }

    // HELPER: for processExerciseRemovals
    // REQUIRES: selectedExercises and indicesToRemove are not null, selectedExercises contains no null elements
    // EFFECTS: Remove selected exercises from the list
    private void removeSelectedExercises(List<Exercise> selectedExercises, List<Integer> indicesToRemove) {
        indicesToRemove.sort((a, b) -> b.compareTo(a)); // Reverse order to avoid IndexOutOfBoundsException
        for (int index : indicesToRemove) {
            selectedExercises.remove(index);
        }
    }

    // HELPER: for saveAndDisplayWorkout
    // REQUIRES: workout != null, workout.getExercises() != null and contains no null elements
    // EFFECTS: Display a summary of the created workout
    private void displayWorkoutSummary(Workout workout) {
        System.out.println("\nWorkout Summary:");
        System.out.println("Name: " + workout.getName());
        System.out.print("Exercises: ");
        List<Exercise> exercises = workout.getExercises();
        if (exercises.isEmpty()) {
            System.out.println("None");
        } else {
            String summaryExercises = generateExerciseNames(exercises);
            System.out.println(summaryExercises);
        }
    }

    // HELPER: for displayCurrentSelection, displayWorkoutSummary
    // REQUIRES: exercises != null and contains no null elements
    // EFFECTS: Concatenate Exercise names in exercises list to a string
    private String generateExerciseNames(List<Exercise> exercises) {
        String exerciseNames = "";
        for (int i = 0; i < exercises.size(); i++) {
            if (i > 0) {
                exerciseNames += ", ";
            }
            exerciseNames += exercises.get(i).getName();
        }
        return exerciseNames;
    }
}
