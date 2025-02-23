package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static utility.Utility.TEST_PRECISION;

import model.muscle.Muscle;
import model.muscle.MuscleGroup;

/** This class tests the IMMUTABILITY of MuscleGroup and verifies its side-effects 
 * on associated Muscle (ExerciseAssociator) objects are as expected/specified. */
public class TestMuscleGroup {
    private MuscleGroup emptyGroup;
    private MuscleGroup singleMuscleGroup;
    private MuscleGroup multiMuscleGroup;
    private List<Muscle> singleMuscleList;
    private List<Muscle> multiMuscleList;
    private Map<String, Double> strengthInfo;
    private Map<String, Double> enduranceInfo;
    private Map<String, Double> intervalInfo;
    
    private static final String MONDAY_CONTEXT = "Monday";
    private static final String WEDNESDAY_CONTEXT = "Wednesday";

    @BeforeEach
    void runBefore() {
        initializeMuscles();
        initializeGroups();
        initializeExerciseData();
    }

    private void initializeMuscles() {
        singleMuscleList = new ArrayList<Muscle>();
        singleMuscleList.add(new Muscle("Biceps"));

        multiMuscleList = new ArrayList<Muscle>();
        multiMuscleList.add(new Muscle("Chest"));
        multiMuscleList.add(new Muscle("Front Deltoid"));
        multiMuscleList.add(new Muscle("Triceps"));
    }

    private void initializeGroups() {
        emptyGroup = new MuscleGroup("Empty Group", null);
        singleMuscleGroup = new MuscleGroup("Biceps Group", singleMuscleList);
        multiMuscleGroup = new MuscleGroup("Push Muscles", multiMuscleList);
    }

    private void initializeExerciseData() {
        strengthInfo = new HashMap<String, Double>();
        strengthInfo.put("totalSets", 4.0);
        strengthInfo.put("totalReps", 8.0);
        strengthInfo.put("totalStrengthDuration", 240.0);
        strengthInfo.put("totalRestTimeBetween", 90.0);
        strengthInfo.put("totalDuration", 330.0);

        enduranceInfo = new HashMap<String, Double>();
        enduranceInfo.put("totalEnduranceDuration", 1800.0);
        enduranceInfo.put("totalDuration", 1800.0);

        intervalInfo = new HashMap<String, Double>();
        intervalInfo.put("totalIntervalDuration", 900.0);
        intervalInfo.put("totalRestTimeBetween", 300.0);
        intervalInfo.put("totalDuration", 1200.0);
    }

    @Test
    void testNullInitiation() {
        MuscleGroup nullMuscleGroup = new MuscleGroup(null, null);
        assertEquals(nullMuscleGroup.getName(), "Unnamed MuscleGroup");

        nullMuscleGroup = new MuscleGroup("", null);
        assertEquals(nullMuscleGroup.getName(), "Unnamed MuscleGroup");
    }

    @Test
    void addNullMuscleToMuscleGroup() {
        ArrayList<Muscle> singleMuscleListCopy = new ArrayList<Muscle>(singleMuscleList);
        singleMuscleListCopy.add(null);
        MuscleGroup testMuscleGroup = new MuscleGroup("Null Muscle Group", singleMuscleListCopy);
        assertEquals(testMuscleGroup.getMuscles().size(), 1);
    }

    @Test
    void testConstructor() {
        // Empty group
        assertTrue(emptyGroup.getMuscles().isEmpty());
        assertEquals("Empty Group", emptyGroup.getName());
        assertTrue(emptyGroup.getGroupMetrics().isEmpty());

        // Single muscle group
        Set<Muscle> singleMuscles = singleMuscleGroup.getMuscles();
        assertEquals(1, singleMuscles.size());
        assertEquals("Biceps Group", singleMuscleGroup.getName());
        assertEquals("Biceps", singleMuscles.iterator().next().getName());

        // Multiple muscles in group
        Set<Muscle> multiMuscles = multiMuscleGroup.getMuscles();
        assertEquals(3, multiMuscles.size());
        assertEquals("Push Muscles", multiMuscleGroup.getName());
    }

    @Test
    void testRegisterExerciseWithContext() {
        assertTrue(multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo));
        
        // Check same exercise can be registered with different context
        assertTrue(multiMuscleGroup.registerMusclesForMetrics("BenchPress", WEDNESDAY_CONTEXT, strengthInfo));
        
