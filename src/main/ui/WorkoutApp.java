package ui;

import model.PredefinedData;
import model.association.ExerciseAssociator;
import model.equipment.Equipment;
import model.exercise.*;
import model.muscle.Muscle;
import model.muscle.MuscleGroup;
import model.workout.*;
import ui.components.*;

import java.util.*;

/**
 * Console-based user interface for the workout planning system.
 * Provides functionality for managing exercises, workouts, schedules, and viewing metrics.
 * 
 * Core Features:
 * - Exercise creation and management
 * - Workout planning and organization
 * - Weekly schedule management
 * - Comprehensive metric analysis
 */
public class WorkoutApp {
    
    private final Scanner input;
    private final ExerciseLibrary exerciseLibrary;
    private final WorkoutLibrary workoutLibrary;
    private final PredefinedData predefinedData;
    private final WeeklySchedule weeklySchedule;
    
    // UI Components
    private final ExerciseViewUI exerciseViewUI;
    private final ExerciseCreationUI exerciseCreationUI;
    private final ExerciseManagementUI exerciseManagementUI;
    private final WorkoutCreationUI workoutCreationUI;
    private final WorkoutManagementUI workoutManagementUI;
    private final ScheduleUI scheduleUI;
    private final MetricsUI metricsUI;

    public WorkoutApp() {
        this.input = new Scanner(System.in);
        this.exerciseLibrary = new ExerciseLibrary();
        this.workoutLibrary = new WorkoutLibrary();
        this.predefinedData = new PredefinedData();
        this.weeklySchedule = new WeeklySchedule();
        
        // Initialize UI components
        this.exerciseViewUI = new ExerciseViewUI(input, exerciseLibrary);
        this.exerciseCreationUI = new ExerciseCreationUI(input, exerciseLibrary, predefinedData);
        this.exerciseManagementUI = new ExerciseManagementUI(input, exerciseLibrary);
        this.workoutCreationUI = new WorkoutCreationUI(input, exerciseLibrary, workoutLibrary);
        this.workoutManagementUI = new WorkoutManagementUI(input, workoutLibrary);
        this.scheduleUI = new ScheduleUI(input, workoutLibrary, weeklySchedule);
        this.metricsUI = new MetricsUI(input, predefinedData);
        
        runWorkoutApp();
    }

    private void runWorkoutApp() {
        boolean keepGoing = true;

        while (keepGoing) {
            displayMenu();
            String command = input.nextLine().trim();

            if (command.equals("8")) {
                System.out.println("Thank you for using Workout Planner!");
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Workout Planning System ===");
        System.out.println("[1] View Exercises");
        System.out.println("[2] Create Exercise");
        System.out.println("[3] Manage Exercises");
        System.out.println("[4] Create Workout");
        System.out.println("[5] Manage Workouts");
        System.out.println("[6] Edit Weekly Schedule");
        System.out.println("[7] View Metrics");
        System.out.println("[8] Exit");
        System.out.print("\nSelect an option: ");
    }

    private void processCommand(String command) {
        switch (command) {
            case "1":
                exerciseViewUI.viewExercises();
                break;
            case "2":
                exerciseCreationUI.createExercise();
                break;
            case "3":
                exerciseManagementUI.manageExercises();
                break;
            case "4":
                workoutCreationUI.createWorkout();
                break;
            case "5":
                workoutManagementUI.manageWorkouts();
                break;
            case "6":
                scheduleUI.editWeeklySchedule();
                break;
            case "7":
                metricsUI.viewMetrics();
                break;
            default:
                System.out.println("Invalid selection. Please choose between 1-8.");
                SharedUI.waitForEnter(input);
        }
    }
}