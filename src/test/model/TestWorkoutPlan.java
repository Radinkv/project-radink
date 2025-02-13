package model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.workout.*;
import model.exercise.*;
import model.association.ExerciseAssociator;
import model.equipment.*;
import model.equipment.strength.*;
import model.equipment.cardio.*;
import model.equipment.bodyweight.*;
import model.muscle.*;

import static utility.Utility.TEST_PRECISION;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestWorkoutPlan {
    private Equipment dumbbell;
    private Equipment treadmill;
    private Equipment bodyweight;
    private MuscleGroup chest;
    private MuscleGroup legs;
    
    private StrengthExercise benchPress;
    private EnduranceExercise running;
    private IntervalExercise hiit;
    
    private static final String MONDAY_CONTEXT = "Monday";
    private static final String WEDNESDAY_CONTEXT = "Wednesday";
    
    private List<Exercise> singleExerciseList;
    private List<Exercise> multiExerciseList;
    private List<Exercise> emptyExerciseList;
    
    @BeforeEach
    void runBefore() {
        initializeEquipmentAndMuscles();
        initializeExercises();
        initializeExerciseLists();
    }

    private void initializeEquipmentAndMuscles() {
        dumbbell = new Dumbbell();
        treadmill = new Treadmill();
        bodyweight = new BodyWeight();
        
        List<Muscle> chestMuscles = List.of(new Muscle("Pectoralis"), new Muscle("Anterior Deltoid"));
        chest = new MuscleGroup("Chest", chestMuscles);
        
        List<Muscle> legMuscles = List.of(new Muscle("Quadriceps"), new Muscle("Hamstrings"));
        legs = new MuscleGroup("Legs", legMuscles);
    }

    private void initializeExercises() {
        benchPress = new StrengthExercise("Bench Press", 4, 12, 2.5, 2.0, dumbbell, chest);
        running = new EnduranceExercise("Running", 30.0, treadmill, legs);
        hiit = new IntervalExercise("HIIT", 30.0, 15.0, 10, bodyweight, legs);
    }

    private void initializeExerciseLists() {
        singleExerciseList = new ArrayList<>();
        singleExerciseList.add(benchPress);

        multiExerciseList = new ArrayList<>();
        multiExerciseList.add(benchPress);
        multiExerciseList.add(running);
        multiExerciseList.add(hiit);

        emptyExerciseList = new ArrayList<>();
    }

    @Test
    void testRestDayConstructorAndBehavior() {
        // valid construction
        RestDay validRest = new RestDay("Active Recovery");
        assertEquals("Active Recovery", validRest.getName());
        
        // empty string
        RestDay emptyRest = new RestDay("");
        assertEquals("", emptyRest.getName());
        
        // null name
        assertThrows(IllegalArgumentException.class, () -> new RestDay(null));
        
        // Verify rest day properties
        assertTrue(validRest.getExercises().isEmpty());
        assertEquals(0.0, validRest.getDuration(), TEST_PRECISION);
        assertTrue(validRest.getWorkoutSummary().isEmpty());
        
        // Verify metric operations have no effect
        validRest.activateMetrics(MONDAY_CONTEXT);
        validRest.deactivateMetrics(MONDAY_CONTEXT);
        assertTrue(validRest.getWorkoutSummary().isEmpty());
    }

    @Test
    void testWorkoutConstructorValidation() {
        // valid construction
        Workout validWorkout = new Workout("Test", singleExerciseList);
        assertEquals("Test", validWorkout.getName());
        assertEquals(1, validWorkout.getExercises().size());
        
        // null name
        assertThrows(IllegalArgumentException.class, () -> new Workout(null, singleExerciseList));
        
        // null exercise list
        assertThrows(IllegalArgumentException.class, () -> new Workout("Test", null));
        
        // list containing null exercise
        List<Exercise> nullExerciseList = new ArrayList<>();
        nullExerciseList.add(benchPress);
        nullExerciseList.add(null);
        assertThrows(IllegalArgumentException.class, () -> new Workout("Test", nullExerciseList));
    }

    @Test
    void testWorkoutMetricActivationWithContext() {
        Workout workout = new Workout("Test", multiExerciseList);
        
        // Activate same workout in different contexts
        workout.activateMetrics(MONDAY_CONTEXT);
        workout.activateMetrics(WEDNESDAY_CONTEXT);
        
        // Verify equipment registrations
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", WEDNESDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", WEDNESDAY_CONTEXT));
        
        // Verify muscle group metrics
        Map<String, Double> chestMetrics = chest.getGroupMetrics();
        assertEquals(8.0, chestMetrics.get("totalSets"), TEST_PRECISION); // 4 sets * 2 contexts
        
        Map<String, Double> legMetrics = legs.getGroupMetrics();
        assertEquals(3600.0, legMetrics.get("totalEnduranceDuration"), TEST_PRECISION); // 30 mins * 60 * 2
    }

    @Test
    void testWorkoutMetricDeactivationWithContext() {
        Workout workout = new Workout("Test", multiExerciseList);
        
        // Activate in both contexts
        workout.activateMetrics(MONDAY_CONTEXT);
        workout.activateMetrics(WEDNESDAY_CONTEXT);
        
        // Deactivate specific context
        workout.deactivateMetrics(MONDAY_CONTEXT);
        
        // Verify Monday metrics removed but Wednesday remains
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", WEDNESDAY_CONTEXT));
        
        // Verify correct metrics remain
        Map<String, Double> chestMetrics = chest.getGroupMetrics();
        assertEquals(4.0, chestMetrics.get("totalSets"), TEST_PRECISION); // Only Wednesday's sets
        
        // Deactivate remaining context
        workout.deactivateMetrics(WEDNESDAY_CONTEXT);
        chestMetrics = chest.getGroupMetrics();
        assertEquals(0.0, chestMetrics.getOrDefault("totalSets", 0.0), TEST_PRECISION);
    }

    @Test
    void testComplexWorkoutMetrics() {
        Workout workout = new Workout("Complex", multiExerciseList);
        
        // Activate in multiple contexts
        workout.activateMetrics(MONDAY_CONTEXT);
        workout.activateMetrics(WEDNESDAY_CONTEXT);
        
        // Verify equipment metrics
        Map<String, Double> dumbbellMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        assertEquals(8.0, dumbbellMetrics.get("totalSets"), TEST_PRECISION);
        
        Map<String, Double> treadmillMetrics = ((ExerciseAssociator) treadmill).getAggregatedExerciseMetrics();
        assertEquals(3600.0, treadmillMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
        
        // Verify muscle group metrics
        Map<String, Double> chestMetrics = chest.getGroupMetrics();
        assertEquals(8.0, chestMetrics.get("totalSets"), TEST_PRECISION);
        
        Map<String, Double> legMetrics = legs.getGroupMetrics();
        assertEquals(3600.0, legMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
        
        // workout summary
        Map<String, Double> summary = workout.getWorkoutSummary();
        double expectedDuration = benchPress.getDuration() + running.getDuration() + hiit.getDuration();
        assertEquals(expectedDuration, summary.get("totalDuration"), TEST_PRECISION);
    }

    @Test
    void testDurationCalculations() {
        // individual workout types
        Workout strengthWorkout = new Workout("Strength", singleExerciseList);
        assertEquals(benchPress.getDuration(), strengthWorkout.getDuration(), TEST_PRECISION);
        
        // empty workout
        Workout emptyWorkout = new Workout("Empty", emptyExerciseList);
        assertEquals(0.0, emptyWorkout.getDuration(), TEST_PRECISION);
        
        // combined workout
        Workout combinedWorkout = new Workout("Combined", multiExerciseList);
        double expectedDuration = benchPress.getDuration() + running.getDuration() + hiit.getDuration();
        assertEquals(expectedDuration, combinedWorkout.getDuration(), TEST_PRECISION);
    }

    @Test
    void testWorkoutSummaryCalculations() {
        // Create workout and get summary
        Workout workout = new Workout("Test", multiExerciseList);
        Map<String, Double> summary = workout.getWorkoutSummary();
        
        // strength metrics from benchPress
        assertEquals(4.0, summary.get("totalSets"), TEST_PRECISION);
        assertEquals(12.0, summary.get("totalReps"), TEST_PRECISION);
        assertEquals(120.0, summary.get("totalStrengthDuration"), TEST_PRECISION);
        
        // endurance metrics from running
        assertEquals(1800.0, summary.get("totalEnduranceDuration"), TEST_PRECISION);
        
        // interval metrics from HIIT
        assertEquals(300.0, summary.get("totalIntervalDuration"), TEST_PRECISION);
        
        // combined rest time (strength rest + interval rest)
        assertEquals(510.0, summary.get("totalRestTimeBetween"), TEST_PRECISION);
        
        // total duration
        double expectedTotalDuration = benchPress.getDuration() + 
                running.getDuration() + hiit.getDuration();
        assertEquals(expectedTotalDuration, summary.get("totalDuration"), TEST_PRECISION);
        
        // empty workout
        Workout emptyWorkout = new Workout("Empty", emptyExerciseList);
        Map<String, Double> emptySummary = emptyWorkout.getWorkoutSummary();
        assertEquals(0.0, emptySummary.get("totalSets"), TEST_PRECISION);
        assertEquals(0.0, emptySummary.get("totalDuration"), TEST_PRECISION);
    }
}