        // Check metrics are properly accumulated across contexts
        Map<String, Double> metrics = multiMuscleGroup.getGroupMetrics();
        assertEquals(8.0, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(16.0, metrics.get("totalReps"), TEST_PRECISION);
        assertEquals(480.0, metrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(180.0, metrics.get("totalRestTimeBetween"), TEST_PRECISION);
        assertEquals(660.0, metrics.get("totalDuration"), TEST_PRECISION);
    }

    @Test
    void testRegisterSameExerciseSameContext() {
        assertTrue(multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo));
        // Attempt to register same exercise with same context should fail
        assertFalse(multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo));
        
        // Check metrics weren't duplicated
        Map<String, Double> metrics = multiMuscleGroup.getGroupMetrics();
        assertEquals(4.0, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(8.0, metrics.get("totalReps"), TEST_PRECISION);
    }

    @Test
    void testInvalidRegistration() {
        // null exercise name
        assertFalse(multiMuscleGroup.registerMusclesForMetrics(null, MONDAY_CONTEXT, strengthInfo));
        
        // null context
        assertFalse(multiMuscleGroup.registerMusclesForMetrics("BenchPress", null, strengthInfo));
        
        // null metrics
        assertFalse(multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, null));
        
        // empty group
        assertFalse(emptyGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo));
        
        // Check no metrics were registered
        assertTrue(multiMuscleGroup.getGroupMetrics().get("totalSets") == 0);
    }

    @Test
    void testUnregisterExerciseWithContext() {
        // Register same exercise in different contexts
        multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo);
        multiMuscleGroup.registerMusclesForMetrics("BenchPress", WEDNESDAY_CONTEXT, strengthInfo);
        
        // Unregister exercise from one context
        assertTrue(multiMuscleGroup.unregisterMusclesFromMetrics("BenchPress", MONDAY_CONTEXT));
        
        // Check metrics from other context remain
        Map<String, Double> metrics = multiMuscleGroup.getGroupMetrics();
        assertEquals(4.0, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(8.0, metrics.get("totalReps"), TEST_PRECISION);
        
        // Unregister non-existent context
        assertFalse(multiMuscleGroup.unregisterMusclesFromMetrics("BenchPress", "Friday"));
    }

    @Test
    void testInvalidUnregistration() {
        multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo);
        
        // null exercise name
        assertFalse(multiMuscleGroup.unregisterMusclesFromMetrics(null, MONDAY_CONTEXT));
        
        // null context
        assertFalse(multiMuscleGroup.unregisterMusclesFromMetrics("BenchPress", null));
        
        // Check original metrics remain unchanged
        Map<String, Double> metrics = multiMuscleGroup.getGroupMetrics();
        assertEquals(4.0, metrics.get("totalSets"), TEST_PRECISION);
    }

    @Test
    void testComplexMetricScenarioWithContexts() {
        // Register multiple exercises with different contexts
        multiMuscleGroup.registerMusclesForMetrics("BenchPress", MONDAY_CONTEXT, strengthInfo);
        multiMuscleGroup.registerMusclesForMetrics("Running", MONDAY_CONTEXT, enduranceInfo);
        multiMuscleGroup.registerMusclesForMetrics("HIIT", MONDAY_CONTEXT, intervalInfo);
        multiMuscleGroup.registerMusclesForMetrics("BenchPress", WEDNESDAY_CONTEXT, strengthInfo);
        
        Map<String, Double> metrics = multiMuscleGroup.getGroupMetrics();
        
        // Check combined metrics across contexts
        assertEquals(8.0, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(16.0, metrics.get("totalReps"), TEST_PRECISION);
        assertEquals(480.0, metrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(1800.0, metrics.get("totalEnduranceDuration"), TEST_PRECISION);
        assertEquals(900.0, metrics.get("totalIntervalDuration"), TEST_PRECISION);
        assertEquals(180.0 + 300.0, metrics.get("totalRestTimeBetween"), TEST_PRECISION);
        assertEquals(3660.0, metrics.get("totalDuration"), TEST_PRECISION);
        
        // Unregister one context and Check remaining metrics
        multiMuscleGroup.unregisterMusclesFromMetrics("BenchPress", MONDAY_CONTEXT);
        metrics = multiMuscleGroup.getGroupMetrics();
        assertEquals(4.0, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(8.0, metrics.get("totalReps"), TEST_PRECISION);
    }
}