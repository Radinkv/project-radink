package persistence;

import static org.junit.jupiter.api.Assertions.*;
import static utility.Utility.TEST_PRECISION;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.equipment.*;
import model.equipment.strength.*;
import model.equipment.cardio.*;
import model.equipment.bodyweight.*;
import model.exercise.*;
import model.muscle.*;
import model.workout.*;

/** This class tests the loading and saving of JSON data to fully represent and reconstruct
 *  valid states of WorkoutLibrary. However, this test suite also tests invalid saved states
 *  on WorkoutLibrary's persistence implementation as well.
 * 
 *  This test suite assumes that ExerciseLibrary's persistence works as specified.
 */
public class TestWorkoutLibraryPersistence {
    private WorkoutLibrary workoutLibrary;
    private ExerciseLibrary exerciseLibrary;
    
    private Exercise benchPress;
    private Exercise running;
    private Exercise hiit;
    
    private Workout identityWorkout;
    private Workout complexWorkout;
    private RestDay identityRestDay;
    
    private JSONObject validWorkoutJson;
    private JSONObject validRestDayJson;
    private JSONObject invalidWorkoutPlanJson;

    @BeforeEach
    void runBefore() {
        exerciseLibrary = new ExerciseLibrary();
        workoutLibrary = new WorkoutLibrary();
        setupTestData();
    }

    private void setupTestData() {
        setupExercises();
        setupCommonWorkouts();
        setupJsonObjects();
    }
    
    private void setupExercises() {
        Equipment dumbbell = new Dumbbell();
        Equipment treadmill = new Treadmill();
        Equipment bodyweight = new BodyWeight();
        
        MuscleGroup chest = new MuscleGroup("Chest", List.of(new Muscle("Pectorals")));
        MuscleGroup legs = new MuscleGroup("Legs", List.of(new Muscle("Quadriceps")));
        MuscleGroup core = new MuscleGroup("Core", List.of(new Muscle("Abs")));
        
        benchPress = new StrengthExercise("Bench Press", 3, 12, 2.0, 1.5, dumbbell, chest);
        running = new EnduranceExercise("Running", 30.0, treadmill, legs);
        hiit = new IntervalExercise("HIIT", 45.0, 15.0, 8, bodyweight, core);
        
        exerciseLibrary.addExercise(benchPress);
        exerciseLibrary.addExercise(running);
        exerciseLibrary.addExercise(hiit);
    }
    
    private void setupCommonWorkouts() {
        identityWorkout = new Workout("Standard Workout", 
                new ArrayList<Exercise>(Arrays.asList(benchPress, running)));
        complexWorkout = new Workout("Complex Workout", 
                new ArrayList<Exercise>(Arrays.asList(benchPress, running, hiit)));
        identityRestDay = new RestDay("Standard Rest");
        
        workoutLibrary.addWorkout(identityWorkout);
        workoutLibrary.addWorkout(complexWorkout);
        workoutLibrary.addWorkout(identityRestDay);
    }
    
    private void setupJsonObjects() {
        validWorkoutJson = new JSONObject()
            .put("name", "Test Workout")
            .put("type", "Workout")
            .put("exercises", new JSONArray()
                .put("Bench Press")
                .put("Running")
                .put("HIIT"));
        
        validRestDayJson = new JSONObject()
            .put("name", "Test Rest")
            .put("type", "RestDay");

        invalidWorkoutPlanJson = new JSONObject()
        .put("workouts", new JSONArray()
                .put(new JSONObject()
                        .put("name", "Test Workout")
                        .put("type", "InvalidType"))); // Neither null, RestDay, nor Workout
    }

    @Test
    void invalidfromJsonmissingNameshouldSkipWorkout() {
        JSONObject workoutMissingName = buildWorkoutJson(null, "Workout", new JSONArray().put("Bench Press"));
        JSONObject libraryJson = new JSONObject().put("workouts", new JSONArray().put(workoutMissingName));
        workoutLibrary.fromJson(libraryJson, exerciseLibrary);
        assertTrue(workoutLibrary.getAllWorkouts().isEmpty());
    }
    
    @Test
    void invalidfromJsonmissingTypeshouldSkipWorkout() {
        JSONObject workoutMissingType = buildWorkoutJson("Nameless Workout", null, new JSONArray().put("Bench Press"));
        JSONObject libraryJson = new JSONObject().put("workouts", new JSONArray().put(workoutMissingType));
        workoutLibrary.fromJson(libraryJson, exerciseLibrary);
        assertNull(workoutLibrary.getWorkout("Nameless Workout"));
    }
    
