package model.persistence;

import model.exercise.*;
import org.json.*;
import org.junit.jupiter.api.*;
import utility.PredefinedData;

import static org.junit.jupiter.api.Assertions.*;
import static utility.Utility.TEST_PRECISION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** This class tests the loading and saving of JSON data to fully represent and reconstruct
 *  valid states of ExerciseLibrary. However, this test suite also tests invalid saved states
 *  on ExerciseLibrary's persistence implementation as well.
 */
public class TestExerciseLibraryPersistence {
    private ExerciseLibrary exerciseLibrary;
    private PredefinedData predefinedData;
    private JSONObject validStrengthJson;
    private JSONObject validEnduranceJson;
    private JSONObject validIntervalJson;

    @BeforeEach
    void runBefore() {
        predefinedData = new PredefinedData();
        exerciseLibrary = new ExerciseLibrary(predefinedData);
        setupExerciseJsonObjects();
    }

    private void setupExerciseJsonObjects() {
        setupStrengthExerciseJsonObject();
        setupEnduranceExerciseJsonObject();
        setupIntervalExerciseJsonObject();
    }

    private void setupStrengthExerciseJsonObject() {
        validStrengthJson = new JSONObject()
                .put("name", "Bench Press")
                .put("type", "Strength")
                .put("equipmentName", "Dumbbell")
                .put("muscleGroupName", "Chest")
                .put("info", new JSONObject()
                        .put("sets", 3.0)
                        .put("reps", 12.0)
                        .put("timePerRep", 2.0)
                        .put("restTime", 1.5));
    }

    private void setupEnduranceExerciseJsonObject() {
        validEnduranceJson = new JSONObject()
                .put("name", "Running")
                .put("type", "Endurance")
                .put("equipmentName", "Treadmill")
                .put("muscleGroupName", "Quadriceps")
                .put("info", new JSONObject()
                        .put("duration", 30.0));
    }

    private void setupIntervalExerciseJsonObject() {
        validIntervalJson = new JSONObject()
                .put("name", "HIIT")
                .put("type", "Interval")
                .put("equipmentName", "Bodyweight")
                .put("muscleGroupName", "Core")
                .put("info", new JSONObject()
                        .put("timeOn", 45.0)
                        .put("timeOff", 15.0)
                        .put("repititions", 8.0));
    }

    // BASIC SERIALIZATION TESTS
    @Test
    void testEmptyLibrary() {
        JSONObject json = exerciseLibrary.toJson();
        ExerciseLibrary library = new ExerciseLibrary(predefinedData);
        library.fromJson(json, predefinedData);
        assertTrue(library.getAllExercises().isEmpty());
    }

    @Test
    void testIllegalArugmentExceptionDataNeededForStateConstruction() {
        try {
            exerciseLibrary.fromJson(validStrengthJson, new ArrayList<Integer>());
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // GOOD
        }
    }

    @Test
    void testNullExerciseType() {
        // Note: The test constructor for EnduranceExercise(2) should now set a default duration.
        ExerciseLibrary library = new ExerciseLibrary(predefinedData);
        library.addExercise(new EnduranceExercise(2));
    
        JSONObject json = library.toJson();
        library.fromJson(json, predefinedData);
        Exercise exercise = library.getAllExercises().get("Unnamed Exercise");
        assertNotNull(exercise);
        assertTrue(exercise instanceof EnduranceExercise);
        // default values set in test constructor
        assertEquals("Unnamed Exercise", exercise.getName());
        assertEquals("Endurance", exercise.exerciseType());
        // Assuming the test constructor sets duration to 1.0 minute:
        Map<String, Double> info = exercise.getInfo();
        assertEquals(1.0, info.get("duration"), TEST_PRECISION);
    }
    
