package ui.console.components;

import static ui.console.components.SharedUI.*;

import java.util.List;

import model.workout.WorkoutPlan;

/** This UI interface is constructed to manage existing workouts within WorkoutLibrary. */
public class WorkoutManagementUI {

    // EFFECTS: Display a list of workouts and provides an interactive menu to manage them
    //          If no workouts exist, returns immediately; Otherwise, allow the user to
    //          select and manage individual workouts (view details or delete) until they should choose to exit
    public void manageWorkouts() {
        while (true) {
            clearScreen();
            List<WorkoutPlan> workouts = viewWorkouts();
            if (workouts.isEmpty()) {
                return;
            }
            if (!processWorkoutManagement(workouts)) {
                break;
            }
        }
    }

    // HELPER: for manageWorkouts
    // EFFECTS: Display all workouts in the library and return them as a list
    private List<WorkoutPlan> viewWorkouts() {
        List<WorkoutPlan> workouts = workoutLibrary.getAllWorkouts();
        
        if (workouts.isEmpty()) {
            System.out.println("\nNo workouts available.");
            waitForEnter();
            return workouts;
        }

        displayWorkoutList(workouts);
        return workouts;
    }

    // HELPER: for viewWorkouts
    // REQUIRES: workouts != null
    // EFFECTS: Display a formatted list of workouts
    private void displayWorkoutList(List<WorkoutPlan> workouts) {
        System.out.println("\n=== Available Workouts ===");
        System.out.println("---------------------------");
        for (int i = 0; i < workouts.size(); i++) {
            WorkoutPlan workout = workouts.get(i);
            System.out.printf("[%d] %-25s (%s)\n", 
                    i, 
                    truncateString(workout.getName(), 25),
                    formatDuration(Math.round(workout.getDuration()))
            );
        }
        System.out.println("---------------------------");
    }

    // HELPER: for manageWorkouts
    // REQUIRES: workouts != null
    // EFFECTS: Process workout management commands and return false if user chooses to exit
    private boolean processWorkoutManagement(List<WorkoutPlan> workouts) {
        System.out.print("\nSelect workout to manage " + BACK_OPTION + ": ");
        String command = input.nextLine().trim();
        if (command.equalsIgnoreCase("b")) {
            return false;
        }
        handleWorkoutSelection(workouts, command);
        return true;
    }

    // HELPER: for processWorkoutManagement
    // REQUIRES: workouts != null, command != null
    // EFFECTS: Handle the selection of a workout for management
    private void handleWorkoutSelection(List<WorkoutPlan> workouts, String command) {
        try {
            int index = Integer.parseInt(command);
            if (index >= 0 && index < workouts.size()) {
                manageWorkout(workouts.get(index));
            } else {
                System.out.println("Invalid selection.");
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for handleWorkoutSelection
    // REQUIRES: workout != null
    // EFFECTS: Manage a specific workout, providing options for viewing and deletion
    private void manageWorkout(WorkoutPlan workout) {
        while (true) {
            displayWorkoutDetails(workout);
            if (!processWorkoutOptions(workout)) {
                break;
            }
        }
    }

    // HELPER: for manageWorkout
    // REQUIRES: workout != null
    // EFFECTS: Process workout management options and return false if user chooses to exit
    private boolean processWorkoutOptions(WorkoutPlan workout) {
        String choice = input.nextLine().trim();
        switch (choice) {
            case "1":
                if (confirmWorkoutDeletion(workout)) {
                    return false;
                }
                break;
            case "2":
                return false;
            default:
                System.out.println(INVALID_INPUT + " Select a valid option.");
                waitForEnter();
        }  
        return true;
    }

    // HELPER: for processWorkoutOptions
    // REQUIRES: workout != null
    // MODIFIES: WorkoutLibrary
    // EFFECTS: Confirm and process workout deletion; return true if deletion from WorkoutLibrary occurred
    //          Remove Workout from WeeklySchedule if in WeeklySchedule (metrics are automatically deleted)
    private boolean confirmWorkoutDeletion(WorkoutPlan workout) {
        System.out.print("\nAre you sure you want to delete '" + workout.getName() + "'? (y/n): ");
        String answer = input.nextLine().trim();
        if (answer.equalsIgnoreCase("y")) {
            // Remove from schedule first
            for (int i = 0; i < 7; i++) {
                WorkoutPlan plan = weeklySchedule.getScheduleForDay(i);
                if (plan.getName().equals(workout.getName())) {
                    weeklySchedule.clearScheduleForDay(i);
                }
            }
            // Then remove from library
            workoutLibrary.removeWorkout(workout.getName());
            System.out.println("Workout deleted successfully!");
            waitForEnter();
            return true;
        }
        return false;
    }
}