    @Test
    void invalidfromJsonnullAndEmptyJsonshouldHaveEmptyLibrary() {
        // null JSON input
        try {
            workoutLibrary.fromJson(null, exerciseLibrary);
            // Empty library merely initialized
            assertTrue(workoutLibrary.getAllWorkouts().isEmpty());
        } catch (JSONException e) {
            fail("No exception should be thrown for null JSON input");
        }
        
        // empty JSON object
        try {
            JSONObject emptyJson = new JSONObject();
            workoutLibrary.fromJson(emptyJson, exerciseLibrary);
            // Empty library merely initialized
            assertTrue(workoutLibrary.getAllWorkouts().isEmpty());
        } catch (JSONException e) {
            fail("No exception should be thrown for empty JSON");
        }
        
        // JSON missing the 'workouts' key
        try {
            JSONObject jsonWithoutWorkouts = new JSONObject().put("invalidKey", "invalidValue");
            workoutLibrary.fromJson(jsonWithoutWorkouts, exerciseLibrary);
            // Empty library merely initialized
            assertTrue(workoutLibrary.getAllWorkouts().isEmpty());
        } catch (JSONException e) {
            fail("No exception should be thrown for JSON missing workouts key");
        }
    }
    
    @Test
    void invalidfromJsoninvalidDataParametershouldThrowException() {
        JSONObject validJson = new JSONObject().put("workouts", new JSONArray());
        
        // null ExerciseLibrary
        try {
            workoutLibrary.fromJson(validJson, null);
            fail("IllegalArgumentException should have been thrown for null ExerciseLibrary");
        } catch (IllegalArgumentException e) {
            assertEquals("ExerciseLibrary required for state reconstruction", e.getMessage());
        }
        
        // invalid data parameter (reconstruction helper object not of type ExerciseLibrary)
        try {
            Object invalidData = new Object();
            workoutLibrary.fromJson(validJson, invalidData);
            fail("IllegalArgumentException should have been thrown for invalid data parameter");
        } catch (IllegalArgumentException e) {
            assertEquals("ExerciseLibrary required for state reconstruction", e.getMessage());
        }
    }

    @Test
    void invalidfromJsonUnknownTypeShouldNotCreateWorkout() {
        // WorkoutPlan JSON with name and invalid type that isn't null but also isn't RestDay/Workout

        
        workoutLibrary.fromJson(invalidWorkoutPlanJson, exerciseLibrary);
        assertNull(workoutLibrary.getWorkout("Test Workout"));
    }
    
    @Test
    void validfromJsonworkoutWithoutExercisesKeyshouldCreateWorkoutWithEmptyExerciseList() {
        // Create workout JSON without exercises field - should still be valid since exercises optional
        JSONObject workoutWithoutExercises = buildWorkoutJson("No Exercise Workout", "Workout", null);
        JSONObject libraryJson = new JSONObject().put("workouts", new JSONArray().put(workoutWithoutExercises));
        
        // Load JSON and verify workout created with no exercises rather than being skipped
        workoutLibrary.fromJson(libraryJson, exerciseLibrary);
        WorkoutPlan workout = workoutLibrary.getWorkout("No Exercise Workout");
        assertNotNull(workout);
        assertTrue(workout instanceof Workout);  // Should be Workout class not RestDay
        assertEquals(0, workout.getExercises().size());  // Exercise list exists but empty
    }
    
    @Test
    void validfromJsonRestDayJsonFormatShouldCreateRestDay() {
        // Use pre-made valid RestDay JSON template to test basic RestDay loading
        JSONObject libraryJson = new JSONObject().put("workouts", new JSONArray().put(validRestDayJson));
        workoutLibrary.fromJson(libraryJson, exerciseLibrary);
        
        // RestDay reconstructed correctly including proper class type
        WorkoutPlan restDay = workoutLibrary.getWorkout("Test Rest");
        verifyWorkoutState(restDay, "Test Rest", 0, RestDay.class);  // RestDays always have 0 exercises
    }
    
    @Test
    void validfromJsonMixedWorkoutAndRestDayShouldLoadCorrectly() {
        // Create JSON with both workout and rest day types to test they don't interfere
        JSONObject mixedJson = new JSONObject().put("workouts", new JSONArray()
                .put(validWorkoutJson)
                .put(validRestDayJson)
                .put(buildWorkoutJson("Another Rest", "RestDay", null))
        );
        
        workoutLibrary.fromJson(mixedJson, exerciseLibrary);
        
        // Workout must load with all exercises
        WorkoutPlan loadedWorkout = workoutLibrary.getWorkout("Test Workout");
        verifyWorkoutState(loadedWorkout, "Test Workout", 3, Workout.class);
        
        // both RestDay objects along the Workout object should load correctly
        verifyWorkoutState(workoutLibrary.getWorkout("Test Rest"), "Test Rest", 0, RestDay.class);
        verifyWorkoutState(workoutLibrary.getWorkout("Another Rest"), "Another Rest", 0, RestDay.class);
    }
    