    @Test
    void testSingleExercise() {
        Exercise exercise = createStrengthExercise("Test Exercise", 3, 12, 2.0, 1.5);
        exerciseLibrary.addExercise(exercise);

        JSONObject json = exerciseLibrary.toJson();
        ExerciseLibrary library = new ExerciseLibrary(predefinedData);
        library.fromJson(json, predefinedData);

        Exercise loaded = library.getExercise("Test Exercise");
        assertNotNull(loaded);
        assertTrue(loaded instanceof StrengthExercise);
        // Check each individual property
        assertEquals("Test Exercise", loaded.getName());
        assertEquals("Strength", loaded.exerciseType());
        assertEquals("Dumbbell", loaded.getRequiredEquipment().getEquipmentName());
        assertEquals("Chest", loaded.getMusclesTargeted().getName());
        Map<String, Double> info = loaded.getInfo();
        assertEquals(3.0, info.get("sets"), TEST_PRECISION);
        assertEquals(12.0, info.get("reps"), TEST_PRECISION);
        assertEquals(2.0, info.get("timePerRep"), TEST_PRECISION);
        assertEquals(1.5, info.get("restTime"), TEST_PRECISION);
    }

    @Test
    void testMultipleExerciseTypes() {
        Exercise strengthEx = createStrengthExercise("Strength", 3, 12, 2.0, 1.5);
        Exercise enduranceEx = createEnduranceExercise("Endurance", 30.0);
        Exercise intervalEx = createIntervalExercise("Interval", 45.0, 15.0, 8);

        exerciseLibrary.addExercise(strengthEx);
        exerciseLibrary.addExercise(enduranceEx);
        exerciseLibrary.addExercise(intervalEx);

        JSONObject json = exerciseLibrary.toJson();
        ExerciseLibrary library = new ExerciseLibrary(predefinedData);
        library.fromJson(json, predefinedData);

        // Strength Exercise
        Exercise loadedStrength = library.getExercise("Strength");
        assertNotNull(loadedStrength);
        assertTrue(loadedStrength instanceof StrengthExercise);
        assertEquals("Strength", loadedStrength.getName());
        assertEquals("Strength", loadedStrength.exerciseType());
        assertEquals("Dumbbell", loadedStrength.getRequiredEquipment().getEquipmentName());
        assertEquals("Chest", loadedStrength.getMusclesTargeted().getName());
        Map<String, Double> strengthInfo = loadedStrength.getInfo();
        assertEquals(3.0, strengthInfo.get("sets"), TEST_PRECISION);
        assertEquals(12.0, strengthInfo.get("reps"), TEST_PRECISION);
        assertEquals(2.0, strengthInfo.get("timePerRep"), TEST_PRECISION);
        assertEquals(1.5, strengthInfo.get("restTime"), TEST_PRECISION);

        // Endurance Exercise
        Exercise loadedEndurance = library.getExercise("Endurance");
        assertNotNull(loadedEndurance);
        assertTrue(loadedEndurance instanceof EnduranceExercise);
        assertEquals("Endurance", loadedEndurance.getName());
        assertEquals("Endurance", loadedEndurance.exerciseType());
        assertEquals("Treadmill", loadedEndurance.getRequiredEquipment().getEquipmentName());
        assertEquals("Quadriceps", loadedEndurance.getMusclesTargeted().getName());
        Map<String, Double> enduranceInfo = loadedEndurance.getInfo();
        assertEquals(30.0, enduranceInfo.get("duration"), TEST_PRECISION);
        // Check that getDuration() returns 30 minutes converted to seconds
        assertEquals(30.0 * 60, loadedEndurance.getDuration(), TEST_PRECISION);

        // Interval Exercise
        Exercise loadedInterval = library.getExercise("Interval");
        assertNotNull(loadedInterval);
        assertTrue(loadedInterval instanceof IntervalExercise);
        assertEquals("Interval", loadedInterval.getName());
        assertEquals("Interval", loadedInterval.exerciseType());
        assertEquals("Bodyweight", loadedInterval.getRequiredEquipment().getEquipmentName());
        assertEquals("Core", loadedInterval.getMusclesTargeted().getName());
        Map<String, Double> intervalInfo = loadedInterval.getInfo();
        assertEquals(45.0, intervalInfo.get("timeOn"), TEST_PRECISION);
        assertEquals(15.0, intervalInfo.get("timeOff"), TEST_PRECISION);
        assertEquals(8.0, intervalInfo.get("repititions"), TEST_PRECISION);
    }

