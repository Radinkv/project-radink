package model.workout;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import persistence.Writable;

/**
 * REPRESENTS: a weekly plan containing workouts and rest days
 * 
 * USED BY:
 *      1. Users tracking their weekly workout regimen
 *      2. UI components displaying planned workouts
 * 
 * PURPOSE: Store exactly 7 workout or rest slots, one per day of the week
 *          Allow workouts or rest days to be assigned, modified, or removed to a workout schedule
 * 
 * MUTABILITY: Mutable
 */
public class WeeklySchedule implements Writable {
    private WorkoutPlan[] schedule;
    private static final String[] DAYS = {
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    };

    // EFFECTS: Create a weekly schedule with a fixed array of 7 slots (one per day of the week)
    public WeeklySchedule() {
        schedule = new WorkoutPlan[7];
        // Initialize with rest days
        for (int i = 0; i < 7; i++) {
            schedule[i] = new RestDay("Rest Day");
        }
    }

    // MODIFIES: this
    // EFFECTS: Assign the given workout or rest day to the specified day (0 = Monday, 6 = Sunday)
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    //          Throw IllegalArgumentException if workoutPlan is null
    //          An added Workout subclass instance will have each of their Exercise metrics activated 
    public void setScheduleForDay(int dayIndex, WorkoutPlan workoutPlan) throws IllegalArgumentException {
        if (dayIndex < 0 || dayIndex > 6 || workoutPlan == null) {
            throw new IllegalArgumentException();
        }

        // Bug for WorkoutPlan being re-assigned to a specific date; metrics should NOT cumulate
        schedule[dayIndex].deactivateMetrics(DAYS[dayIndex]);

        schedule[dayIndex] = workoutPlan;
        workoutPlan.activateMetrics(DAYS[dayIndex]);
    }

    // MODIFIES: this
    // EFFECTS: Remove the assigned workout or rest day for the given day, setting it to a rest day
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    //          A removed Workout subclass instance will have each of their Exercise metrics deactivated 
    public void clearScheduleForDay(int dayIndex) throws IllegalArgumentException {
        if (dayIndex < 0 || dayIndex > 6) {
            throw new IllegalArgumentException();
        }
        schedule[dayIndex].deactivateMetrics(DAYS[dayIndex]);
        schedule[dayIndex] = new RestDay("Rest Day");
    }

    // EFFECTS: Return list of all workouts and rest days assigned to each day of the week
    public List<WorkoutPlan> getWeeklySchedule() {
        List<WorkoutPlan> scheduleList = new ArrayList<WorkoutPlan>();
        for (WorkoutPlan plan : schedule) {
            scheduleList.add(plan);
        }
        return scheduleList;
    }

    // EFFECTS: Return the WorkoutPlan at this WeeklySchedule's dayIndex
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    public WorkoutPlan getScheduleForDay(int dayIndex) {
        if (dayIndex < 0 || dayIndex > 6) {
            throw new IllegalArgumentException();
        }
        return schedule[dayIndex];
    }

    // EFFECTS: Return a formatted string summary of this week's plan
    public String getWeekSummary() {
        StringBuilder summary = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            summary.append(DAYS[i])
                    .append(": ")
                    .append(schedule[i].getName())
                    .append("\n")
                    .append(schedule[i].getWorkoutSummary());
        }
        return summary.toString();
    }

    // EFFECTS: Return a JSON representation of this WeeklySchedule containing
    //          the complete state of the weekly schedule
    @Override
    public JSONObject toJson() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toJson'");
    }

    // MODIFIES: this
    // EFFECTS: Reconstruct this WeeklySchedule's state from the provided JSON data
    //          Reactivate all exercise metrics for assigned workouts
    //          Throws JSONException if data is invalid or incomplete
    @Override
    public void fromJson(JSONObject json) throws JSONException {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromJson'");
    }
}