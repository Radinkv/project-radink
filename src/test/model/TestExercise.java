package model;

import static org.junit.jupiter.api.Assertions.*;
import static utility.Utility.TEST_PRECISION;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.exercise.*;
import model.association.ExerciseAssociator;
import model.equipment.*;
import model.equipment.strength.*;
import model.equipment.cardio.*;
import model.equipment.bodyweight.*;
import model.muscle.*;

import java.util.List;
import java.util.Map;

/** This class tests the IMMUTABLE aspects and modification operations of 
 * Exercise subclass implementations and instantiations. */
class TestExercise {
    private Equipment dumbbell;
    private Equipment treadmill;
    private Equipment bodyweight;
    private Muscle pec;
    private Muscle frontDelt;
    private Muscle quad;
    private MuscleGroup chest;
    private MuscleGroup legs;
    
    private StrengthExercise benchPress;
    private EnduranceExercise running;
    private IntervalExercise hiit;
    
    @BeforeEach
    void runBefore() {
        initializeEquipment();
        initializeMuscles();
        initializeExercises();
    }

    private void initializeEquipment() {
        dumbbell = new Dumbbell();
        treadmill = new Treadmill();
        bodyweight = new BodyWeight();
    }

    private void initializeMuscles() {
        pec = new Muscle("Pectoralis Major");
        frontDelt = new Muscle("Anterior Deltoid");
        quad = new Muscle("Quadriceps");
        chest = new MuscleGroup("Chest", List.of(pec, frontDelt));
        legs = new MuscleGroup("Legs", List.of(quad));
    }

    private void initializeExercises() {
        benchPress = new StrengthExercise("Bench Press", 4, 12, 2.5, 2.0, dumbbell, chest);
        running = new EnduranceExercise("Running", 30.0, treadmill, legs);
        hiit = new IntervalExercise("HIIT", 30.0, 15.0, 10, bodyweight, legs);
    }

    @Test
    void testConstructorNormalValues() {
        assertEquals("Bench Press", benchPress.getName());
        assertEquals("Running", running.getName());
        assertEquals("HIIT", hiit.getName());
        
        Map<String, Double> strengthInfo = benchPress.getInfo();
        assertEquals(4.0, strengthInfo.get("sets"));
        assertEquals(12.0, strengthInfo.get("reps"));
        assertEquals(2.5, strengthInfo.get("timePerRep"));
        assertEquals(2.0, strengthInfo.get("restTime"));
        
        Map<String, Double> enduranceInfo = running.getInfo();
        assertEquals(30.0, enduranceInfo.get("duration"));
        
        Map<String, Double> intervalInfo = hiit.getInfo();
        assertEquals(30.0, intervalInfo.get("timeOn"));
        assertEquals(15.0, intervalInfo.get("timeOff"));
        assertEquals(10.0, intervalInfo.get("repititions"));
    }

    @Test
    void testExerciseAbstractGetters() {
        // Can use any Exercise subclass (StrengthExercise in this case)
        Exercise exercise = new StrengthExercise(
                "TestExercise", 1, 1, 1.0, 1.0, dumbbell, chest);
        
        // Testing Getters
        // Equipment Instances are Shared So No Potential Defensive Copy Refactorization
        assertEquals(dumbbell, exercise.getRequiredEquipment());
        assertEquals(chest, exercise.getMusclesTargeted());

        // For Defensive Copy/MuscleGroup Immutability Refactorization
        assertTrue(chest.getMuscles().equals(exercise.getMusclesTargeted().getMuscles()));

        exercise = new StrengthExercise(
            null, 1, 1, 1.0, 1.0, dumbbell, chest);
        
        assertEquals("Unnamed Exercise", exercise.getName());

        // Exercise Absraction null behaviour
        exercise = new EnduranceExercise(1);
        assertEquals("Unknown Type", exercise.exerciseType());

        exercise = new EnduranceExercise(2);
        assertEquals("Unknown Type", exercise.exerciseType());

    }

    @Test
    void testConstructorEdgeCases() {
        // extremely large values
        StrengthExercise maxStrength = new StrengthExercise(
                "Max", Integer.MAX_VALUE, Integer.MAX_VALUE, 
                Double.MAX_VALUE, Double.MAX_VALUE, dumbbell, chest);
        EnduranceExercise maxEndurance = new EnduranceExercise(
                "Max", Double.MAX_VALUE, treadmill, legs);
        IntervalExercise maxInterval = new IntervalExercise(
                "Max", Double.MAX_VALUE, Double.MAX_VALUE, 
                    Integer.MAX_VALUE, bodyweight, legs);
        
        assertTrue(maxStrength.getDuration() < Double.POSITIVE_INFINITY);
        assertTrue(maxEndurance.getDuration() < Double.POSITIVE_INFINITY);
        assertTrue(maxInterval.getDuration() < Double.POSITIVE_INFINITY);
        
        // negative values
        StrengthExercise negStrength = new StrengthExercise(
                "Neg", -1, -1, -1.0, -1.0, dumbbell, chest);
        EnduranceExercise negEndurance = new EnduranceExercise(
                "Neg", -1.0, treadmill, legs);
        IntervalExercise negInterval = new IntervalExercise(
                "Neg", -1.0, -1.0, -1, bodyweight, legs);
        
        assertTrue(negStrength.getDuration() >= 0);
        assertTrue(negEndurance.getDuration() >= 0);
        assertTrue(negInterval.getDuration() >= 0);
    }

