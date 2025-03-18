package ui.console.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.exercise.Exercise;
import model.exercise.ExerciseLibrary;
import model.muscle.MuscleGroup;
import model.workout.WeeklySchedule;
import model.workout.Workout;
import model.workout.WorkoutLibrary;
import model.workout.WorkoutPlan;
import utility.PredefinedData;

/** This class contains static, shared helper methods and fields that different UI components collectively use.
 * Its purpose is to minimize redundant code, as it acts as a miscilaneous library which different sections
 * of UI can point to in order to simplify their tasks in any way, shape, or form.
 * 
 * REMINDER, THIS IS NOT A UI CLASS. HOWEVER, EACH UI CLASS HAS ACCESS TO EACH OF THESE FIELDS AND METHODS!
 */
public class SharedUI {
    protected static final String INVALID_INPUT = "Invalid input. Please try again.";
    protected static final String BACK_OPTION = "(or 'b' to go back)";
    protected static final String PRESS_ENTER = "\nPress Enter to continue...";
    protected static Scanner input;
    protected static ExerciseLibrary exerciseLibrary;
    protected static WorkoutLibrary workoutLibrary;
    protected static WeeklySchedule weeklySchedule;
    protected static PredefinedData predefinedData;

    // Helper class for weekly schedule statistics
    protected static class WeeklyStats {
        int totalWorkouts = 0;
        int restDays = 0;
        double totalDuration = 0;
    }

    // REQUIRES: NONE of the fields are null or contain null elements
    // EFFECTS: Initialize the object-pointing fields needed by multiple UI components 
    //          (called once from WorkoutApp constructor)
    public static void initializeItems(Scanner scanner, ExerciseLibrary exerciseLib, 
                                        WorkoutLibrary workoutLib, WeeklySchedule weeklySched,
                                        PredefinedData predefData) {
        input = scanner;
        exerciseLibrary = exerciseLib;
        workoutLibrary = workoutLib;
        weeklySchedule = weeklySched;
        predefinedData = predefData;
    }

    // EFFECTS: Wait for user to press enter
    public static void waitForEnter() {
        System.out.println(PRESS_ENTER);
        input.nextLine();
    }

    // EFFECTS: Clear terminal screen
    // NOTE: Java does not have any built in nor extended ability to clear the terminal
    //       Howeer, printing 50 empty lines gets the job done
    protected static void clearScreen() {
        for (int i = 0; i < 50; ++i) { 
            System.out.println(); 
        } 
    }