    @Test
    void testFullRoundTripSerialization() {
        JSONObject libraryJson = new JSONObject();
        JSONArray exercises = new JSONArray()
                .put(validStrengthJson)
                .put(validEnduranceJson)
                .put(validIntervalJson);
        libraryJson.put("exercises", exercises);

        exerciseLibrary.fromJson(libraryJson, predefinedData);

        // each exercise was reconstructed correctly
        Exercise strengthExercise = exerciseLibrary.getExercise("Bench Press");
        Exercise enduranceExercise = exerciseLibrary.getExercise("Running");
        Exercise intervalExercise = exerciseLibrary.getExercise("HIIT");

        // Assert exercises exist
        assertNotNull(strengthExercise, "Strength exercise should not be null");
        assertNotNull(enduranceExercise, "Endurance exercise should not be null");
        assertNotNull(intervalExercise, "Interval exercise should not be null");

        // Strength Exercise Details
        assertEquals("Bench Press", strengthExercise.getName());
        assertEquals("Strength", strengthExercise.exerciseType());
        assertEquals("Dumbbell", strengthExercise.getRequiredEquipment().getEquipmentName());
        assertEquals("Chest", strengthExercise.getMusclesTargeted().getName());
        Map<String, Double> strengthInfo = strengthExercise.getInfo();
        assertEquals(3.0, strengthInfo.get("sets"), TEST_PRECISION);
        assertEquals(12.0, strengthInfo.get("reps"), TEST_PRECISION);
        assertEquals(2.0, strengthInfo.get("timePerRep"), TEST_PRECISION);
        assertEquals(1.5, strengthInfo.get("restTime"), TEST_PRECISION);

        // Endurance Exercise Details
        assertEquals("Running", enduranceExercise.getName());
        assertEquals("Endurance", enduranceExercise.exerciseType());
        assertEquals("Treadmill", enduranceExercise.getRequiredEquipment().getEquipmentName());
        assertEquals("Quadriceps", enduranceExercise.getMusclesTargeted().getName());
        Map<String, Double> enduranceInfo = enduranceExercise.getInfo();
        assertEquals(30.0, enduranceInfo.get("duration"), TEST_PRECISION);
        assertEquals(30.0 * 60, enduranceExercise.getDuration(), TEST_PRECISION);

        // Interval Exercise Details
        assertEquals("HIIT", intervalExercise.getName());
        assertEquals("Interval", intervalExercise.exerciseType());
        assertEquals("Bodyweight", intervalExercise.getRequiredEquipment().getEquipmentName());
        assertEquals("Core", intervalExercise.getMusclesTargeted().getName());
        Map<String, Double> intervalInfo = intervalExercise.getInfo();
        assertEquals(45.0, intervalInfo.get("timeOn"), TEST_PRECISION);
        assertEquals(15.0, intervalInfo.get("timeOff"), TEST_PRECISION);
        assertEquals(8.0, intervalInfo.get("repititions"), TEST_PRECISION);
    }

    @Test
    void testNullAndEmptyInputs() {
        exerciseLibrary.fromJson(null, predefinedData);
        assertTrue(exerciseLibrary.getAllExercises().isEmpty());

        exerciseLibrary.fromJson(new JSONObject(), predefinedData);
        assertTrue(exerciseLibrary.getAllExercises().isEmpty());
    }

    @Test
    void testMissingFields() {
        JSONObject incompleteJson = new JSONObject()
                .put("exercises", new JSONArray()
                        .put(new JSONObject()
                                .put("type", "Strength")));
        exerciseLibrary.fromJson(incompleteJson, predefinedData);
        Exercise exercise = exerciseLibrary.getExercise("Unnamed Exercise");
        assertNotNull(exercise);
        assertTrue(exercise instanceof StrengthExercise);
        // Check that missing equipment and muscle group result in default values
        assertEquals("Bodyweight", exercise.getRequiredEquipment().getEquipmentName());
    }

    @Test
    void testMalformedData() {
        testMalformedExercise("Strength", StrengthExercise.class);
        testMalformedExercise("Endurance", EnduranceExercise.class);
        testMalformedExercise("Interval", IntervalExercise.class);
    }

    @Test
    void testExtremeValues() {
        testExerciseExtremeValues("Min", true);
        testExerciseExtremeValues("Max", false);
    }
    
    private void testExerciseExtremeValues(String testTypePrefix, boolean testMin) {
        ArrayList<String> types = new ArrayList<String>(List.of("Strength", "Endurance", "Interval"));
        for (String type : types) {
            Exercise exercise = createExtremeExercise(testTypePrefix + " " + type, type, testMin);
            verifyExerciseExtremeValues(exercise, testTypePrefix);
        }
    }
    