    @Test
    void testExtremeNamesInWorkoutLibrary() {
        // Test edge cases for workout names to ensure robust name handling
        String[] testNames = new String[] {
            "", 
            "Rest!@#$%^&*()",
            "R" + "R".repeat(10000)
        };
        
        // First check RestDay objects handle extreme names correctly via JSON load
        for (String testName : testNames) {
            JSONObject json = new JSONObject()
                    .put("workouts", new JSONArray().put(buildWorkoutJson(testName, "RestDay", null)));
            workoutLibrary.fromJson(json, exerciseLibrary);
            WorkoutPlan rest = workoutLibrary.getWorkout(testName);
            verifyWorkoutState(rest, testName, 0, RestDay.class);
        }

        // Reset library to test same names for Workout 
        // WorkoutLibrary throws IllegalArgumentException for duplicate names
        workoutLibrary = new WorkoutLibrary();
    
        // Then verify Workout objects handle same extreme names via save/load cycle
        for (String testName : testNames) {
            // Create workout with extreme name and verify that it survives persistence
            Workout workout = new Workout(testName, Arrays.asList(benchPress));
            workoutLibrary.addWorkout(workout);
            WorkoutLibrary loadedLibrary = saveAndLoad(workoutLibrary);
            WorkoutPlan loaded = loadedLibrary.getWorkout(testName);
            verifyWorkoutState(loaded, testName, 1, Workout.class);
            verifyWorkoutContent(workout, loaded); // Exercise content should be unchanged
        }
    }

    
    
    @Test
    void largeWorkoutInLibraryShouldHandleManyExercises() {
        // Create workout with 1000 copies of an Exercise object to test large exercise list handling
        List<Exercise> manyExercises = new ArrayList<Exercise>();
        for (int i = 0; i < 1000; i++) {
            manyExercises.add(benchPress);
        }

        List<Exercise> anotherSetOfManyExercises = new ArrayList<Exercise>();
        for (int i = 0; i < 1000; i++) {
            anotherSetOfManyExercises.add(running); // Different Exercise class type
        }

        // Check for StrengthExercise
        // Add large workout and verify it persists correctly with all 1000 exercises intact
        Workout largeWorkout = new Workout("Large Strength Workout", manyExercises);
        workoutLibrary.addWorkout(largeWorkout);
        WorkoutLibrary loadedLibrary = saveAndLoad(workoutLibrary);
        WorkoutPlan loaded = loadedLibrary.getWorkout("Large Strength Workout");
        verifyWorkoutState(loaded, "Large Strength Workout", 1000, Workout.class);
        verifyWorkoutContent(largeWorkout, loaded); // All 1000 exercises should match exactly

        // Check for EnduranceExercise
        largeWorkout = new Workout("Large Endurance Workout", anotherSetOfManyExercises);
        workoutLibrary.addWorkout(largeWorkout);
        loadedLibrary = saveAndLoad(workoutLibrary);
        loaded = loadedLibrary.getWorkout("Large Endurance Workout");
        verifyWorkoutState(loaded, "Large Endurance Workout", 1000, Workout.class);
        verifyWorkoutContent(largeWorkout, loaded); // All 1000 exercises should match exactly
    }
    
    @Test
    void coresaveAndLoadbasicLibraryshouldRetainWorkouts() {        
        // Persistence cycle with two-exercise workout
        WorkoutLibrary loadedLibrary = saveAndLoad(workoutLibrary);
        WorkoutPlan workout = loadedLibrary.getWorkout("Standard Workout");
        
        // Check both structure and content preserved exactly
        verifyWorkoutState(workout, "Standard Workout", 2, Workout.class); // Basic state matches
        verifyWorkoutContent(workout, identityWorkout); // Detailed content matches
    }
    
    @Test
    void coresaveAndLoadworkoutContentshouldRetainExerciseDetails() {
        // Save and load comparison of more complex three-exercise workout
        WorkoutLibrary loadedLibrary = saveAndLoad(workoutLibrary);
        WorkoutPlan loadedComplex = loadedLibrary.getWorkout("Complex Workout");
        
        // complex Workout preserves both structure and detailed exercise content
        verifyWorkoutState(loadedComplex, "Complex Workout", 3, Workout.class);
        verifyWorkoutContent(loadedComplex, complexWorkout); // All exercise details must match
    }
    
