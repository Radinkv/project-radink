package model;

import static org.junit.jupiter.api.Assertions.*;
import static utility.Utility.TEST_PRECISION;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.association.ExerciseAssociator;
import model.equipment.strength.*;
import model.equipment.cardio.Treadmill;
import model.equipment.bodyweight.BodyWeight;
import model.muscle.Muscle;

/** ExerciseAssociator Tests for each subclass/implementation of ExerciseAssociator
    If one implementation overrides ExerciseAssociator, this test suite formation detects that
    Equipments/muscles are split to directly see which subclass potenttially (should they override
    ExerciseAssociator's methods) has an incorrect implementation  */
public class TestExerciseAssociator {
    // Equipment implementations
    private ExerciseAssociator barbell;
    private ExerciseAssociator cable;
    private ExerciseAssociator dumbbell;
    private ExerciseAssociator machine;
    private ExerciseAssociator treadmill;
    private ExerciseAssociator bodyweight;
    
    private ExerciseAssociator bicep;
    private ExerciseAssociator quad; 

    private Map<String, Double> strengthInfo1;
    private Map<String, Double> strengthInfo2;
    private Map<String, Double> enduranceInfo1;
    private Map<String, Double> enduranceInfo2;
    private Map<String, Double> intervalInfo1;
    private Map<String, Double> intervalInfo2;
    private Map<String, Double> invalidInfo;
    private Map<String, Double> emptyInfo;
    private Map<String, Double> partialInfo;
    private Map<String, Double> mixedInfo;

    @BeforeEach
    void runBefore() {
        barbell = new Barbell();
        cable = new Cable();
        dumbbell = new Dumbbell();
        machine = new Machine();
        treadmill = new Treadmill();
        bodyweight = new BodyWeight();
        bicep = new Muscle("Bicep");
        quad = new Muscle("Quads");

        initializeMockData();
    }

    // STRENGTH EQUIPMENT
    @Test
    void testBarbellAssociator() {
        testRegistrationHelper(barbell);
        testUnregistrationHelper(barbell);
        testMetricAggregationHelper(barbell);
        testEdgeCasesHelper(barbell);
        testContextSpecificBehavior(barbell);
    }

    @Test
    void testDumbbellAssociator() {
        testRegistrationHelper(dumbbell);
        testUnregistrationHelper(dumbbell);
        testMetricAggregationHelper(dumbbell);
        testEdgeCasesHelper(dumbbell);
        testContextSpecificBehavior(dumbbell);
    }

    @Test
    void testCableAssociator() {
        testRegistrationHelper(cable);
        testUnregistrationHelper(cable);
        testMetricAggregationHelper(cable);
        testEdgeCasesHelper(cable);
        testContextSpecificBehavior(cable);
    }

    @Test
    void testMachineAssociator() {
        testRegistrationHelper(machine);
        testUnregistrationHelper(machine);
        testMetricAggregationHelper(machine);
        testEdgeCasesHelper(machine);
        testContextSpecificBehavior(machine);
    }


    // CARDIO EQUIPMENT
    @Test
    void testTreadmillAssociator() {
        testRegistrationHelper(treadmill);
        testUnregistrationHelper(treadmill);
        testMetricAggregationHelper(treadmill);
        testEdgeCasesHelper(treadmill);
        testContextSpecificBehavior(treadmill);
    }

    // BODY WEIGHT
    @Test
    void testBodyweightAssociator() {
        testRegistrationHelper(bodyweight);
        testUnregistrationHelper(bodyweight);
        testMetricAggregationHelper(bodyweight);
        testEdgeCasesHelper(bodyweight);
        testContextSpecificBehavior(bodyweight);
    }


    // MUSCLES
    @Test
    void testBicepMuscleAssociator() {
        testRegistrationHelper(bicep);
        testUnregistrationHelper(bicep);
        testMetricAggregationHelper(bicep);
        testEdgeCasesHelper(bicep);
        testContextSpecificBehavior(bicep);
    }

    @Test
    void testQuadMuscleAssociator() {
        testRegistrationHelper(quad);
        testUnregistrationHelper(quad);
        testMetricAggregationHelper(quad);
        testEdgeCasesHelper(quad);
        testContextSpecificBehavior(quad);
    }


    private void initializeMockData() {
        initializeStrengthData();
        initializeEnduranceData();
        initializeIntervalData();
        initializeInvalidData();
    }
    
