package ui.components;

import model.workout.RestDay;
import model.workout.Workout;
import model.workout.WorkoutPlan;
import ui.components.SharedUI.WeeklyStats;

import static ui.components.SharedUI.*;

import java.util.List;

/** This class is for scheduling WorkoutPlan instance objects a user creates into any day of a weekly calendar. 
 * By doing so, they not only register that WorkoutPlan to their WeeklySchedule, but they also activate metrics
 * for that WorkoutPlan---whichever Equipment, MuscleGroup -> Muscle are a part of the WorkoutPlan's exercises
 * will have detailed metrics that the user can see and visualize in the muscle metrics (MuscleMetricsUI) and
 * equipment metrics (EquipmentMetricsUI) parts of this program's interface.
*/
public class ScheduleUI {

    // EFFECTS: Manage user interaction with the weekly schedule
    //          Display the current schedule, present available 
    //          interface usage options, and process user command(s) 
    //          to view, modify, or exit workout scheduling management
    // REMINDER: While loop(s) allow for the user to navigate back at any stage of the interface
    public void editWeeklySchedule() {
        while (true) {
            clearScreen();
            displayWeeklySchedule();
            displayScheduleOptions();
            
            String command = input.nextLine().trim();
            if (!processScheduleCommand(command)) {
                break;
            }
        }
    }

    // HELPER: for editWeeklySchedule
    // EFFECTS: Display the current weekly schedule
    private void displayWeeklySchedule() {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", 
                "Friday", "Saturday", "Sunday"};
        
        System.out.println("\n=============== Weekly Schedule ===============");
        System.out.println("============================================");
        
        for (int i = 0; i < 7; i++) {
            displayScheduleDay(i, days[i]);
        }
        
