package model.workout;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import model.log.Event;
import model.log.EventLog;
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
        initializeSchedule();
        EventLog.getInstance().logEvent(new Event("WeeklySchedule initialized with default rest days"));
    }

    // MODIFIES: this, MuscleGroup, Equipment
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

    // MODIFIES: this, MuscleGroup, Equipment
    // EFFECTS: Remove the assigned workout or rest day for the given day, setting it to a rest day
    //          Throw IllegalArgumentException if dayIndex is not in range [0,6]
    //          A removed Workout subclass instance will have each of their Exercise metrics deactivated 
    public void clearScheduleForDay(int dayIndex) throws IllegalArgumentException {
        if (dayIndex < 0 || dayIndex > 6) {
            throw new IllegalArgumentException();
        }
        schedule[dayIndex].deactivateMetrics(DAYS[dayIndex]);
        schedule[dayIndex] = new RestDay("Rest Day");
        
        // Log schedule clearing--- ONLY used when a WorkoutPlan is deleted from WorkoutLibrary in the GUI
        EventLog.getInstance().logEvent(new Event("Schedule cleared for " + DAYS[dayIndex] + " (set to Rest Day)"));
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

    // HELPER: for WeeklySchedule, fromJson
    // EFFECTS: Initialize this WeeklySchedule with RestDays for each day in the schedule week
    private void initializeSchedule() {
        // Initialize with rest days
        for (int i = 0; i < 7; i++) {
            schedule[i] = new RestDay("Rest Day");
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        JSONArray scheduleArray = new JSONArray();

        for (int i = 0; i < DAYS.length; i++) {
            JSONObject dayJson = createDayJson(i);
            scheduleArray.put(dayJson);
        }
        json.put("schedule", scheduleArray);
        EventLog.getInstance().logEvent(new Event("WeeklySchedule serialized to JSON"));
        return json;
    }

    // HELPER: for toJson
    // EFFECTS: Create a JSON object for the given day index containing:
    //          1. The day index (0-6)
    //          2. The workout name (null if default RestDay)
    private JSONObject createDayJson(int dayIndex) {
        JSONObject dayJson = new JSONObject();
        WorkoutPlan plan = schedule[dayIndex];
        
        dayJson.put("day", dayIndex);
        dayJson.put("workoutName", plan.getName()); // WorkoutPlan objects have unique names (WorkoutLibrary enforces)
        
        return dayJson;
    }

    // REQUIRES: toJson's output is not modified to this program's persistence is not modified
    // MODIFIES: this
    // EFFECTS: Reoncstruct this WeeklySchedule using the WorkoutPlan name and the day index (0-6)
    // NOTE: The REQUIRES clause is necessary for fromJson to function correctly. However,  
    //       there is extensive error handling, exception throwing, and default value 
    //       employing for missing or corrupted fields/data structures. Ultimately, even if 
    //       the REQUIRES clause is not met, fromJson handles these issues with default or fallback
    //       states without exposing errors or exceptions to the user interface.
    //       Exceptions are handled at three levels: this, JsonManager, PersistenceUI
    @Override
    public void fromJson(JSONObject json, Object data) throws JSONException {
        if (!(data instanceof WorkoutLibrary)) {
            throw new IllegalArgumentException("WorkoutLibrary required for state reconstruction");
        }
        WorkoutLibrary workoutLibrary = (WorkoutLibrary) data;
        
        initializeSchedule();

        if (json == null || !json.has("schedule")) {
            return;
        }

        JSONArray scheduleArray = json.getJSONArray("schedule");
        reconstructSchedule(scheduleArray, workoutLibrary);
        EventLog.getInstance().logEvent(new Event("WeeklySchedule deserialized from JSON"));
    }

    // HELPER: for fromJson
    // MODIFIES: this
    // EFFECTS: Reconstruct schedule from JSON array using workouts from workoutLibrary
    //          Maintain default rest days for invalid or missing entries
    private void reconstructSchedule(JSONArray scheduleArray, WorkoutLibrary workoutLibrary) {
        for (int i = 0; i < scheduleArray.length(); i++) {
            try {
                JSONObject dayJson = scheduleArray.getJSONObject(i);
                reconstructDay(dayJson, workoutLibrary);
            } catch (JSONException e) {
                // Skip invalid entries (non-JSONObject elements)
                continue;
            }
        }
    }

    // HELPER: for reconstructSchedule
    // MODIFIES: this
    // EFFECTS: Add workout to schedule for the specified day if:
    //          1. Day index is valid (0-6)
    //          2. Workout name is not null
    //          3. Workout exists in workoutLibrary
    //          Otherwise, maintain default rest day
    private void reconstructDay(JSONObject dayJson, WorkoutLibrary workoutLibrary) {
        int day = getDayIndex(dayJson);
        if (!isValidDayIndex(day)) {
            return;
        }

        String workoutName = getWorkoutName(dayJson);
        if (workoutName == null) {
            return;
        }

        setWorkoutForDay(day, workoutName, workoutLibrary);
    }

    // HELPER: for reconstructDay
    // EFFECTS: Extract and return day index from JSON, or -1 if invalid
    private int getDayIndex(JSONObject dayJson) {
        try {
            return dayJson.getInt("day");
        } catch (JSONException e) {
            return -1;
        }
    }

    // HELPER: for reconstructDay
    // EFFECTS: Return true if day index is within valid range (0-6)
    private boolean isValidDayIndex(int day) {
        return day >= 0 && day < DAYS.length;
    }

    // HELPER: for reconstructDay
    // EFFECTS: Extract and return workout name from JSON, or null if missing or invalid
    private String getWorkoutName(JSONObject dayJson) {
        try {
            return dayJson.isNull("workoutName") ? null : dayJson.getString("workoutName");
        } catch (JSONException e) {
            return null;
        }
    }

    // HELPER: for reconstructDay
    // MODIFIES: this
    // EFFECTS: Set workout from workoutLibrary for the specified day
    //          Maintain default rest day if workout not found
    private void setWorkoutForDay(int day, String workoutName, WorkoutLibrary workoutLibrary) {
        try {
            WorkoutPlan workout = workoutLibrary.getWorkout(workoutName);
            if (workout != null) {
                setScheduleForDay(day, workout);
            }
        } catch (IllegalArgumentException e) {
            // Keep default RestdDay if workout not found
            // Already instantiated within `schedule`
            // setScheduleForDay(day, new RestDay("Rest Day")) may be called, but will have no relative side effects
        }
    }
}