package model.workout;

import java.util.ArrayList;
import java.util.List;

/**
 * REPRESENTS: a structured weekly plan containing workouts and rest days
 * 
 * USED BY:
 *      1. Users tracking their weekly workout regimen
 *      2. UI components displaying planned workouts
 * 
 * PURPOSE: Stores exactly 7 workout or rest slots, one per day of the week
 *          Allow workouts or rest days to be assigned, modified, or removed to a workout schedule
 * 
 * MUTABILITY: Mutable
 */
public class WeeklySchedule {

    // EFFECTS: Create a weekly schedule with a fixed list of 7 slots (one per day of the week)
    public WeeklySchedule() {
        // stub
    }

    // EFFECTS: Return workout or rest day assigned for the given day (0 = Monday, 6 = Sunday)
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    public WorkoutPlan getScheduleForDay(int dayIndex) throws IllegalArgumentException {
        return null; // stub
    }

    // MODIFIES: this
    // EFFECTS: Assign the given workout or rest day to the specified day (0 = Monday, 6 = Sunday)
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    //          Throw IllegalArgumentException if workoutPlan is null
    public void setScheduleForDay(int dayIndex, WorkoutPlan workoutPlan) throws IllegalArgumentException {
        return; // stub
    }

    // MODIFIES: this
    // EFFECTS: Remove the assigned workout or rest day for the given day, leaving it empty
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    public void clearScheduleForDay(int dayIndex) throws IllegalArgumentException {
        return; // stub
    }

    // EFFECTS: Return list of all workouts and rest days assigned to each day of the week
    public List<WorkoutPlan> getWeeklySchedule() {
        return new ArrayList<WorkoutPlan>(); // stub
    }

    // EFFECTS: Return a formatted string summary of this week's plan
    public String getWeekSummary() {
        return ""; // stub
    }
}