    private Exercise createExtremeExercise(String name, String type, boolean testMin) {
        if (testMin) {
            switch (type) {
                case "Strength":
                    // sets and reps are allowed to be 1 minimum
                    return createStrengthExercise(name, 1, 1, Double.MIN_NORMAL, Double.MIN_NORMAL);
                case "Endurance":
                    return createEnduranceExercise(name, Double.MIN_NORMAL);
                case "Interval":
                    return createIntervalExercise(name, Double.MIN_NORMAL, Double.MIN_NORMAL, 1);
                default:
                    throw new IllegalArgumentException("Unknown exercise type: " + type);
            }
        } else {
            // Faced some JSON <-> Java extreme value parsing errors, particularly with Double.MAX_VALUE
            switch (type) {
                case "Strength":
                    return createStrengthExercise(name, 10000000, 1000000, 100000.0, 1000000.0);
                case "Endurance":
                    return createEnduranceExercise(name, 1000000.0);
                case "Interval":
                    return createIntervalExercise(name, 100000.0, 100000.0, 10000);
                default:
                    throw new IllegalArgumentException("Unknown exercise type: " + type);
            }
        }
    }
    
    private void verifyExerciseExtremeValues(Exercise exercise, String testTypePrefix) {
        exerciseLibrary.addExercise(exercise);
        // Save exercise
        JSONObject json = exerciseLibrary.toJson();

        // Load onto another ExerciseLibrary
        ExerciseLibrary newLibrary = new ExerciseLibrary(predefinedData);
        newLibrary.fromJson(json, predefinedData);
        
        Exercise loaded = newLibrary.getExercise(testTypePrefix + " " + exercise.exerciseType());
        assertNotNull(loaded, "Loaded exercise should not be null");
        
        // Shared fields between Exercise Instantiations
        assertEquals(exercise.getName(), loaded.getName());
        assertEquals(exercise.exerciseType(), loaded.exerciseType());
        assertEquals(exercise.getRequiredEquipment().getEquipmentName(), 
                loaded.getRequiredEquipment().getEquipmentName());
        assertEquals(exercise.getMusclesTargeted().getName(), 
                loaded.getMusclesTargeted().getName());
        
        Map<String, Double> expectedInfo = exercise.getInfo();
        Map<String, Double> actualInfo = loaded.getInfo();
        
        assertEquals(expectedInfo.keySet(), actualInfo.keySet(), "Info keys should match");

        // Each key-value unique to the Exercise instantiation must match its actual key-value
        for (String key : expectedInfo.keySet()) {
            assertEquals(expectedInfo.get(key), actualInfo.get(key), TEST_PRECISION);
        }
    }

    // EXERCISE CREATION HELPER METHODS
    private Exercise createStrengthExercise(String name, int sets, int reps, 
            double timePerRep, double restTime) {
        return new StrengthExercise(name, sets, reps, timePerRep, restTime,
                predefinedData.findEquipment("Dumbbell"),
                predefinedData.findMuscleGroup("Chest"));
    }

    private Exercise createEnduranceExercise(String name, double duration) {
        return new EnduranceExercise(name, duration,
                predefinedData.findEquipment("Treadmill"),
                predefinedData.findMuscleGroup("Quadriceps"));
    }

    private Exercise createIntervalExercise(String name, double timeOn, 
            double timeOff, int repetitions) {
        return new IntervalExercise(name, timeOn, timeOff, repetitions,
                predefinedData.findEquipment("Bodyweight"),
                predefinedData.findMuscleGroup("Core"));
    }

    private void testMalformedExercise(String type, Class<?> expectedClass) {
        // JSONObject json is NOT in expected form
        JSONObject json = new JSONObject()
                .put("exercises", new JSONArray()
                        .put(new JSONObject()
                                .put("name", "Test")
                                .put("type", type)
                                .put("info", new JSONObject()
                                        .put("invalidMetric", "invalid"))));
        exerciseLibrary.fromJson(json, predefinedData);
        Exercise exercise = exerciseLibrary.getExercise("Test");
        assertNotNull(exercise);
        assertTrue(expectedClass.isInstance(exercise));
    }
}
