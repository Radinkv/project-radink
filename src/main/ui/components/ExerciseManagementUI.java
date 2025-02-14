package ui.components;

import static ui.components.SharedUI.*;
import java.util.List;
import model.exercise.Exercise;

/** This UI component manages Exercise viewing and deletion based on what the user chooses.  */
public class ExerciseManagementUI {

    // EFFECTS: Display the exercise management UI, which ultimately allows the user to delete exercises
    //          If no exercises are available, inform the user and exit this UI segment
    public void manageExercises() {
        while (true) {
            clearScreen();
            List<Exercise> exercises = viewExercises();
            if (exercises.isEmpty()) {
                System.out.println("No exercises to manage.");
                waitForEnter();
                return;
            }
            if (!processExerciseManagement(exercises)) {
                break;
            }
        }
    }

    // HELPER: for manageExercises
    // REQUIRES: exercises is not null and contains no null elements.
    // EFFECTS: Process exercise management commands; return false if the user chooses to exit.
    private boolean processExerciseManagement(List<Exercise> exercises) {
        System.out.print("\nSelect exercise to delete " + BACK_OPTION + ": ");
        String command = input.nextLine().trim();
        if (command.equalsIgnoreCase("b")) {
            return false;
        }
        handleExerciseDeletion(exercises, command);
        return true;
    }

    // HELPER: for processExerciseManagement
    // REQUIRES: exercises is not null and contains no null elements; command is not null.
    // EFFECTS: Handle the deletion of the selected exercise.
    private void handleExerciseDeletion(List<Exercise> exercises, String command) {
        try {
            int index = Integer.parseInt(command);
            if (index >= 0 && index < exercises.size()) {
                confirmAndDeleteExercise(exercises.get(index));
            } else {
                System.out.println(INVALID_INPUT);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
        waitForEnter();
    }

    // HELPER: for handleExerciseDeletion
    // REQUIRES: exercise is not null.
    // MODIFIES: ExerciseLibrary
    // EFFECTS: Confirm with the user and delete the specified exercise if confirmed.
    private void confirmAndDeleteExercise(Exercise exercise) {
        System.out.print("\nAre you sure you want to delete '" + exercise.getName() + "'? (y/n): ");
        String confirmation = input.nextLine().trim();
        if (confirmation.equalsIgnoreCase("y")) {
            exerciseLibrary.removeExercise(exercise.getName());
            System.out.println("Exercise deleted successfully!");
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
}