    private void initializeStrengthData() {
        strengthInfo1 = new HashMap<>();
        strengthInfo1.put("totalSets", 3.7);
        strengthInfo1.put("totalReps", 12.3);
        strengthInfo1.put("totalStrengthDuration", 187.8);
        strengthInfo1.put("totalRestTimeBetween", 63.2);
        strengthInfo1.put("totalDuration", 251.0);
    
        strengthInfo2 = new HashMap<>();
        strengthInfo2.put("totalSets", 4.2);
        strengthInfo2.put("totalReps", 8.7);
        strengthInfo2.put("totalStrengthDuration", 242.3);
        strengthInfo2.put("totalRestTimeBetween", 91.8);
        strengthInfo2.put("totalDuration", 334.1);
    }
    
    private void initializeEnduranceData() {
        enduranceInfo1 = new HashMap<>();
        enduranceInfo1.put("totalEnduranceDuration", 1823.7);
        enduranceInfo1.put("totalDuration", 1823.7);
    
        enduranceInfo2 = new HashMap<>();
        enduranceInfo2.put("totalEnduranceDuration", 2418.4);
        enduranceInfo2.put("totalDuration", 2418.4);
    }
    
    private void initializeIntervalData() {
        intervalInfo1 = new HashMap<>();
        intervalInfo1.put("totalIntervalDuration", 923.4);
        intervalInfo1.put("totalRestTimeBetween", 328.9);
        intervalInfo1.put("totalDuration", 1252.3);
    
        intervalInfo2 = new HashMap<>();
        intervalInfo2.put("totalIntervalDuration", 1231.8);
        intervalInfo2.put("totalRestTimeBetween", 427.6);
        intervalInfo2.put("totalDuration", 1659.4);
    }
    
