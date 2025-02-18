package ui.components;

import static ui.components.SharedUI.*;
import java.util.List;
import java.util.Map;
import model.exercise.Exercise;
import model.workout.Workout;
import model.workout.WorkoutPlan;

/** This UI component manages Exercise viewing, details display, and deletion based on user choice. */
public class ExerciseManagementUI {

    // EFFECTS: Display the exercise management UI, which allows users to view details or delete exercises
    //          If no exercises are available, inform the user and exit this UI segment
    public void manageExercises() {
        while (true) {
            clearScreen();
            List<Exercise> exercises = viewExercises();
            if (exercises.isEmpty()) {
                waitForEnter();
                return;
            }
            displayManagementOptions();
            if (!processExerciseManagement(exercises)) {
                break;
            }
        }
    }

    // EFFECTS: Display the available management options to the user
    private void displayManagementOptions() {
        System.out.println("\nOptions:");
        System.out.println("[0] View exercise details");
        System.out.println("[1] Delete exercise");
        System.out.println("[2] Back to Main Menu");
    }

    // HELPER: for manageExercises
    // REQUIRES: exercises != null and contains no null elements.
    // EFFECTS: Process exercise management commands; return false if the user chooses to exit.
    private boolean processExerciseManagement(List<Exercise> exercises) {
        System.out.print("\nSelect option: ");
        String command = input.nextLine().trim().toUpperCase();
        
        switch (command) {
            case "0":
                handleExerciseDetails(exercises);
                break;
            case "1":
                handleExerciseDeletion(exercises);
                break;
            case "2":
                return false;
            default:
                System.out.println(INVALID_INPUT);
                waitForEnter();
        }
        return true;
    }

    // HELPER: for processExerciseManagement
    // REQUIRES: exercises != null and contains no null elements
    // EFFECTS: Handle the viewing of exercise details
    private void handleExerciseDetails(List<Exercise> exercises) {
        clearScreen();
        viewExercises(); // Easier to follow and understand UI flow for user
        System.out.print("\nSelect exercise to view details " + BACK_OPTION + ": ");
        String command = input.nextLine().trim();
        if (command.equalsIgnoreCase("b")) {
            return;
        }
        try {
            int index = Integer.parseInt(command);
            if (index >= 0 && index < exercises.size()) {
                displayExerciseDetails(exercises.get(index));
                waitForEnter();
            } else {
                System.out.println(INVALID_INPUT);
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for handleExerciseDetails
    // REQUIRES: exercise != null
    // EFFECTS: Display detailed information about the specified exercise
    private void displayExerciseDetails(Exercise exercise) {
        System.out.println("\n=== Exercise Details ===");
        System.out.println("Name: " + exercise.getName());
        System.out.println("Type: " + exercise.exerciseType());
        System.out.println("---------------------------");
        
        Map<String, Double> info = exercise.getInfo();
        if (info.isEmpty()) {
            System.out.println("No additional information available.");
            return;
        }
        // Display total duration first
        if (info.containsKey("totalDuration")) {
            System.out.println("Total Duration: " 
                    + formatDuration(Math.round(info.get("totalDuration")))
            );
        }
        if (info.containsKey("sets") && info.containsKey("reps")) { // Strength Exercise
            displayStrengthInfo(info);
        } else if (info.containsKey("timeOn")) { // Interval Exercise
            displayIntervalInfo(info); 
        } else if (info.containsKey("duration")) { // Endurance Exercise
            displayEnduranceInfo(info);
        } else {
            System.out.println("No exercise info available for this exercise.");
        }
    }

    // HELPER: for displayExerciseDetails
    // REQUIRES: info != null and contains all of StrengthExercise's promised info keys & values
    // EFFECTS: Display the details of a StrengthExercise
    private void displayStrengthInfo(Map<String, Double> info) {
        System.out.println("\nTraining Details:");
        System.out.println("---------------------------");
        System.out.printf("Sets: %.0f\n", info.get("sets"));
        System.out.printf("Reps: %.0f\n", info.get("reps"));
        if (info.containsKey("timePerRep")) {
            System.out.printf("Time Per Rep: %.1f seconds\n", info.get("timePerRep"));
        }
        if (info.containsKey("restTime")) {
            System.out.printf("Rest Time (Between Each Set): %.1f minutes\n", info.get("restTime"));
        }
    }

    // HELPER: for displayExerciseDetails
    // REQUIRES: info != null and contains all of IntervalExercise's promised info keys & values
    // EFFECTS: Display the details of an IntervalExercise
    private void displayIntervalInfo(Map<String, Double> info) {
        System.out.println("\nInterval Details:");
        System.out.println("---------------------------");
        System.out.printf("Active Time: %.1f seconds\n", info.get("timeOn"));
        System.out.printf("Rest Time (Between Each Active Portion): %.1f seconds\n", info.get("timeOff"));
        System.out.printf("Repetitions: %.0f\n", info.get("repititions"));
    }

    // HELPER: for displayExerciseDetails
    // REQUIRES: info != null and contains all of EnduranceExercise's promised info keys & values
    // EFFECTS: Display the details of an EnduranceExercise
    private void displayEnduranceInfo(Map<String, Double> info) {
        System.out.println("\nEndurance Details:");
        System.out.println("---------------------------");
        System.out.printf("Duration: %.1f minutes\n", info.get("duration"));
    }

    // HELPER: for processExerciseManagement
    // REQUIRES: exercises != null and contains no null elements
    // EFFECTS: Handle the deletion selection process
    private void handleExerciseDeletion(List<Exercise> exercises) {
        clearScreen();
        viewExercises(); // Easier to follow and understand UI flow for user
        System.out.print("\nSelect exercise to delete " + BACK_OPTION + ": ");
        String command = input.nextLine().trim();
        if (command.equalsIgnoreCase("b")) {
            return;
        }
        try {
            int index = Integer.parseInt(command);
            if (index >= 0 && index < exercises.size()) {
                confirmAndDeleteExercise(exercises.get(index));
                waitForEnter();
            } else {
                System.out.println(INVALID_INPUT);
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for handleExerciseDeletion
    // REQUIRES: exercise != null
    // MODIFIES: ExerciseLibrary
    // EFFECTS: Confirm with the user and delete the specified exercise if confirmed
    //          Delete exercise from any Workout instance
    private void confirmAndDeleteExercise(Exercise exercise) {
        System.out.print("\nAre you sure you want to delete '" + exercise.getName() + "'? (y/n): ");
        String confirmation = input.nextLine().trim();
        if (confirmation.equalsIgnoreCase("y")) {
            // Remove from all workouts first
            for (WorkoutPlan workoutPlan : workoutLibrary.getAllWorkouts()) {
                if (workoutPlan instanceof Workout) {
                    ((Workout) workoutPlan).removeExercise(exercise.getName());
                }
            }
            // Then remove from library
            exerciseLibrary.removeExercise(exercise.getName());
            System.out.println("Exercise deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}