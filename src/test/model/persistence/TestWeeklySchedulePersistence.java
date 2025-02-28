package model.persistence;

import static org.junit.jupiter.api.Assertions.*;
import static utility.Utility.TEST_PRECISION;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.association.ExerciseAssociator;
import model.equipment.*;
import model.equipment.strength.Dumbbell;
import model.equipment.cardio.Treadmill;
import model.exercise.*;
import model.muscle.*;
import model.workout.*;

/**
 * This class tests the JSON persistence capabilities of WeeklySchedule, which includes:
 * 1. Saving complete workout schedules to JSON format
 * 2. Loading workout schedules from valid JSON representations
 * 3. Handling edge cases and invalid JSON inputs
 * 4. Verifying that exercise metrics are properly preserved across save/load operations
 * 5. Verifying Workout objects (which contain Exercise objects) are correctly reconstructed
 * 
 * This test suite assumes WorkoutLibrary's persistence implementation works as specified
 */
public class TestWeeklySchedulePersistence {
    private WeeklySchedule schedule;
    private WorkoutLibrary workoutLibrary;
    private ExerciseLibrary exerciseLibrary;

    private Exercise benchPress;
    private Exercise running;
    private Workout strengthWorkout;
    private Workout cardioWorkout;
    private RestDay restDay;

    private Equipment dumbbell;
    private Equipment treadmill;

    private MuscleGroup chest;
    private MuscleGroup legs;

    private JSONObject validScheduleJson;
    
    @BeforeEach
    void runBefore() {
        exerciseLibrary = new ExerciseLibrary();
        workoutLibrary = new WorkoutLibrary();
        schedule = new WeeklySchedule();
        setupTestData();
    }

    private void setupTestData() {
        setupEquipmentAndMuscleGroups();
        setupExercises();
        setupWorkouts();
        setupValidJson();
    }

    private void setupEquipmentAndMuscleGroups() {
        dumbbell = new Dumbbell();
        treadmill = new Treadmill();
        
        chest = new MuscleGroup("Chest", List.of(new Muscle("Pectorals"))); // List.of for immutable list
        legs = new MuscleGroup("Legs", List.of(new Muscle("Quadriceps")));
    }

    private void setupExercises() {
        benchPress = new StrengthExercise("Bench Press", 3, 12, 2.0, 1.5, dumbbell, chest);
        running = new EnduranceExercise("Running", 30.0, treadmill, legs);
        
        exerciseLibrary.addExercise(benchPress);
        exerciseLibrary.addExercise(running);
    }

    private void setupWorkouts() {
        strengthWorkout = new Workout("Strength Day", Arrays.asList(benchPress));
        cardioWorkout = new Workout("Cardio Day", Arrays.asList(running));
        restDay = new RestDay("Rest Day");
        
        workoutLibrary.addWorkout(strengthWorkout);
        workoutLibrary.addWorkout(cardioWorkout);
        workoutLibrary.addWorkout(restDay);
    }

    private void setupValidJson() {
        validScheduleJson = new JSONObject()
            .put("schedule", new JSONArray()
                .put(createDayJson(0, "Strength Day"))
                .put(createDayJson(1, "Cardio Day"))
                .put(createDayJson(2, "Rest Day")) // Explicit RestDay
                .put(createDayJson(3, null)) // WeeklySchedule sees null as an implicit RestDay
                .put(createDayJson(4, null)) 
                .put(createDayJson(5, null))
                .put(createDayJson(6, null)));
    }

    // REQUIRES: 0 <= day <= 6 workoutName != null
    // EFFECTS: Create the JSON equivalent of one day, exactly how WeeklySchedule toJson and fromJson
    //          is supposed to represent and expect
    private JSONObject createDayJson(int day, String workoutName) {
        return new JSONObject()
            .put("day", day)
            .put("workoutName", workoutName);
    }

    // REQUIRES: original (WeeklySchedule) != null
    // EFFECTS: Get data from original WeeklySchedule toJson, load it onto another WeeklySchedule,
    //          and return the new WeeklySchedule
    private WeeklySchedule saveAndLoad(WeeklySchedule original) {
        JSONObject json = original.toJson();
        WeeklySchedule loaded = new WeeklySchedule();
        loaded.fromJson(json, workoutLibrary);
        return loaded;
    }