    @Test
    void testNullAndEmptyConstructors() {
        // null names
        StrengthExercise nullNameStrength = new StrengthExercise(
                null, 1, 1, 1.0, 1.0, dumbbell, chest);
        EnduranceExercise nullNameEndurance = new EnduranceExercise(
                null, 1.0, treadmill, legs);
        IntervalExercise nullNameInterval = new IntervalExercise(
                null, 1.0, 1.0, 1, bodyweight, legs);
        
        assertNotNull(nullNameStrength.getName());
        assertNotNull(nullNameEndurance.getName());
        assertNotNull(nullNameInterval.getName());
        
        // empty names
        StrengthExercise emptyNameStrength = new StrengthExercise(
                "", 1, 1, 1.0, 1.0, dumbbell, chest);
        EnduranceExercise emptyNameEndurance = new EnduranceExercise(
                "", 1.0, treadmill, legs);
        IntervalExercise emptyNameInterval = new IntervalExercise(
                "", 1.0, 1.0, 1, bodyweight, legs);
        
        assertFalse(emptyNameStrength.getName().isEmpty());
        assertFalse(emptyNameEndurance.getName().isEmpty());
        assertFalse(emptyNameInterval.getName().isEmpty());
    }

    @Test
    void testMetricActivation() {
        String context = "Monday";
        
        benchPress.activateMetrics(context);
        running.activateMetrics(context);
        hiit.activateMetrics(context);
        
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", context));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", context));
        assertTrue(((ExerciseAssociator) bodyweight).containsExercise("HIIT", context));
        
        Map<String, Double> chestMetrics = chest.getGroupMetrics();
        Map<String, Double> legsMetrics = legs.getGroupMetrics();
        
        assertNotNull(chestMetrics.get("totalSets"));
        assertNotNull(legsMetrics.get("totalEnduranceDuration"));
        assertNotNull(legsMetrics.get("totalIntervalDuration"));
    }

    @Test
    void testMetricDeactivation() {
        String context = "Monday";
        
        // Activate and then deactivate metrics
        benchPress.activateMetrics(context);
        running.activateMetrics(context);
        hiit.activateMetrics(context);
        
        benchPress.deactivateMetrics(context);
        running.deactivateMetrics(context);
        hiit.deactivateMetrics(context);
        
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", context));
        assertFalse(((ExerciseAssociator) treadmill).containsExercise("Running", context));
        assertFalse(((ExerciseAssociator) bodyweight).containsExercise("HIIT", context));
    }

    @Test
    void testInvalidContextHandling() {
        // null context
        benchPress.activateMetrics(null);
        running.activateMetrics(null);
        hiit.activateMetrics(null);
        
        // empty context
        benchPress.activateMetrics("");
        running.activateMetrics("");
        hiit.activateMetrics("");
        
        // whitespace context
        benchPress.activateMetrics("   ");
        running.activateMetrics("   ");
        hiit.activateMetrics("   ");
        
        // no metrics should have been registered
        // REMEMBER: this behaviour is ALLOWED: null-string contexts
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", ""));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", ""));
        assertTrue(((ExerciseAssociator) bodyweight).containsExercise("HIIT", ""));
    }

    @Test
    void testDurationCalculations() {
        // strength exercise duration
        double strengthDuration = benchPress.getDuration();
        double expectedStrengthDuration = 4 * (12 * 2.5 + 2.0 * 60); // sets * (reps * secsPerRep + restTime (seconds)) 
        assertEquals(expectedStrengthDuration, strengthDuration, TEST_PRECISION);
        
        // endurance exercise duration
        double enduranceDuration = running.getDuration();
        double expectedEnduranceDuration = 30.0 * 60;
        assertEquals(expectedEnduranceDuration, enduranceDuration, TEST_PRECISION);
        
        // interval exercise duration
        double intervalDuration = hiit.getDuration();
        double expectedIntervalDuration = 10 * (30.0 + 15.0);
        assertEquals(expectedIntervalDuration, intervalDuration, TEST_PRECISION);
    }

    @Test
    void testMultipleContextMetrics() {
        String context1 = "Monday";
        String context2 = "Wednesday";
        
        // Activate metrics in multiple contexts
        benchPress.activateMetrics(context1);
        benchPress.activateMetrics(context2);
        running.activateMetrics(context1);
        running.activateMetrics(context2);
        hiit.activateMetrics(context1);
        hiit.activateMetrics(context2);
        
        // metrics are tracked respectively
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", context1));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", context2));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", context1));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", context2));
        assertTrue(((ExerciseAssociator) bodyweight).containsExercise("HIIT", context1));
        assertTrue(((ExerciseAssociator) bodyweight).containsExercise("HIIT", context2));
    }

    @Test
    void testNullEquipmentAndMuscleGroup() {
        StrengthExercise nullCompStrength = new StrengthExercise(
                "Null", 1, 1, 1.0, 1.0, null, null);
        EnduranceExercise nullCompEndurance = new EnduranceExercise(
                "Null", 1.0, null, null);
        IntervalExercise nullCompInterval = new IntervalExercise(
                "Null", 1.0, 1.0, 1, null, null);
        
        String context = "Monday";
        
        // no exceptions when activating/deactivating metrics
        nullCompStrength.activateMetrics(context);
        nullCompEndurance.activateMetrics(context);
        nullCompInterval.activateMetrics(context);
        
        nullCompStrength.deactivateMetrics(context);
        nullCompEndurance.deactivateMetrics(context);
        nullCompInterval.deactivateMetrics(context);
        
        // getInfo still works
        assertNotNull(nullCompStrength.getInfo());
        assertNotNull(nullCompEndurance.getInfo());
        assertNotNull(nullCompInterval.getInfo());
    }

    @Test
    void testExerciseTypeConsistency() {
        assertEquals("Strength", benchPress.exerciseType());
        assertEquals("Endurance", running.exerciseType());
        assertEquals("Interval", hiit.exerciseType());
    }
}