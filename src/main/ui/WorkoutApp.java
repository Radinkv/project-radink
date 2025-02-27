package ui;

import model.exercise.*;
import model.workout.*;
import ui.components.*;
import utility.PredefinedData;

import java.util.*;

/**
 * This interface is a console-based implementation for a workout planning program.
 * This program provides functionality for creating and managing exercises, workouts, schedules, and viewing metrics.
 * 
 * The Core Features Summarized:
 * 
 * Exercise creation and management - 
 * Workout planning and organization -
 * Weekly schedule management - 
 * Equipment usage metric analysis -
 * Muscle group usage metric analysis -
 * Muscle usage metric analysis -
 * Exercise training split metric analysis 
 */
public class WorkoutApp {

    private final Scanner input;
    private final ExerciseLibrary exerciseLibrary;
    private final WorkoutLibrary workoutLibrary;
    private final PredefinedData predefinedData;
    private final WeeklySchedule weeklySchedule;
    
    private final ExerciseCreationUI exerciseCreationUI;
    private final ExerciseManagementUI exerciseManagementUI;
    private final WorkoutCreationUI workoutCreationUI;
    private final WorkoutManagementUI workoutManagementUI;
    private final ScheduleUI scheduleUI;
    private final MetricsUI metricsUI;
    private final PersistenceUI persistenceUI;

    public static void main(String[] args) {
        new WorkoutApp();
    }

    // EFFECTS: Initialize the WorkoutApp by setting up input, libraries, predefined data, schedule,
    //          and all UI components, then run the application
    public WorkoutApp() {
        this.input = new Scanner(System.in);
        this.exerciseLibrary = new ExerciseLibrary();
        this.workoutLibrary = new WorkoutLibrary();
        this.predefinedData = new PredefinedData();
        this.weeklySchedule = new WeeklySchedule();
        
        // MetricsUI will work with this same instantiation of equipment and muscle metrics UI
        SharedUI.initializeItems(input, exerciseLibrary, workoutLibrary, weeklySchedule, predefinedData);
        this.exerciseCreationUI = new ExerciseCreationUI();
        this.exerciseManagementUI = new ExerciseManagementUI();
        this.workoutCreationUI = new WorkoutCreationUI();
        this.workoutManagementUI = new WorkoutManagementUI();
        this.scheduleUI = new ScheduleUI();
        this.metricsUI = new MetricsUI();
        this.persistenceUI = new PersistenceUI(); 
        runWorkoutApp();
    }

    // EFFECTS: Continuously display the main menu and process user commands
    //          The application terminates should the user select the exit option
    private void runWorkoutApp() {
        boolean keepGoing = true;
        while (keepGoing) {
            displayMenu();
            String command = input.nextLine().trim();
            if (command.equals("9")) {
                System.out.println("Thank you for using Workout Planner!");
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // EFFECTS: Display the main menu for the workout planning system
    private void displayMenu() {
        System.out.println("\n=== Workout Planning System ===");
        System.out.println("[1] Create Exercise");
        System.out.println("[2] Manage Exercises");
        System.out.println("[3] Create Workout");
        System.out.println("[4] Manage Workouts");
        System.out.println("[5] Edit Weekly Schedule");
        System.out.println("[6] View Metrics");
        System.out.println("[7] Save Program State");
        System.out.println("[8] Load Program State");
        System.out.println("[9] Exit");
        System.out.print("\nSelect an option: ");
    }

    // REQUIRES: command is not null
    // EFFECTS: Process the user's command by invoking the corresponding UI component
    private void processCommand(String command) {
        switch (command) {
            case "1":
                exerciseCreationUI.createExercise();
                break;
            case "2":
                exerciseManagementUI.manageExercises();
                break;
            case "3":
                workoutCreationUI.createWorkout();
                break;
            case "4":
                workoutManagementUI.manageWorkouts();
                break;
            case "5":
                scheduleUI.editWeeklySchedule();
                break;
            case "6":
                metricsUI.viewMetrics();
                break;
            case "7":
                persistenceUI.saveState();
                break;
            case "8":
                persistenceUI.loadState();
                break;
            case "9":
                System.out.println("Thank you for using Workout Planner!");
                return;
            default:
                System.out.println("Invalid selection. Please choose between 1-9.");
                SharedUI.waitForEnter();
        }
    }
}