    // REQUIRES: schedule, expectedWorkoutName, expectedType, expectedExerciseNames != null, 0 <= day <= 6
    // EFFECTS: Check that a schedule day contains the expected workout with the correct type, name, and exercises
    private void verifyScheduleDay(WeeklySchedule schedule, int day, 
            String expectedWorkoutName, Class<?> expectedType, List<String> expectedExerciseNames) {
        WorkoutPlan plan = schedule.getScheduleForDay(day);
        assertNotNull(plan);
        assertEquals(expectedWorkoutName, plan.getName());
        assertTrue(expectedType.isInstance(plan));
        
        // Check and verify exercises for Workout instances
        if (expectedType == Workout.class) {
            verifyWorkoutExercises(plan, expectedExerciseNames);
        } else if (expectedType == RestDay.class) {
            // RestDay should have empty exercise list
            assertTrue(plan.getExercises().isEmpty());
        }
    }

    // REQUIRES: plan and expectedExerciseNames != null
    // EFFECTS: Check that a workout contains exactly the expected exercises
    private void verifyWorkoutExercises(WorkoutPlan plan, List<String> expectedExerciseNames) {
        if (!(plan instanceof Workout)) {
            fail("Expected Workout instance but found " + plan.getClass().getSimpleName());
        }
        
        WorkoutPlan workout = plan;
        List<Exercise> exercises = workout.getExercises();
        
        // Confirm exercise count matches expected
        assertEquals(expectedExerciseNames.size(), exercises.size());
        
        // Check each exercise has the correct name and properties
        for (int i = 0; i < expectedExerciseNames.size(); i++) {
            Exercise exercise = exercises.get(i);
            assertNotNull(exercise);
            assertEquals(expectedExerciseNames.get(i), exercise.getName());
            
            // Ensure exercise has proper equipment and muscle group
            assertNotNull(exercise.getRequiredEquipment());
            assertNotNull(exercise.getMusclesTargeted());
        }
    }

    // REQUIRES: plan and expectedMetrics != null
    // EFFECTS: Verify that workout metrics match expected values 
    private void verifyMetrics(WorkoutPlan plan, Map<String, Double> expectedMetrics) {
        Map<String, Double> actualMetrics = plan.getWorkoutSummary();
        
        assertEquals(expectedMetrics.keySet(), actualMetrics.keySet());
                
        for (String key : expectedMetrics.keySet()) {
            assertEquals(expectedMetrics.get(key), actualMetrics.get(key), TEST_PRECISION);
        }
    }

    // REQUIRES: associator and expectedValues != null
    // EFFECTS: Verify metrics stored in equipment and muscle groups match expected values
    private void verifyAssociatorMetrics(ExerciseAssociator associator, Map<String, Double> expectedValues) {
        Map<String, Double> actualMetrics = associator.getAggregatedExerciseMetrics();
        
        for (String key : expectedValues.keySet()) {
            assertEquals(expectedValues.get(key), actualMetrics.get(key), TEST_PRECISION);
        }
    }

    @Test
    void testNullWorkoutName() {
        JSONObject malformed = new JSONObject()
                .put("schedule", new JSONArray()
                .put(new JSONObject()
                    .put("day", 0)
                    .put("workoutName", JSONObject.NULL)));
                    
        schedule.fromJson(malformed, workoutLibrary);
        
        // Confirm day defaulted to RestDay with null workout name
        verifyScheduleDay(schedule, 0, "Rest Day", RestDay.class, List.of());
        
        // Check metrics are empty for RestDay
        Map<String, Double> metrics = schedule.getScheduleForDay(0).getWorkoutSummary();
        assertTrue(metrics.isEmpty());
    }