        System.out.println("============================================");
    }

    // HELPER: for displayWeeklySchedule
    // REQUIRES: dayName != null, 0 <= dayIndex <= 6
    // EFFECTS: Display information for a specific day in the schedule
    private void displayScheduleDay(int dayIndex, String dayName) {
        WorkoutPlan plan = weeklySchedule.getScheduleForDay(dayIndex);
        String status = getScheduleDayStatus(plan);
        String duration;
        if (plan == null) {
            duration = "";
        } else {
            duration = formatDurationString(plan.getDuration());
        }
        
        System.out.printf("[%d] %-10s: %-30s%s\n", 
                dayIndex + 1, 
                dayName, 
                status,
                duration);
    }

    // HELPER: for displayScheduleDay
    // EFFECTS: Return the status string for a schedule day
    private String getScheduleDayStatus(WorkoutPlan plan) {
        if (plan == null) {
            return "Empty";
        }
        if (plan instanceof RestDay) {
            return "Rest Day";
        } else {
            return truncateString(plan.getName(), 30);
        }
    }

    // HELPER: for displayScheduleDay
    // EFFECTS: Format duration as a string if positive
    private String formatDurationString(double duration) {
        if (duration > 0) {
            return " (" + formatDuration(Math.round(duration)) + ")";
        } else {
            return "";
        }
    }

    // HELPER: for editWeeklySchedule
    // EFFECTS: Display schedule management options
    private void displayScheduleOptions() {
        System.out.println("\nSchedule Management Options:");
        System.out.println("[1] View Day Details");
        System.out.println("[2] Edit Schedule");
        System.out.println("[3] View Week Summary");
        System.out.println("[4] Back to Main Menu");
        System.out.print("\nSelect option: ");
    }

    // HELPER: for editWeeklySchedule
    // REQUIRES: command != null
    // EFFECTS: Process schedule management commands and return false if user chooses to exit
    private boolean processScheduleCommand(String command) {
        switch (command) {
            case "1":
                viewDayDetails(); // day-choosing and detail-viewing interface
                return true;
            case "2":
                editScheduleDay(); // edit schedule interface
                return true;
            case "3":
                displayWeekSummary(); // show summary
                waitForEnter(); // user exits from summary
                return true;
            case "4":
                return false;
            default:
                System.out.println("Invalid selection.");
                waitForEnter();
                return true;
        }
    }

    // HELPER: for processScheduleCommand (case "1")
    // EFFECTS: View detailed information for a selected day
    private void viewDayDetails() {
        clearScreen();
        displayWeeklySchedule(); // Better UI flow
        System.out.print("\nSelect day to view (1-7): ");
        int day = readDaySelection();
        
        if (day != -1) {
            displayDayDetails(day - 1);
        } 
        // Will go back to main menu if the user exits the displayDayDetails interface or they select an invalid day
    }

    // HELPER: for viewDayDetails and editScheduleDay
    // EFFECTS: Read and validate day selection from user input and return day number, or -1 if invalid
    private int readDaySelection() {
        // DAY SAFEGUARD
        // WeeklySchedule methods WILL behave as intended if day != between 1-7. However, to minimize catching
        // IllegalArgumentException's, safeguard day inputs at the UI level as well
        try {
            int day = Integer.parseInt(input.nextLine().trim());
            if (day >= 1 && day <= 7) {
                return day;
            }
            System.out.println(INVALID_INPUT + "Please enter a number between 1 and 7.");
        } catch (NumberFormatException e) { // User did NOT give a number
            System.out.println(INVALID_INPUT);
        }
        waitForEnter();
        return -1;
    }

    // HELPER: for viewDayDetails
    // REQUIRES: 0 <= dayIndex <= 6
    // EFFECTS: Display detailed information for a specific day
    private void displayDayDetails(int dayIndex) {
        WorkoutPlan plan = weeklySchedule.getScheduleForDay(dayIndex);
        
        System.out.println("\n=== Schedule Details for " + getDayName(dayIndex) + " ===");
        
        if (plan == null) {
            System.out.println("No workout or rest day scheduled.");
        } else if (plan instanceof RestDay) {
            System.out.println("Rest Day");
        } else {
            displayWorkoutDetails(plan);
        }
        waitForEnter();
    }

    // HELPER: for processScheduleCommand (case "2")
    // EFFECTS: Handle editing of a specific day in the schedule
    private void editScheduleDay() {
        clearScreen();
        displayWeeklySchedule();
        System.out.print("\nSelect day to edit (1-7): ");
        int day = readDaySelection();
        
        if (day != -1) {
            modifyScheduleDay(day - 1);
        }
    }

    // HELPER: for editScheduleDay
    // REQUIRES: 0 <= dayIndex <= 6
    // EFFECTS: Modify a specific day in the schedule
    private void modifyScheduleDay(int dayIndex) {
        while (true) {
            displayModificationOptions(dayIndex);
            String command = input.nextLine().trim();
            
            if (!processModificationCommand(command, dayIndex)) {
                break;
            }
        }
    }

    // HELPER: for modifyScheduleDay
    // REQUIRES: 0 <= dayIndex <= 6
    // EFFECTS: Display modification options for a schedule day
    private void displayModificationOptions(int dayIndex) {
        String dayName = getDayName(dayIndex);
        WorkoutPlan currentPlan = weeklySchedule.getScheduleForDay(dayIndex);
        
        System.out.println("\n=== Editing Schedule for " + dayName + " ===");
        System.out.println("Current: " + (currentPlan != null ? currentPlan.getName() : "Empty"));

        System.out.println("\nOptions:");
        System.out.println("[1] Add Workout");
        System.out.println("[2] Add Rest Day");
        System.out.println("[3] Back");
    }

    // HELPER: for modifyScheduleDay
    // REQUIRES: command != null, 0 <= dayIndex <= 6
    // EFFECTS: Process modification commands and return false if user chooses to exit
    private boolean processModificationCommand(String command, int dayIndex) {
        switch (command) {
            case "1":
                addWorkoutToDay(dayIndex);
                return false;
            case "2":
                clearScheduleDay(dayIndex);
                return false;
            case "3":
                return false;
            default:
                System.out.println("Invalid selection.");
                waitForEnter();
                return true;
        }
    }

    // HELPER: for processModificationCommand (case "1")
    // REQUIRES: 0 <= dayIndex <= 6
    // MODIFIES: Workout, WeeklySchedule (should WorkoutPlan be added to WeeklySchedule)
    // EFFECTS: Add a workout to a specific day in the schedule
    private void addWorkoutToDay(int dayIndex) {
        List<WorkoutPlan> workouts = workoutLibrary.getAllWorkouts();
        if (workouts.isEmpty()) {
            System.out.println("\nNo workouts available. Please create a workout first.");
            waitForEnter();
            return;
        }

        displayWorkoutSelectionMenu(workouts);
        processWorkoutSelection(dayIndex, workouts);
    }

    // HELPER: for addWorkoutToDay
    // REQUIRES: workouts != null
    // EFFECTS: Display the workout selection menu
    private void displayWorkoutSelectionMenu(List<WorkoutPlan> workouts) {
        System.out.println("\n=== Available Workouts ===");
        for (int i = 0; i < workouts.size(); i++) {
            WorkoutPlan workout = workouts.get(i);
            System.out.printf("[%d] %-20s (%s)\n", 
                    i, 
                    truncateString(workout.getName(), 20),
                    formatDuration(Math.round(workout.getDuration()))
            );
        }
    }

    // HELPER: for addWorkoutToDay
    // REQUIRES: workouts != null, WorkoutPlan instances in workouts are not null, input != null, 0 <= dayIndex <= 6
    // EFFECTS: Process WorkoutPlan selection for a day in the schedule
    private void processWorkoutSelection(int dayIndex, List<WorkoutPlan> workouts) {
        System.out.print("\nSelect workout to add " + BACK_OPTION + ": ");
        String userInput = input.nextLine().trim();
        
        if (!userInput.equalsIgnoreCase("b")) {
            handleWorkoutAddition(dayIndex, workouts, userInput);
        }
    }

    // HELPER: for processWorkoutSelection
    // REQUIRES: workouts != null, input != null, 0 <= dayIndex <= 6
    // MODIFIES: Workout, WeeklySchedule
    // EFFECTS: Handle the addition of a workout to the schedule
    // NOTE: By adding a Workout (instance of WorkoutPlan) to WeeklySchedule, it WILL MODIFY 
    //       Workout -> Exercise -> Equipment and Muscle
    private void handleWorkoutAddition(int dayIndex, List<WorkoutPlan> workouts, String input) {
        try {
            int selection = Integer.parseInt(input);
            if (selection >= 0 && selection < workouts.size()) { // give updateScheduleDay valid WeeklySchedule inputs
                updateScheduleDay(dayIndex, workouts.get(selection)); // Should NOT throw IllegalArgumentException
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
        }
        waitForEnter(); // not included in updateScheduleDay, instead here given different possible cases
    }

    // HELPER: for handleWorkoutAddition
    // REQUIRES: 0 <= dayIndex <= 6, newPlan != null
    // MODIFIES: Workout, WeeklySchedule
    // EFFECTS: Update a day in the schedule with a new WorkoutPlan
    private void updateScheduleDay(int dayIndex, WorkoutPlan newPlan) {
        weeklySchedule.setScheduleForDay(dayIndex, newPlan);
        System.out.println("Workout added successfully!");
    }

    // HELPER: for processModificationCommand (case "2")
    // REQUIRES: 0 <= dayIndex <= 6
    // MODIFIES: Workout, WeeklySchedule
    // EFFECTS: Clear a day in the schedule
    private void clearScheduleDay(int dayIndex) { 
        weeklySchedule.clearScheduleForDay(dayIndex); // IllegalArgumentException if invalid, but UI safeguards
        System.out.println("Day cleared successfully!");
        waitForEnter(); // included here, program will not give this method an 'invalid' dayIndex
    }

    // HELPER: for displayDayDetails and displayModificationOptions
    // REQUIRES: 0 <= dayIndex <= 6
    // EFFECTS: Return the name of the day for a given index
    private String getDayName(int dayIndex) {
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", 
                "Friday", "Saturday", "Sunday"};
        return days[dayIndex];
    }

    // HELPER: for processScheduleCommand (case "3")
    // EFFECTS: Display a summary of the current week's schedule
    private void displayWeekSummary() {
        clearScreen();
        System.out.println("\n=== Weekly Schedule Summary ===");
        WeeklyStats stats = calculateWeeklyStats();
        
        System.out.println("Total Workouts: " + stats.totalWorkouts);
        System.out.println("Rest Days: " + stats.restDays);
        System.out.println("Empty Days: " + (7 - stats.totalWorkouts - stats.restDays));
        System.out.println("Total Weekly Duration: " + formatDuration(Math.round(stats.totalDuration)));
    }

    // HELPER: for displayWeekSummary
    // EFFECTS: Calculate weekly schedule statistics and return a WeeklyStats object
    private WeeklyStats calculateWeeklyStats() {
        WeeklyStats stats = new WeeklyStats();
        for (int i = 0; i < 7; i++) {
            updateWeeklyStats(stats, weeklySchedule.getScheduleForDay(i));
        }
        return stats;
    }

    // HELPER: for calculateWeeklyStats
    // REQUIRES: stats != null
    // MODIFIES: WeeklyStats
    // EFFECTS: Iterator that updates weekly stats based on a WorkoutPlan in WeeklySchedule
    private void updateWeeklyStats(WeeklyStats stats, WorkoutPlan plan) {
        if (plan instanceof Workout) {
            stats.totalWorkouts++;
            stats.totalDuration += plan.getDuration();
        } else if (plan instanceof RestDay) {
            stats.restDays++;
        }
    }
}
