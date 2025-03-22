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

/* This class tests the immutable (RestDay is immutable), shared, and modification aspects of 
 * WorkoutPlan extensions, and also verifies Workout's removeExercise works as intended.
 * 
 * NOTE: A Workout's list of Exercise is modified IF AND ONLY IF an Exercise from ExerciseLibrary
 * is removed and it is in a Workout object that is instantiated within the program at runtime. 
 * This modification is done at the UI level. It prevents complex exception handling and is less 
 * problematic for data persistence implementation. */
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
        singleExerciseList = new ArrayList<Exercise>();
        singleExerciseList.add(benchPress);

        multiExerciseList = new ArrayList<Exercise>();
        multiExerciseList.add(benchPress);
        multiExerciseList.add(running);
        multiExerciseList.add(hiit);

        emptyExerciseList = new ArrayList<Exercise>();
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
        
        // rest day properties
        assertTrue(validRest.getExercises().isEmpty());
        assertEquals(0.0, validRest.getDuration(), TEST_PRECISION);
        assertTrue(validRest.getWorkoutSummary().isEmpty());
        
        // metric operations have no effect
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
        List<Exercise> nullExerciseList = new ArrayList<Exercise>();
        nullExerciseList.add(benchPress);
        nullExerciseList.add(null);
        assertThrows(IllegalArgumentException.class, () -> new Workout("Test", nullExerciseList));
    }

    @Test
    void testRemoveExerciseExisting() {
        Workout workout = new Workout("Test Workout", multiExerciseList);
        assertEquals(3, workout.getExercises().size());
        
        // Remove an existing exercise
        workout.removeExercise("Running");
        
        // exercise was removed
        List<Exercise> exercises = workout.getExercises();
        assertEquals(2, exercises.size());
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("HIIT", exercises.get(1).getName());
    }
    
    @Test
    void testRemoveExerciseNonExisting() {
        Workout workout = new Workout("Test Workout", multiExerciseList);
        assertEquals(3, workout.getExercises().size());
        
        // remove a non-existing exercise
        workout.removeExercise("Non-existing Exercise");
        
        // no changes
        List<Exercise> exercises = workout.getExercises();
        assertEquals(3, exercises.size());
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("Running", exercises.get(1).getName());
        assertEquals("HIIT", exercises.get(2).getName());
    }
    
    @Test
    void testAddExerciseNew() {
        Workout workout = new Workout("Test Workout", singleExerciseList);
        assertEquals(1, workout.getExercises().size());
        
        // Add a new exercise
        workout.addExercise(running);
        
        // exercise was added
        List<Exercise> exercises = workout.getExercises();
        assertEquals(2, exercises.size());
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("Running", exercises.get(1).getName());
    }
    
    @Test
    void testAddExerciseDuplicate() {
        Workout workout = new Workout("Test Workout", singleExerciseList);
        assertEquals(1, workout.getExercises().size());
        
        // add a duplicate exercise (same name)
        StrengthExercise duplicateBenchPress = new StrengthExercise(
                "Bench Press", 3, 10, 3.0, 1.5, dumbbell, chest);
        workout.addExercise(duplicateBenchPress);
        
        // no changes (duplicate not added)
        List<Exercise> exercises = workout.getExercises();
        assertEquals(1, exercises.size());
        assertEquals("Bench Press", exercises.get(0).getName());
        // it's the original exercise (by checking reps)
        StrengthExercise retrievedExercise = (StrengthExercise) exercises.get(0);
        assertEquals(12, retrievedExercise.getInfo().get("reps"));
    }
    
    @Test
    void testAddExerciseNull() {
        Workout workout = new Workout("Test Workout", singleExerciseList);
        
        // add a null exercise
        try {
            workout.addExercise(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        
        // no changes
        List<Exercise> exercises = workout.getExercises();
        assertEquals(1, exercises.size());
    }
    
    @Test
    void testSetExercisesValid() {
        Workout workout = new Workout("Test Workout", singleExerciseList);
        assertEquals(1, workout.getExercises().size());
        
        // Set new exercises
        workout.setExercises(multiExerciseList);
        
        // exercises were updated
        List<Exercise> exercises = workout.getExercises();
        assertEquals(3, exercises.size());
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("Running", exercises.get(1).getName());
        assertEquals("HIIT", exercises.get(2).getName());
    }
    
    @Test
    void testSetExercisesEmpty() {
        Workout workout = new Workout("Test Workout", multiExerciseList);
        assertEquals(3, workout.getExercises().size());
        
        // Set empty exercise list
        workout.setExercises(emptyExerciseList);
        
        // exercises were updated to empty
        List<Exercise> exercises = workout.getExercises();
        assertTrue(exercises.isEmpty());
    }
    
    @Test
    void testSetExercisesNull() {
        Workout workout = new Workout("Test Workout", singleExerciseList);
        
        // set null exercise list
        try {
            workout.setExercises(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // no changes
        List<Exercise> exercises = workout.getExercises();
        assertEquals(1, exercises.size());
        assertEquals(benchPress, exercises.get(0));

    }

    @Test
    void testSetExercisesIncludesNull() {
        List<Exercise> nullList = new ArrayList<Exercise>(singleExerciseList);
        Workout workout = new Workout("Test Workout", singleExerciseList);
        
        // includes null in exercise list
        try {
            nullList.add(null);
            workout.setExercises(nullList);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // no changes
        List<Exercise> exercises = workout.getExercises();
        assertEquals(1, exercises.size());
        assertEquals(benchPress, exercises.get(0));
    }
    
    @Test
    void testComplexWorkoutModification() {
        // Test a series of modifications to ensure consistency
        Workout workout = new Workout("Complex Workout", multiExerciseList);
        assertEquals(3, workout.getExercises().size());
        
        // Remove one exercise
        workout.removeExercise("Running");
        assertEquals(2, workout.getExercises().size());

        Exercise extraExercise = new IntervalExercise("Push-ups", 10, 10, 30, bodyweight, chest);
        
        // Add a new exercise
        workout.addExercise(extraExercise);
        assertEquals(3, workout.getExercises().size());
        
        // add duplicate (should be ignored)
        workout.addExercise(benchPress);
        assertEquals(3, workout.getExercises().size());
        
        // final state
        List<Exercise> exercises = workout.getExercises();
        assertEquals("Bench Press", exercises.get(0).getName());
        assertEquals("HIIT", exercises.get(1).getName());
        assertEquals("Push-ups", exercises.get(2).getName());
        
        // Replace with a completely new set
        List<Exercise> newList = new ArrayList<>();
        newList.add(running);
        workout.setExercises(newList);
        
        // final state after setExercises
        exercises = workout.getExercises();
        assertEquals(1, exercises.size());
        assertEquals("Running", exercises.get(0).getName());
    }

    @Test
    void testWorkoutMetricActivationWithContext() {
        Workout workout = new Workout("Test", multiExerciseList);

        // Activate same workout in different contexts
        workout.activateMetrics(MONDAY_CONTEXT);
        workout.activateMetrics(WEDNESDAY_CONTEXT);
        
        // equipment registrations
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", WEDNESDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", WEDNESDAY_CONTEXT));
        
        // muscle group metrics
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
        
        // Monday metrics removed but Wednesday remains
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", MONDAY_CONTEXT));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", WEDNESDAY_CONTEXT));
        
        // correct metrics remain
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
        
        // equipment metrics
        Map<String, Double> dumbbellMetrics = ((ExerciseAssociator) dumbbell).getAggregatedExerciseMetrics();
        assertEquals(8.0, dumbbellMetrics.get("totalSets"), TEST_PRECISION);
        
        Map<String, Double> treadmillMetrics = ((ExerciseAssociator) treadmill).getAggregatedExerciseMetrics();
        assertEquals(3600.0, treadmillMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
        
        // muscle group metrics
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
        assertEquals(4.0 * 12.0, summary.get("totalReps"), TEST_PRECISION);
        assertEquals(600.0, summary.get("totalStrengthDuration"), TEST_PRECISION);
        
        // endurance metrics from running
        assertEquals(1800.0, summary.get("totalEnduranceDuration"), TEST_PRECISION);
        
        // interval metrics from HIIT
        assertEquals(450.0, summary.get("totalIntervalDuration"), TEST_PRECISION);
        
        // combined rest time (strength rest + interval rest)
        assertEquals(630.0, summary.get("totalRestTimeBetween"), TEST_PRECISION);
        
        // total duration
        double expectedTotalDuration = benchPress.getDuration() 
                +  running.getDuration() + hiit.getDuration();
        assertEquals(expectedTotalDuration, summary.get("totalDuration"), TEST_PRECISION);
        
        // empty workout
        Workout emptyWorkout = new Workout("Empty", emptyExerciseList);
        Map<String, Double> emptySummary = emptyWorkout.getWorkoutSummary();
        assertEquals(0.0, emptySummary.get("totalSets"), TEST_PRECISION);
        assertEquals(0.0, emptySummary.get("totalDuration"), TEST_PRECISION);
    }

    @Test
    void testExerciseRemovalFromWorkouts() {
        // Same values except name
        Exercise exercise1 = new StrengthExercise("Exercise1", 3, 12, 2.0, 1.5, bodyweight, chest);
        Exercise exercise2 = new StrengthExercise("Exercise2", 3, 12, 2.0, 1.5, bodyweight, chest);
        
        List<Exercise> exercises = new ArrayList<Exercise>();
        exercises.add(exercise1);
        exercises.add(exercise2);
        
        Workout workout = new Workout("TestWorkout", exercises);
    
        // Initial state
        assertEquals(2, workout.getExercises().size());
        assertTrue(workout.getExercises().contains(exercise1));
        
        // Simulate exercise deletion and cleanup
        workout.removeExercise("Exercise1");
        
        // Correct exercise was removed
        assertEquals(1, workout.getExercises().size());
        assertFalse(workout.getExercises().contains(exercise1));
        assertTrue(workout.getExercises().contains(exercise2));
    }
}