    @Test
    void testMissingWorkoutName() {
        JSONObject malformed = new JSONObject()
                .put("schedule", new JSONArray()
                .put(new JSONObject()
                    .put("day", 0)));
                    
        schedule.fromJson(malformed, workoutLibrary);
        
        // Confirm day defaulted to RestDay when workout name field is missing
        verifyScheduleDay(schedule, 0, "Rest Day", RestDay.class, List.of());
        
        // Ensure RestDay has zero duration
        assertEquals(0.0, schedule.getScheduleForDay(0).getDuration(), TEST_PRECISION);
    }

    @Test
    void testInvalidJsonStructure() {
        // Test case for JSON array containing non-JSONObject element
        JSONObject malformed = new JSONObject()
                .put("schedule", new JSONArray()
                .put("invalid"));
                
        schedule.fromJson(malformed, workoutLibrary);
        
        // Examine if all days default to RestDay with invalid JSON structure
        for (int i = 0; i < 7; i++) {
            verifyScheduleDay(schedule, i, "Rest Day", RestDay.class, List.of());
        }
        
        // Inspect the weekly summary content
        String summary = schedule.getWeekSummary();
        assertTrue(summary.contains("Monday: Rest Day"));
        assertTrue(summary.contains("Sunday: Rest Day"));
    }

    @Test
    void testNullAndEmptyJson() {
        WeeklySchedule testSchedule = new WeeklySchedule();
        
        // fromJson given null
        try {
            testSchedule.fromJson(null, workoutLibrary);
            
            // Confirm default rest days remain intact with null JSON
            for (int i = 0; i < 7; i++) {
                verifyScheduleDay(testSchedule, i, "Rest Day", RestDay.class, List.of());
            }
        } catch (Exception e) {
            fail("Should not throw exception for null JSON: " + e.getMessage());
        }
        
        // fromJson given empty JSON object
        try {
            testSchedule.fromJson(new JSONObject(), workoutLibrary);
            
            // Validate default rest days remain intact with empty JSON
            for (int i = 0; i < 7; i++) {
                verifyScheduleDay(testSchedule, i, "Rest Day", RestDay.class, List.of());
                assertEquals(0.0, testSchedule.getScheduleForDay(i).getDuration(), TEST_PRECISION);
            }
        } catch (Exception e) {
            fail("Should not throw exception for empty JSON: " + e.getMessage());
        }
        
        // Check if schedule entirely consists of RestDay objects
        List<WorkoutPlan> weeklySchedule = testSchedule.getWeeklySchedule();
        assertEquals(7, weeklySchedule.size());
        for (WorkoutPlan plan : weeklySchedule) {
            assertTrue(plan instanceof RestDay);
            assertEquals("Rest Day", plan.getName());
        }
    }
    
    @Test
    void testInvalidWorkoutLibrary() {
        JSONObject json = validScheduleJson;
        
        // null WorkoutLibrary
        try {
            schedule.fromJson(json, null);
            fail("Expected IllegalArgumentException for null WorkoutLibrary");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("WorkoutLibrary required"));
        }
        