    private void initializeInvalidData() {
        invalidInfo = new HashMap<>();
        invalidInfo.put("invalidKey1", 123.4);
        invalidInfo.put("invalidKey2", 234.5);
        invalidInfo.put("wrongMetric", 345.6);
    
        emptyInfo = new HashMap<>();
    
        partialInfo = new HashMap<>();
        partialInfo.put("totalSets", 3.8);
        
        // Miss other metrics intentionally than from what is a full metric suite
        mixedInfo = new HashMap<>();
        mixedInfo.put("totalSets", 4.3);
        mixedInfo.put("invalidKey", 156.7);
        mixedInfo.put("totalReps", 10.2);
        mixedInfo.put("wrongMetric", 267.8);

        System.out.println("mixedInfo after initialization:");
        mixedInfo.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    private void testRegistrationHelper(ExerciseAssociator associator) {
        associator.clearExercises();
        
        // Valid registration with different contexts
        assertTrue(associator.registerExercise("Exercise1", "Monday", strengthInfo1));
        assertEquals(1, associator.getNumAssociatedExercises());
        assertTrue(associator.containsExercise("Exercise1", "Monday"));
    
        // Same exercise, different context (should work)
        assertTrue(associator.registerExercise("Exercise1", "Tuesday", strengthInfo1));
        assertEquals(2, associator.getNumAssociatedExercises());
        assertTrue(associator.containsExercise("Exercise1", "Tuesday"));
    
        // Duplicate registration (same exercise and context)
        assertFalse(associator.registerExercise("Exercise1", "Monday", strengthInfo2));
        assertEquals(2, associator.getNumAssociatedExercises());
    
        // Null exercise name
        assertFalse(associator.registerExercise(null, "Monday", strengthInfo1));
        assertEquals(2, associator.getNumAssociatedExercises());
    
        // Null context
        assertFalse(associator.registerExercise("Exercise2", null, strengthInfo1));
        assertEquals(2, associator.getNumAssociatedExercises());
    
        // Null exercise info
        assertFalse(associator.registerExercise("Exercise2", "Monday", null));
        assertEquals(2, associator.getNumAssociatedExercises());
    
        // Registration with invalid data
        assertTrue(associator.registerExercise("Exercise3", "Monday", invalidInfo));
        assertEquals(3, associator.getNumAssociatedExercises());
    
        // Registration with empty data
        assertTrue(associator.registerExercise("Exercise4", "Monday", emptyInfo));
        assertEquals(4, associator.getNumAssociatedExercises());
    
        // Registration with partial data
        assertTrue(associator.registerExercise("Exercise5", "Monday", partialInfo));
        assertEquals(5, associator.getNumAssociatedExercises());
    
        // Registration with mixed valid/invalid data
        assertTrue(associator.registerExercise("Exercise6", "Monday", mixedInfo));
        assertEquals(6, associator.getNumAssociatedExercises());
    }
    
    private void testUnregistrationHelper(ExerciseAssociator associator) {
        associator.clearExercises();
        
        // Setup initial exercises with different contexts
        associator.registerExercise("Exercise1", "Monday", strengthInfo1);
        associator.registerExercise("Exercise1", "Tuesday", strengthInfo1);
        associator.registerExercise("Exercise2", "Monday", enduranceInfo1);
        associator.registerExercise("Exercise3", "Monday", intervalInfo1);
    
        // Valid unregistration
        assertTrue(associator.unregisterExercise("Exercise1", "Monday"));
        assertEquals(3, associator.getNumAssociatedExercises());
        assertFalse(associator.containsExercise("Exercise1", "Monday"));
        assertTrue(associator.containsExercise("Exercise1", "Tuesday")); // Other context still exists
    
        // Unregistering non-existent exercise
        assertFalse(associator.unregisterExercise("NonExistentExercise", "Monday"));
        assertEquals(3, associator.getNumAssociatedExercises());
    
        // Unregistering with null exercise name
        assertFalse(associator.unregisterExercise(null, "Monday"));
        assertEquals(3, associator.getNumAssociatedExercises());
    
        // Unregistering with null context
        assertFalse(associator.unregisterExercise("Exercise2", null));
        assertEquals(3, associator.getNumAssociatedExercises());
    
        // Unregistering existing exercise with wrong context
        assertFalse(associator.unregisterExercise("Exercise2", "Tuesday"));
        assertEquals(3, associator.getNumAssociatedExercises());
    
        // Unregistering all remaining exercises
        assertTrue(associator.unregisterExercise("Exercise1", "Tuesday"));
        assertTrue(associator.unregisterExercise("Exercise2", "Monday"));
        assertTrue(associator.unregisterExercise("Exercise3", "Monday"));
        assertEquals(0, associator.getNumAssociatedExercises());
    }

    private void testMetricAggregationHelper(ExerciseAssociator associator) {
        associator.clearExercises();
        
        // Register exercises with different contexts
        associator.registerExercise("StrengthEx1", "Monday", strengthInfo1);
        associator.registerExercise("StrengthEx1", "Tuesday", strengthInfo2); // Same exercise, different day
        associator.registerExercise("EnduranceEx1", "Monday", enduranceInfo1);
        associator.registerExercise("IntervalEx1", "Wednesday", intervalInfo1);
    
        Map<String, Double> metrics = associator.getAggregatedExerciseMetrics();
    
        // Calculate expected metrics considering both contexts
        Double expectedSets = strengthInfo1.get("totalSets") + strengthInfo2.get("totalSets");
        Double expectedReps = strengthInfo1.get("totalReps") + strengthInfo2.get("totalReps");
        Double expectedStrengthDuration = strengthInfo1.get("totalStrengthDuration") 
                + strengthInfo2.get("totalStrengthDuration");
        Double expectedEnduranceDuration = enduranceInfo1.get("totalEnduranceDuration");
        Double expectedIntervalDuration = intervalInfo1.get("totalIntervalDuration");
        Double expectedRestTime = strengthInfo1.get("totalRestTimeBetween")
                + strengthInfo2.get("totalRestTimeBetween")
                + intervalInfo1.get("totalRestTimeBetween");
        Double expectedTotalDuration = strengthInfo1.get("totalDuration")
                + strengthInfo2.get("totalDuration")
                + enduranceInfo1.get("totalDuration")
                + intervalInfo1.get("totalDuration");
    
        // all metrics
        assertEquals(expectedSets, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(expectedReps, metrics.get("totalReps"), TEST_PRECISION);
        assertEquals(expectedStrengthDuration, metrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(expectedEnduranceDuration, metrics.get("totalEnduranceDuration"), TEST_PRECISION);
        assertEquals(expectedIntervalDuration, metrics.get("totalIntervalDuration"), TEST_PRECISION);
        assertEquals(expectedRestTime, metrics.get("totalRestTimeBetween"), TEST_PRECISION);
        assertEquals(expectedTotalDuration, metrics.get("totalDuration"), TEST_PRECISION);
    
        // partial unregistration
        associator.unregisterExercise("StrengthEx1", "Monday");
        metrics = associator.getAggregatedExerciseMetrics();
        
        // metrics after partial unregistration
        assertEquals(strengthInfo2.get("totalSets"), metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(strengthInfo2.get("totalReps"), metrics.get("totalReps"), TEST_PRECISION);
    
        // Clear all and verify zero metrics
        associator.clearExercises();
        Map<String, Double> emptyMetrics = associator.getAggregatedExerciseMetrics();
        assertEquals(0.0, emptyMetrics.get("totalSets"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalReps"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalStrengthDuration"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalIntervalDuration"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalRestTimeBetween"), TEST_PRECISION);
        assertEquals(0.0, emptyMetrics.get("totalDuration"), TEST_PRECISION);
    }
    
    private void testEdgeCasesHelper(ExerciseAssociator associator) {
        associator.clearExercises();
    
        // various null combinations
        assertFalse(associator.registerExercise(null, null, mixedInfo));
        assertFalse(associator.registerExercise("Exercise", null, mixedInfo));
        assertFalse(associator.registerExercise(null, "Monday", mixedInfo));
        assertFalse(associator.containsExercise(null, null));
        assertFalse(associator.containsExercise("Exercise", null));
        assertFalse(associator.containsExercise(null, "Monday"));
    
        // same exercise with multiple contexts
        String[] contexts = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        for (String context : contexts) {
            assertTrue(associator.registerExercise("MultiContext", context, strengthInfo1));
            assertTrue(associator.containsExercise("MultiContext", context));
        }
        assertEquals(contexts.length, associator.getNumAssociatedExercises());
    
        // exercise with mixed valid/invalid data across contexts
        associator.clearExercises();
        assertTrue(associator.registerExercise("Mixed", "Monday", mixedInfo));
        Map<String, Double> metrics = associator.getAggregatedExerciseMetrics();
        
        // only valid metrics are counted
        assertEquals(10.2, metrics.get("totalReps"), TEST_PRECISION);
        assertEquals(4.3, metrics.get("totalSets"), TEST_PRECISION);

        associator.clearExercises();
    
        // extreme values across different contexts
        Map<String, Double> extremeValues = new HashMap<>();
        extremeValues.put("totalSets", Double.MAX_VALUE);
        extremeValues.put("totalReps", Double.MIN_VALUE);
        extremeValues.put("totalDuration", 1e-20);
    
        assertTrue(associator.registerExercise("Extreme", "Monday", extremeValues));
        assertTrue(associator.registerExercise("Extreme", "Tuesday", extremeValues));
        
        metrics = associator.getAggregatedExerciseMetrics();
        assertEquals(Double.MAX_VALUE + Double.MAX_VALUE, metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(Double.MIN_VALUE + Double.MIN_VALUE, metrics.get("totalReps"), TEST_PRECISION);
        assertEquals(2e-20, metrics.get("totalDuration"), 1e-21);

        associator.clearExercises();
    
        // For simplicity of implementation, these cases are ALLOWED. 
        // Different styles of 
        associator.clearExercises();
        assertTrue(associator.registerExercise("Collision", "Monday", strengthInfo1));
        assertFalse(associator.registerExercise("Collision", "Monday", strengthInfo2));
        assertTrue(associator.registerExercise("Collision", "Tuesday", strengthInfo2));
    
        // empty string context
        associator.clearExercises();
        assertTrue(associator.registerExercise("Exercise", "", mixedInfo));
        assertTrue(associator.containsExercise("Exercise", ""));
    
        // whitespace context
        associator.clearExercises();
        assertTrue(associator.registerExercise("Exercise", "   ", mixedInfo));
        assertTrue(associator.containsExercise("Exercise", "   "));
    
        associator.clearExercises();
        // special character contexts
        String[] specialContexts = {"Monday!", "Tuesday@", "Wednesday#", "Thursday$", "Friday%"};
        for (String context : specialContexts) {
            assertTrue(associator.registerExercise("SpecialContext", context, strengthInfo1));
            assertTrue(associator.containsExercise("SpecialContext", context));
        }
    }
    
    // Specifically for context based functionality
    private void testContextSpecificBehavior(ExerciseAssociator associator) {
        associator.clearExercises();
        // same exercise in different contexts
        assertTrue(associator.registerExercise("BenchPress", "Monday", strengthInfo1));
        assertTrue(associator.registerExercise("BenchPress", "Wednesday", strengthInfo2));
        assertEquals(2, associator.getNumAssociatedExercises());
    
        // context-specific containment
        assertTrue(associator.containsExercise("BenchPress", "Monday"));
        assertTrue(associator.containsExercise("BenchPress", "Wednesday"));
        assertFalse(associator.containsExercise("BenchPress", "Friday"));
    
        // context-specific unregistration
        assertTrue(associator.unregisterExercise("BenchPress", "Monday"));
        assertFalse(associator.containsExercise("BenchPress", "Monday"));
        assertTrue(associator.containsExercise("BenchPress", "Wednesday"));
    
        // metric isolation between contexts
        Map<String, Double> metrics = associator.getAggregatedExerciseMetrics();
        assertEquals(strengthInfo2.get("totalSets"), metrics.get("totalSets"), TEST_PRECISION);
        assertEquals(strengthInfo2.get("totalReps"), metrics.get("totalReps"), TEST_PRECISION);
    }
}