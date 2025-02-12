package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.association.ExerciseAssociator;
import model.equipment.strength.*;
import model.equipment.cardio.Treadmill;
import model.equipment.bodyweight.BodyWeight;
import model.muscle.Muscle;

public class TestExerciseAssociator {
    // Equipment implementations
    private ExerciseAssociator barbell;
    private ExerciseAssociator cable;
    private ExerciseAssociator dumbbell;
    private ExerciseAssociator machine;
    private ExerciseAssociator treadmill;
    private ExerciseAssociator bodyweight;
    
    // Muscle implementations
    private ExerciseAssociator bicep;
    private ExerciseAssociator quad;
    // Mock exercise data maps
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

    /* ExerciseAssociator Tests for each subclass/implementation of ExerciseAssociator
       If one implementation overrides ExerciseAssociator, this test suite formation detects that
       Equipments/muscles are split to directly see which subclass potenttially (should they override
       ExerciseAssociator's methods) has an incorrect implementation  */


    // STRENGTH EQUIPMENT
    @Test
    void testBarbellAssociator() {
        testRegistrationHelper(barbell);
        testUnregistrationHelper(barbell);
        testMetricAggregationHelper(barbell);
        testEdgeCasesHelper(barbell);
    }

    @Test
    void testDumbbellAssociator() {
        testRegistrationHelper(dumbbell);
        testUnregistrationHelper(dumbbell);
        testMetricAggregationHelper(dumbbell);
        testEdgeCasesHelper(dumbbell);
    }

    @Test
    void testCableAssociator() {
        testRegistrationHelper(cable);
        testUnregistrationHelper(cable);
        testMetricAggregationHelper(cable);
        testEdgeCasesHelper(cable);
    }

    @Test
    void testMachineAssociator() {
        testRegistrationHelper(machine);
        testUnregistrationHelper(machine);
        testMetricAggregationHelper(machine);
        testEdgeCasesHelper(machine);
    }


    // CARDIO EQUIPMENT
    @Test
    void testTreadmillAssociator() {
        testRegistrationHelper(treadmill);
        testUnregistrationHelper(treadmill);
        testMetricAggregationHelper(treadmill);
        testEdgeCasesHelper(treadmill);
    }

    // BODY WEIGHT
    @Test
    void testBodyweightAssociator() {
        testRegistrationHelper(bodyweight);
        testUnregistrationHelper(bodyweight);
        testMetricAggregationHelper(bodyweight);
        testEdgeCasesHelper(bodyweight);
    }


    // MUSCLES
    @Test
    void testBicepMuscleAssociator() {
        testRegistrationHelper(bicep);
        testUnregistrationHelper(bicep);
        testMetricAggregationHelper(bicep);
        testEdgeCasesHelper(bicep);
    }

    @Test
    void testQuadMuscleAssociator() {
        testRegistrationHelper(quad);
        testUnregistrationHelper(quad);
        testMetricAggregationHelper(quad);
        testEdgeCasesHelper(quad);
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
    }

    private void testRegistrationHelper(ExerciseAssociator associator) {
        // Valid registration
        assertTrue(associator.registerExercise("Exercise1", strengthInfo1));
        assertEquals(1, associator.getNumAssociatedExercises());
        assertTrue(associator.containsExercise("Exercise1"));

        // Duplicate registration
        assertFalse(associator.registerExercise("Exercise1", strengthInfo2));
        assertEquals(1, associator.getNumAssociatedExercises());

        // Null exercise name
        assertFalse(associator.registerExercise(null, strengthInfo1));
        assertEquals(1, associator.getNumAssociatedExercises());

        // Null exercise info
        assertFalse(associator.registerExercise("Exercise2", null));
        assertEquals(1, associator.getNumAssociatedExercises());

        // Registration with invalid data
        assertTrue(associator.registerExercise("Exercise3", invalidInfo));
        assertEquals(2, associator.getNumAssociatedExercises());

        // Registration with empty data
        assertTrue(associator.registerExercise("Exercise4", emptyInfo));
        assertEquals(3, associator.getNumAssociatedExercises());

        // Registration with partial data (should work: implements 'familiar' metrics)
        assertTrue(associator.registerExercise("Exercise5", partialInfo));
        assertEquals(4, associator.getNumAssociatedExercises());

        // Registration with mixed valid/invalid data (should work: implements 'familiar' metrics)
        assertTrue(associator.registerExercise("Exercise6", mixedInfo));
        assertEquals(5, associator.getNumAssociatedExercises());
    }

    private void testUnregistrationHelper(ExerciseAssociator associator) {
        // Setup initial exercises
        associator.registerExercise("Exercise1", strengthInfo1);
        associator.registerExercise("Exercise2", enduranceInfo1);
        associator.registerExercise("Exercise3", intervalInfo1);

        // Valid unregistration
        assertTrue(associator.unregisterExercise("Exercise1"));
        assertEquals(2, associator.getNumAssociatedExercises());
        assertFalse(associator.containsExercise("Exercise1"));

        // Unregistering non-existent exercise
        assertFalse(associator.unregisterExercise("NonExistentExercise"));
        assertEquals(2, associator.getNumAssociatedExercises());

        // Unregistering with null
        assertFalse(associator.unregisterExercise(null));
        assertEquals(2, associator.getNumAssociatedExercises());

        // Unregistering all exercises
        assertTrue(associator.unregisterExercise("Exercise2"));
        assertTrue(associator.unregisterExercise("Exercise3"));
        assertEquals(0, associator.getNumAssociatedExercises());
    }