        // Invalid data object type (not WorkoutLibrary)
        try {
            schedule.fromJson(json, "invalid data type");
            fail("Expected IllegalArgumentException for invalid reconstruction helper object type");
        } catch (IllegalArgumentException e) {
            // Expected exception
            assertTrue(e.getMessage().contains("WorkoutLibrary required"));
        }
    }

    @Test
    void testNonStringInvalidWorkoutNames() {
        JSONObject malformed = new JSONObject()
                .put("schedule", new JSONArray()
                        .put(new JSONObject() // Number as workoutName
                                .put("day", 0)
                                .put("workoutName", 123))
                        .put(new JSONObject() // Boolean as workoutName
                                .put("day", 1)
                                .put("workoutName", true))
                        .put(new JSONObject() // JSONObject as workoutName
                                .put("day", 2)
                                .put("workoutName", new JSONObject())));
                        
        schedule.fromJson(malformed, workoutLibrary);
        
        // Analyze how schedule handles invalid workout name types
        for (int i = 0; i < 7; i++) {
            verifyScheduleDay(schedule, i, "Rest Day", RestDay.class, List.of());
            
            // Check rest days have no exercises
            assertTrue(schedule.getScheduleForDay(i).getExercises().isEmpty());
            
            // Confirm no metrics for rest days
            assertTrue(schedule.getScheduleForDay(i).getWorkoutSummary().isEmpty());
        }
    }

    @Test
    void testBasicSchedulePersistence() {
        // Set up initial schedule with explicit workouts and rest day
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(1, cardioWorkout);
        schedule.setScheduleForDay(2, restDay);
        
        // Save and load
        WeeklySchedule loaded = saveAndLoad(schedule);
        
        // Evaluate loaded workout types, names and exercises
        verifyScheduleDay(loaded, 0, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(loaded, 1, "Cardio Day", Workout.class, List.of("Running"));
        verifyScheduleDay(loaded, 2, "Rest Day", RestDay.class, List.of());
        
        // Analyze restored exercise properties
        Exercise loadedBenchPress = loaded.getScheduleForDay(0).getExercises().get(0);
        assertEquals("Bench Press", loadedBenchPress.getName());
        assertEquals("Strength", loadedBenchPress.exerciseType());
        
        Exercise loadedRunning = loaded.getScheduleForDay(1).getExercises().get(0);
        assertEquals("Running", loadedRunning.getName());
        assertEquals("Endurance", loadedRunning.exerciseType());
        
        // Check default rest days for remaining days
        for (int i = 3; i < 7; i++) {
            verifyScheduleDay(loaded, i, "Rest Day", RestDay.class, List.of());
        }
        
        // Confirm workout durations were properly maintained
        assertEquals(strengthWorkout.getDuration(), loaded.getScheduleForDay(0).getDuration(), TEST_PRECISION);
        assertEquals(cardioWorkout.getDuration(), loaded.getScheduleForDay(1).getDuration(), TEST_PRECISION);
        assertEquals(restDay.getDuration(), loaded.getScheduleForDay(2).getDuration(), TEST_PRECISION);
    }

    @Test
    void testMetricsPersistence() {
        // Set up schedule with workouts
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(1, cardioWorkout);
        
        // Calculate expected metrics before save/load
        Map<String, Double> strengthMetrics = strengthWorkout.getWorkoutSummary();
        Map<String, Double> cardioMetrics = cardioWorkout.getWorkoutSummary();
        
        // Save and load
        WeeklySchedule loaded = saveAndLoad(schedule);
        
        // Compare metrics for strength workout before and after loading
        verifyMetrics(loaded.getScheduleForDay(0), strengthMetrics);
        
        // Same for strength workout
        verifyMetrics(loaded.getScheduleForDay(1), cardioMetrics);
        
        // Inspect exercise reconstruction details
        WorkoutPlan loadedStrengthWorkout = loaded.getScheduleForDay(0);
        Exercise loadedBenchPress = loadedStrengthWorkout.getExercises().get(0);
        
        assertNotNull(loadedBenchPress.getRequiredEquipment());
        assertEquals(dumbbell.getClass(), loadedBenchPress.getRequiredEquipment().getClass());
        
        assertNotNull(loadedBenchPress.getMusclesTargeted());
        assertEquals(chest.getName(), loadedBenchPress.getMusclesTargeted().getName());
        
        // Check if exercise-specific metrics were retained
        Map<String, Double> benchPressInfo = loadedBenchPress.getInfo();
        assertEquals(3.0, benchPressInfo.get("sets"), TEST_PRECISION);
        assertEquals(12.0, benchPressInfo.get("reps"), TEST_PRECISION);
        assertEquals(2.0, benchPressInfo.get("timePerRep"), TEST_PRECISION);
        assertEquals(1.5, benchPressInfo.get("restTime"), TEST_PRECISION);
    }

    @Test
    void testMalformedScheduleData() {
        // Test case: JSON contains various invalid day data
        JSONObject malformed = new JSONObject()
                .put("schedule", new JSONArray()
                .put(new JSONObject() // Missing day index
                    .put("workoutName", "Strength Day"))
                .put(new JSONObject() // Invalid day index (out of range)
                    .put("day", 7)
                    .put("workoutName", "Cardio Day"))
                .put(new JSONObject() // Non-existent workout
                    .put("day", 2)
                    .put("workoutName", "NonExistent")));
                    
        WeeklySchedule testSchedule = new WeeklySchedule();
        testSchedule.fromJson(malformed, workoutLibrary);
        
        // Test handling of malformed schedule data
        for (int i = 0; i < 7; i++) {
            verifyScheduleDay(testSchedule, i, "Rest Day", RestDay.class, List.of());
            
            // Validate rest day properties
            assertEquals(0.0, testSchedule.getScheduleForDay(i).getDuration(), TEST_PRECISION);
            assertTrue(testSchedule.getScheduleForDay(i).getWorkoutSummary().isEmpty());
        }
    }


    /**
     * IMPORTANT NOTE:
     * The following tests verify WeeklySchedule's proper handling of Workout and Exercise metric propagation
     * during JSON serialization (implicit saving) and deserialization (loading). These tests validate the 
     * implementation of WeeklySchedule's fromJson and toJson methods by thoroughly examining:
     * 
     *   1. Individual propagated metrics in MuscleGroup objects
     *   2. Collective metrics in Equipment objects
     *   3. Aggregate metrics in Workout objects
     * 
     * These test in this test suite uses multiple Workout objects containing exercises with distinct, metric values. 
     * This approach makes sure that any flawed implementation of the JSON persistence methods would be highly unlikely 
     * to pass all exception and basic loading tests without strong attention to detail.
     */
    @Test
    void testCompleteScheduleRoundTrip() {
        // Set up a complex schedule with multiple workouts
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(1, cardioWorkout);
        schedule.setScheduleForDay(2, restDay);
        schedule.setScheduleForDay(3, strengthWorkout);
        schedule.setScheduleForDay(4, cardioWorkout);
        
        // Save and load
        WeeklySchedule loaded = saveAndLoad(schedule);
        
        // Compare original and loaded state for each day
        for (int i = 0; i < 7; i++) {
            WorkoutPlan original = schedule.getScheduleForDay(i);
            WorkoutPlan loadedPlan = loaded.getScheduleForDay(i);
            
            // Review basic properties
            assertEquals(original.getName(), loadedPlan.getName());
            assertEquals(original.getClass(), loadedPlan.getClass());
            assertEquals(original.getDuration(), loadedPlan.getDuration(), TEST_PRECISION);
            
            // Compare workout metrics
            Map<String, Double> originalMetrics = original.getWorkoutSummary();
            Map<String, Double> loadedMetrics = loadedPlan.getWorkoutSummary();
            assertEquals(originalMetrics.keySet(), loadedMetrics.keySet());
            
            for (String key : originalMetrics.keySet()) {
                assertEquals(originalMetrics.get(key), loadedMetrics.get(key), TEST_PRECISION);
            }
            
            // For Workout instances, analyze exercises and their properties
            if (original instanceof Workout) {
                List<Exercise> originalExercises = original.getExercises();
                List<Exercise> loadedExercises = loadedPlan.getExercises();
                
                assertEquals(originalExercises.size(), loadedExercises.size());
                
                // Same object references due to PredefinedData
                for (int j = 0; j < originalExercises.size(); j++) {
                    Exercise originalExercise = originalExercises.get(j);
                    Exercise loadedExercise = loadedExercises.get(j);
                    
                    assertEquals(originalExercise.getName(), loadedExercise.getName());
                    assertEquals(originalExercise.exerciseType(), loadedExercise.exerciseType());
                    assertEquals(originalExercise.getDuration(), loadedExercise.getDuration(), TEST_PRECISION);
                    
                    // Confirm equipment and muscle groups were properly restored
                    assertNotNull(loadedExercise.getRequiredEquipment());
                    assertNotNull(loadedExercise.getMusclesTargeted());
                    
                    assertEquals(originalExercise.getRequiredEquipment().getClass(),
                            loadedExercise.getRequiredEquipment().getClass());
                    assertEquals(originalExercise.getMusclesTargeted().getName(),
                            loadedExercise.getMusclesTargeted().getName());
                }
            }
        }
    }

    @Test
    void testMetricsPropagationAfterLoading() {
        // Setup exercise metrics tracking
        Map<String, Double> strengthMetricsBeforeLoad = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        Map<String, Double> legMetricsBeforeLoad = legs.getGroupMetrics();
        
        // Examine initial metrics state
        assertEquals(0.0, strengthMetricsBeforeLoad.get("totalSets"), TEST_PRECISION);
        assertEquals(0.0, legMetricsBeforeLoad.get("totalEnduranceDuration"), TEST_PRECISION);

        // Assign workouts to schedule - this should activate metrics
        schedule.setScheduleForDay(0, strengthWorkout); // Monday
        schedule.setScheduleForDay(2, cardioWorkout);   // Wednesday
        schedule.setScheduleForDay(4, strengthWorkout); // Friday

        // Check metrics after workout assignment
        Map<String, Double> strengthMetricsAfterAssignment = 
                ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        Map<String, Double> legMetricsAfterAssignment = legs.getGroupMetrics();
        
        assertEquals(6.0, strengthMetricsAfterAssignment.get("totalSets"), TEST_PRECISION);
        assertEquals(1800.0, legMetricsAfterAssignment.get("totalEnduranceDuration"), TEST_PRECISION);

        // Save and load schedule
        WeeklySchedule loadedSchedule = saveAndLoad(schedule);

        // Inspect workout assignments in loaded schedule
        verifyScheduleDay(loadedSchedule, 0, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(loadedSchedule, 2, "Cardio Day", Workout.class, List.of("Running"));
        verifyScheduleDay(loadedSchedule, 4, "Strength Day", Workout.class, List.of("Bench Press"));

        // Evaluate metrics propagation after loading
        Map<String, Double> strengthMetricsAfterLoad = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        Map<String, Double> legMetricsAfterLoad = legs.getGroupMetrics();

        // Detailed metrics comparison
        verifyAssociatorMetrics((ExerciseAssociator) dumbbell, Map.of(
                "totalSets", 6.0,
                "totalReps", 72.0,
                "totalStrengthDuration", 684.0
        ));
        
        for (Muscle muscle : legs.getMuscles()) {
            verifyAssociatorMetrics(muscle, Map.of(
                    "totalEnduranceDuration", 1800.0,
                    "totalDuration", 1800.0
            ));
        }
        
        assertEquals(strengthMetricsAfterAssignment.get("totalSets"), 
                strengthMetricsAfterLoad.get("totalSets"), TEST_PRECISION);
        assertEquals(strengthMetricsAfterAssignment.get("totalReps"), 
                strengthMetricsAfterLoad.get("totalReps"), TEST_PRECISION);
        assertEquals(strengthMetricsAfterAssignment.get("totalStrengthDuration"), 
                strengthMetricsAfterLoad.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(legMetricsAfterAssignment.get("totalEnduranceDuration"), 
                legMetricsAfterLoad.get("totalEnduranceDuration"), TEST_PRECISION);
        assertEquals(legMetricsAfterAssignment.get("totalDuration"), 
                legMetricsAfterLoad.get("totalDuration"), TEST_PRECISION);
        
        // Inspect equipment and muscle groups in loaded exercises
        Exercise loadedBenchPress = loadedSchedule.getScheduleForDay(0).getExercises().get(0);
        assertNotNull(loadedBenchPress.getRequiredEquipment());
        assertEquals(Dumbbell.class, loadedBenchPress.getRequiredEquipment().getClass());
        
        Exercise loadedRunning = loadedSchedule.getScheduleForDay(2).getExercises().get(0);
        assertNotNull(loadedRunning.getRequiredEquipment());
        assertEquals(Treadmill.class, loadedRunning.getRequiredEquipment().getClass());
    }

    @Test
    void testMetricsClearanceOnScheduleModification() {
        // Set up initial schedule and verify metrics
        schedule.setScheduleForDay(0, strengthWorkout);
        Map<String, Double> initialMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        
        assertEquals(3.0, initialMetrics.get("totalSets"), TEST_PRECISION);
        
        // Check initial workout content
        verifyScheduleDay(schedule, 0, "Strength Day", Workout.class, List.of("Bench Press"));

        // Save and load
        WeeklySchedule loadedSchedule = saveAndLoad(schedule);

        // Confirm loaded workout contents
        verifyScheduleDay(loadedSchedule, 0, "Strength Day", Workout.class, List.of("Bench Press"));

        // Modify loaded schedule and observe metrics reset
        loadedSchedule.clearScheduleForDay(0);
        Map<String, Double> metricsAfterClear = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        
        assertEquals(0.0, metricsAfterClear.get("totalSets"), TEST_PRECISION);

        // Add different workout and review resulting metrics
        loadedSchedule.setScheduleForDay(0, cardioWorkout);
        
        // Validate new workout assignment
        verifyScheduleDay(loadedSchedule, 0, "Cardio Day", Workout.class, List.of("Running"));
        
        // Evaluate metrics after workout change
        Map<String, Double> metricsAfterNewWorkout = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        Map<String, Double> legMetrics = legs.getGroupMetrics();
        
        assertEquals(0.0, metricsAfterNewWorkout.get("totalSets"), TEST_PRECISION);
        assertEquals(1800.0, legMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
    }

    @Test
    void testMultipleContextMetricPropagation() {
        // Setup multiple workout contexts
        schedule.setScheduleForDay(0, strengthWorkout); // Monday
        schedule.setScheduleForDay(3, strengthWorkout); // Thursday
        schedule.setScheduleForDay(4, strengthWorkout); // Friday
        
        // Validate workout assignments
        verifyScheduleDay(schedule, 0, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(schedule, 3, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(schedule, 4, "Strength Day", Workout.class, List.of("Bench Press"));
        
        // Assess initial metrics accumulation
        Map<String, Double> initialMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        
        assertEquals(9.0, initialMetrics.get("totalSets"), TEST_PRECISION);
        assertEquals(108.0, initialMetrics.get("totalReps"), TEST_PRECISION);
        assertEquals(1026.0, initialMetrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(0.0, initialMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
        assertEquals(1026.0, initialMetrics.get("totalDuration"), TEST_PRECISION);
        assertEquals(810.0, initialMetrics.get("totalRestTimeBetween"), TEST_PRECISION);
        
        // Save and load the schedule
        WeeklySchedule loadedSchedule = saveAndLoad(schedule);
        
        // Confirm workout instances in loaded schedule
        verifyScheduleDay(loadedSchedule, 0, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(loadedSchedule, 3, "Strength Day", Workout.class, List.of("Bench Press"));
        verifyScheduleDay(loadedSchedule, 4, "Strength Day", Workout.class, List.of("Bench Press"));
        
        // Check metric registration contexts
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Thursday"));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Friday"));
        
        // Compare aggregated metrics after loading
        Map<String, Double> loadedMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        
        assertEquals(9.0, loadedMetrics.get("totalSets"), TEST_PRECISION);
        assertEquals(108.0, loadedMetrics.get("totalReps"), TEST_PRECISION);
        assertEquals(1026.0, loadedMetrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(0.0, loadedMetrics.get("totalIntervalDuration"), TEST_PRECISION);
        assertEquals(1026.0, loadedMetrics.get("totalDuration"), TEST_PRECISION);
        assertEquals(810.0, loadedMetrics.get("totalRestTimeBetween"), TEST_PRECISION);

        // Testing context removal
        loadedSchedule.clearScheduleForDay(0);

        // Check context management after modification
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday")); 
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Thursday"));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Friday"));
        
        // Examine metrics after context removal
        Map<String, Double> remainingMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        
        assertEquals(6.0, remainingMetrics.get("totalSets"), TEST_PRECISION);
        assertEquals(72.0, remainingMetrics.get("totalReps"), TEST_PRECISION);
        assertEquals(684.0, remainingMetrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(684.0, remainingMetrics.get("totalDuration"), TEST_PRECISION);
        assertEquals(540.0, remainingMetrics.get("totalRestTimeBetween"), TEST_PRECISION);
    }
}