    // EFFECTS: Truncate a string to the specified length, adding "..." if truncated
    protected static String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 3) + "...";
    }


    // REQUIRES: prompt is not null
    // EFFECTS: Read one positive double value from user input and return it
    protected static double readPositiveDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(input.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Please enter a positive number.");
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
            }
        }
    }

    // REQUIRES: prompt is not null; min <= max
    // EFFECTS: Read an integer input within the specified range from the user and return it
    protected static int readIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(input.nextLine().trim());
                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.printf("Please enter a number between %d and %d.\n", min, max);
                }
            } catch (NumberFormatException e) {
                System.out.println(INVALID_INPUT);
            }
        }
    }

    // EFFECTS: Build a formatted duration string from seconds
    protected static String buildDurationString(long seconds) {
        long days = seconds / (24 * 3600);
        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        seconds = seconds % 3600;
        long minutes = seconds / 60;
        seconds = seconds % 60;
    
        StringBuilder duration = new StringBuilder();
        appendTimeUnit(duration, days, "day");
        appendTimeUnit(duration, hours, "hour");
        appendTimeUnit(duration, minutes, "minute");
        
        if (seconds > 0 && duration.length() == 0) {
            appendTimeUnit(duration, seconds, "second");
        }
        
        return duration.toString().trim();
    }


    // EFFECTS: Append a time unit to the duration string if the duration value is above 0
    protected static void appendTimeUnit(StringBuilder duration, long value, String unit) {
        if (value > 0) {
            if (duration.length() > 0) {
                duration.append(" ");
            }
            duration.append(value).append(" ").append(unit);
            if (value > 1) {
                duration.append("s");
            }
            duration.append(" ");
        }
    }

    // EFFECTS: Generate a visual ratio/progress bar representation for a given metric
    protected static String generateProgressBar(double value, double max) {
        final int barLength = 30;
        int filledBars;
        // Zero max case
        // Max is a value given by the UI; The maximum metric value out of the group (MuscleGroup or Equipment group)
        if (max == 0) {
            filledBars = 0;
        } else {
            filledBars = (int) Math.round((value / max) * barLength); // Ratio the given metric value based on 
        }                                                             // the maximum
        StringBuilder bar = new StringBuilder("[");
        for (int i = 0; i < barLength; i++) {
            if (i < filledBars) {
                bar.append("█");
            } else {
                bar.append("░");
            }
        }
        bar.append("]");
        return bar.toString();
    }

    // EFFECTS: Display all exercises in ExerciseLibrary and returns a list 
    //          of the Exercises for various UI components to work with
    protected static List<Exercise> viewExercises() {
        Map<String, Exercise> exercises = exerciseLibrary.getAllExercises();
        // exercises.values() is originally a Set, however by converting to a list,
        // each Exercise will get its own number on display. This UI selection components 
        // easier to implement
        List<Exercise> exerciseList = new ArrayList<Exercise>(exercises.values());
        if (exerciseList.isEmpty()) {
            System.out.println("\nNo exercises available.");
            return exerciseList;
        } else {
            displayExerciseList(exerciseList);
            return exerciseList;
        }
    }

    // EFFECTS: Displays the list of exercises from ExerciseLibrary
    protected static void displayExerciseList(List<Exercise> exerciseList) {
        System.out.println("\n=== Available Exercises ===");
        System.out.println("---------------------------");
        for (int i = 0; i < exerciseList.size(); i++) {
            Exercise exercise = exerciseList.get(i);
            System.out.printf("[%d] %-25s (%s)\n", 
                    i, 
                    SharedUI.truncateString(exercise.getName(), 25),
                    exercise.exerciseType()
            );
        }
        System.out.println("---------------------------");
    }

    // EFFECTS: Displays a brief summary of the workout
    protected static void displayWorkoutSummary(WorkoutPlan workout) {
        System.out.println("\n=== Workout Summary ===");
        System.out.println("Name: " + workout.getName());
        System.out.println("Total Duration: " 
                    + formatDuration(Math.round(workout.getDuration())));
        displayWorkoutExercises(workout);
    }

    // EFFECTS: Format duration, given in seconds to a UI displayable string
    protected static String formatDuration(long seconds) {
        if (seconds == 0) {
            return "0 seconds";
        }
        return buildDurationString(seconds);
    }

    // EFFECTS: Display exercises in the workout
    protected static void displayWorkoutExercises(WorkoutPlan workout) {
        List<Exercise> exercises = workout.getExercises();
        System.out.println("\nExercises (" + exercises.size() + "):");
        System.out.println("---------------------------");
        // This is an example of where converting the set to a list becomes beneficial, 
        // both for the program's handling (assigning order and numerical, consecutive value)
        // and for the user's experience (selecting a number rather than a name)
        for (int i = 0; i < exercises.size(); i++) {
            Exercise exercise = exercises.get(i);
            System.out.printf("[%d] %-20s (%s)\n", 
                    i + 1, 
                    truncateString(exercise.getName(), 20),
                    exercise.exerciseType()
            );
        }
    }

    // EFFECTS: Display summary information about a workout (used by both ScheduleUI and Workout UIs)
    protected static void displayWorkoutDetails(WorkoutPlan workout) {
        if (workout instanceof Workout) {
            displayWorkoutSummary(workout);
        } else {
            System.out.println("\n=== Rest Day ===");
            System.out.println("No exercises to display.");
        }

        displayWorkoutOptions();
    }

    // HELPER: for displayWorkoutDetails
    // EFFECTS: Display workout management options menu
    private static void displayWorkoutOptions() {
        System.out.println("\nOptions:");
        System.out.println("[1] Delete Workout");
        System.out.println("[2] Back to Workout List");
    }


    // EFFECTS: Display exercise training type distribution with visualization
    protected static void displayTrainingDistribution(Map<String, Double> distribution, double total) {
        if (distribution.isEmpty()) {
            return; // Do not show the section if there is no data
        }

        // Displaying metrics in descending 'order' requires 'order'
        /* distribution.entrySet is a MAP. This map is is ExerciseAssociator metrics, for a given Equipment, but
         * MODIFIED to represent training duration metrics by EXERCISE TYPE. See the calculateTrainingDistribution
         * method below this method for how this works. EquipmentMetricsUI and MuscleMetricsUI use these methods. */
        List<Map.Entry<String, Double>> sortedTrainingMetricsDescendingOrder = 
                new ArrayList<Map.Entry<String, Double>>(distribution.entrySet());
        sortedTrainingMetricsDescendingOrder.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (Map.Entry<String, Double> entry : sortedTrainingMetricsDescendingOrder) {
            displayDistributionEntry(entry, total);
        }
    }

    // EFFECTS: Calculate the distribution of different training types from ExerciseAssociator's metrics
    protected static Map<String, Double> calculateTrainingDistribution(Map<String, Double> metrics) {
        Map<String, Double> distribution = new HashMap<String, Double>();
        double totalDuration = metrics.getOrDefault("totalDuration", 0.0);
        
        distribution.put("Strength", totalDuration 
                - metrics.getOrDefault("totalIntervalDuration", 0.0)
                - metrics.getOrDefault("totalEnduranceDuration", 0.0));
        distribution.put("Interval", metrics.getOrDefault("totalIntervalDuration", 0.0));
        distribution.put("Endurance", metrics.getOrDefault("totalEnduranceDuration", 0.0));
        
        return distribution;
    }

    // HELPER: for displayTrainingDistribution
    // EFFECTS: Display a single entry in the training distribution
    private static void displayDistributionEntry(Map.Entry<String, Double> entry, double total) {
        double percentage = (entry.getValue() / total) * 100;
        System.out.printf("%-12s: %s (%s - %.1f%%)\n",
                entry.getKey(),
                generateProgressBar(entry.getValue(), total),
                formatDuration(Math.round(entry.getValue())),
                percentage
        );
    }

    // EFFECTS: Process muscle group selection input and returns selected group or null if invalid
    protected static MuscleGroup processMuscleGroupSelection(String input) {
        try {
            int selection = Integer.parseInt(input);
            List<MuscleGroup> groupList = new ArrayList<MuscleGroup>(
                    predefinedData.getAllMuscleGroups().values());
            
            if (selection >= 0 && selection < groupList.size()) {
                return groupList.get(selection);
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
        return null;
    }  
}