    @Test
    void coremetricsPersistenceshouldRetainWorkoutMetrics() {
        // Workout metrics survive persistence cycle unchanged 
        WorkoutLibrary loadedLibrary = saveAndLoad(workoutLibrary);
        WorkoutPlan workout = loadedLibrary.getWorkout("Standard Workout");

        // Check the loaded workout matches the metrics and their values of 
        // identityWorkout which it preserved and loaded from
        String context = "TestContext";
        workout.activateMetrics(context);
        identityWorkout.activateMetrics(context);
        Map<String, Double> actualMetrics = workout.getWorkoutSummary();
        Map<String, Double> expectedMetrics = identityWorkout.getWorkoutSummary();
        assertEquals(expectedMetrics.keySet(), actualMetrics.keySet());
        for (String key : expectedMetrics.keySet()) {
            assertEquals(expectedMetrics.get(key), actualMetrics.get(key), TEST_PRECISION);
        }
        workout.deactivateMetrics(context);
        identityWorkout.deactivateMetrics(context);
    }

    
    // EFFECTS: Save given WorkoutLibrary state into JSON and load it back
    //          Return a new WorkoutLibrary instance reconstructed from that JSON
    private WorkoutLibrary saveAndLoad(WorkoutLibrary original) {
        JSONObject json = original.toJson();
        WorkoutLibrary loaded = new WorkoutLibrary();
        loaded.fromJson(json, exerciseLibrary);
        return loaded;
    }
    
    // REQUIRES: WorkoutLibrary does not modify Workout objects
    // EFFECTS: Verify a workout's state (name, exercise count, type)
    //          Assumes (backed by testing in TestWorkoutLibrary) that WorkoutLibrary
    //          DOES NOT modify the size or content of a Workout's Exercise list
    private void verifyWorkoutState(WorkoutPlan workout, String expectedName, 
            int expectedExercises, Class<?> expectedType) {
        assertNotNull(workout);
        assertEquals(expectedName, workout.getName());
        assertTrue(expectedType.isInstance(workout));
        if (workout instanceof Workout) {
            assertEquals(expectedExercises, workout.getExercises().size());
        }
    }
    
    // EFFECTS: Verify that two Workout objects have identical content by comparing their
    //          exercise list sizes and each exercise's properties 
    private void verifyWorkoutContent(WorkoutPlan actual, WorkoutPlan expected) {
        if (!(actual instanceof Workout) || !(expected instanceof Workout)) {
            return;
        }
        List<Exercise> actualExercises = actual.getExercises();
        List<Exercise> expectedExercises = expected.getExercises();
        for (int i = 0; i < expectedExercises.size(); i++) {
            verifyExerciseEquality(expectedExercises.get(i), actualExercises.get(i));
        }
    }
    
    // HELPER: for verifyWorkoutContent
    // EFFECTS: Compare all attributes of two Exercise objects for exact equality, attributes including:
    //          name, type, Equipment, MuscleGroup, and exercise info
    private void verifyExerciseEquality(Exercise expected, Exercise actual) {
        // Should be of the same instantiation type
        assertTrue(expected.getClass().equals(actual.getClass()));

        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.exerciseType(), actual.exerciseType());

        Equipment expectedEquipment = expected.getRequiredEquipment();
        Equipment actualEquipment = actual.getRequiredEquipment();
        assertEquals(expectedEquipment.getEquipmentName(), actualEquipment.getEquipmentName());
        assertEquals(expectedEquipment.getEquipmentType(), actualEquipment.getEquipmentType());
        
        MuscleGroup expectedMuscles = expected.getMusclesTargeted();
        MuscleGroup actualMuscles = actual.getMusclesTargeted();
        assertEquals(expectedMuscles.getName(), actualMuscles.getName());
        
        Map<String, Double> expectedInfo = expected.getInfo();
        Map<String, Double> actualInfo = actual.getInfo();
        assertEquals(expectedInfo.keySet(), actualInfo.keySet());
        for (String key : expectedInfo.keySet()) {
            assertEquals(expectedInfo.get(key), actualInfo.get(key), TEST_PRECISION);
        }
    }
    
    // EFFECTS: Create JSON for a workout with only provided fields included 
    //          that mirrors WorkoutLibrary's expected toJson format
    //          'type' distinguishes between "Workout" and "RestDay" for reconstruction
    //          'exercises' only included for Workout type, not RestDay type
    private JSONObject buildWorkoutJson(String name, String type, JSONArray exercises) {
        JSONObject obj = new JSONObject();
        if (name != null) {
            obj.put("name", name);
        }
        if (type != null) {
            obj.put("type", type); // WorkoutLibrary uses to determine if WorkoutPlan is a Workout or RestDay
        }
        if (exercises != null) {
            obj.put("exercises", exercises); // only valid for Workout, ignored for RestDay
        }
        return obj;
    }
}