    // @SuppressWarnings("methodLength")
    private void testMetricAggregationHelper(ExerciseAssociator associator) {
        // Register different types of exercises
        associator.registerExercise("StrengthEx1", strengthInfo1);
        associator.registerExercise("StrengthEx2", strengthInfo2);
        associator.registerExercise("EnduranceEx1", enduranceInfo1);
        associator.registerExercise("IntervalEx1", intervalInfo1);

        Map<String, Double> metrics = associator.getAggregatedExerciseMetrics();

        // Calculate expected metrics explicitly for comparison
        Double expectedSets = strengthInfo1.get("totalSets")
                + strengthInfo2.get("totalSets");

        Double expectedReps = strengthInfo1.get("totalReps")
                + strengthInfo2.get("totalReps");

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

        // Strength metrics
        assertEquals(expectedSets, metrics.get("totalSets"), 0.1);
        assertEquals(expectedReps, metrics.get("totalReps"), 0.1);
        assertEquals(expectedStrengthDuration, metrics.get("totalStrengthDuration"), 0.1);
        
        // Endurance metrics
        assertEquals(expectedEnduranceDuration, metrics.get("totalEnduranceDuration"), 0.1);

        // Interval metrics
        assertEquals(expectedIntervalDuration, metrics.get("totalIntervalDuration"), 0.1);
        
        // Combined metrics
        assertEquals(expectedRestTime, metrics.get("totalRestTimeBetween"), 0.1);
        assertEquals(expectedTotalDuration, metrics.get("totalDuration"), 0.1);

        // Empty metrics
        associator.unregisterExercise("StrengthEx1");
        associator.unregisterExercise("StrengthEx2");
        associator.unregisterExercise("EnduranceEx1");
        associator.unregisterExercise("IntervalEx1");
        
        Map<String, Double> emptyMetrics = associator.getAggregatedExerciseMetrics();
        assertEquals(0.0, emptyMetrics.get("totalSets"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalReps"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalStrengthDuration"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalEnduranceDuration"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalIntervalDuration"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalRestTimeBetween"), 0.1);
        assertEquals(0.0, emptyMetrics.get("totalDuration"), 0.1);
    }

    // @SuppressWarnings("methodLength")
    private void testEdgeCasesHelper(ExerciseAssociator associator) {
        // Registration with mixed valid/invalid data
        associator.registerExercise("MixedEx", mixedInfo);
        Map<String, Double> metrics = associator.getAggregatedExerciseMetrics();
        
        // Should only count valid metrics
        // Recall that mixedInfo has totalSets and totalReps which are metrics that ExerciseAssociator
        // looks for. It ignores the other 'unfamiliar' metrics
        assertEquals(4.3, metrics.get("totalSets"), 0.1);
        assertEquals(10.2, metrics.get("totalReps"), 0.1);
        
        // Extreme values
        Map<String, Double> extremeValues = new HashMap<>();
        extremeValues.put("totalSets", Double.MAX_VALUE);
        extremeValues.put("totalReps", Double.MIN_VALUE);
        extremeValues.put("totalDuration", 1e-20);  // EXTREMELY small decimal
        assertTrue(associator.registerExercise("Extreme", extremeValues));
        
        metrics = associator.getAggregatedExerciseMetrics();
        assertEquals(Double.MAX_VALUE, metrics.get("totalSets"), 0.1);
        assertEquals(Double.MIN_VALUE, metrics.get("totalReps"), 0.1);
        assertEquals(1e-10, metrics.get("totalDuration"), 1e-11);

        // Multiple register/unregister cycles
        for (int i = 0; i < 100; i++) {
            String exerciseName = "CycleEx" + i;
            assertTrue(associator.registerExercise(exerciseName, strengthInfo1));
            assertTrue(associator.containsExercise(exerciseName));
            if (i % 2 == 0) {
                assertTrue(associator.unregisterExercise(exerciseName));
                assertFalse(associator.containsExercise(exerciseName));
            }
        }
        
        // Verify metrics consistency after cycles
        metrics = associator.getAggregatedExerciseMetrics();
        Double expectedSets = strengthInfo1.get("totalSets") * 50;  // 50 exercises remain from loop
        assertEquals(expectedSets, metrics.get("totalSets"), 0.1);
        
        // Test handling of empty exercise data
        Map<String, Double> emptyMap = new HashMap<>();
        // Store current metrics before adding empty exercise
        Map<String, Double> metricsBeforeEmpty = associator.getAggregatedExerciseMetrics();
        
        // Register empty exercise
        assertTrue(associator.registerExercise("EmptyEx", emptyMap));
        
        // Get metrics after adding empty exercise
        Map<String, Double> metricsAfterEmpty = associator.getAggregatedExerciseMetrics();
        
        // Verify all metrics remain unchanged when adding empty exercise data
        assertEquals(metricsBeforeEmpty.get("totalSets"), metricsAfterEmpty.get("totalSets"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalReps"), metricsAfterEmpty.get("totalReps"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalStrengthDuration"), 
                metricsAfterEmpty.get("totalStrengthDuration"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalEnduranceDuration"), 
                metricsAfterEmpty.get("totalEnduranceDuration"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalIntervalDuration"), 
                metricsAfterEmpty.get("totalIntervalDuration"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalRestTimeBetween"), 
                metricsAfterEmpty.get("totalRestTimeBetween"), 0.1);
        assertEquals(metricsBeforeEmpty.get("totalDuration"), 
                metricsAfterEmpty.get("totalDuration"), 0.1);
    }    
}