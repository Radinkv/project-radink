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

class TestWeeklySchedule {
    // Equipment for exercises
    private Equipment dumbbell;
    private Equipment treadmill;
    private Equipment bodyweight;
    
    // Muscles and muscle groups
    private Muscle chest;
    private MuscleGroup chestGroup;
    private Muscle quad;
    private MuscleGroup legsGroup;
    
    // Various exercises
    private StrengthExercise benchPress;
    private EnduranceExercise running;
    private IntervalExercise hiit;
    
    // Workout plans
    private Workout strengthWorkout;
    private Workout cardioWorkout;
    private RestDay restDay;
    
    // Weekly schedule
    private WeeklySchedule schedule;


    @BeforeEach
    void runBefore() {
        // Initialize equipment
        dumbbell = new Dumbbell();
        treadmill = new Treadmill();
        bodyweight = new BodyWeight();
        
        // Initialize muscles and groups
        chest = new Muscle("Chest");
        chestGroup = new MuscleGroup("Chest", new ArrayList<Muscle>(List.of(chest)));
        quad = new Muscle("Quadriceps");
        legsGroup = new MuscleGroup("Legs", new ArrayList<Muscle>(List.of(quad)));
        
        // Initialize exercises
        benchPress = new StrengthExercise("Bench Press", 4, 12, 2.5, 2.0, dumbbell, chestGroup);
        running = new EnduranceExercise("Running", 30.0, treadmill, legsGroup);
        hiit = new IntervalExercise("HIIT", 30.0, 15.0, 10, bodyweight, legsGroup);
        
        // Initialize workouts
        strengthWorkout = new Workout("Strength Day", new ArrayList<Exercise>(List.of(benchPress)));
        cardioWorkout = new Workout("Cardio Day", new ArrayList<Exercise>(List.of(running, hiit)));
        restDay = new RestDay("Recovery + Stretching");
        
        // Initialize schedule
        schedule = new WeeklySchedule();
    }

    @Test
    void testInitialScheduleState() {
        List<WorkoutPlan> weeklyPlan = schedule.getWeeklySchedule();
        assertEquals(7, weeklyPlan.size());
        
        // All days should initially be RestDays
        for (int i = 0; i < 7; i++) {
            assertTrue(schedule.getScheduleForDay(i) instanceof RestDay);
            assertEquals("Rest Day", schedule.getScheduleForDay(i).getName());
        }
    }

    @Test
    void testScheduleAssignment() {
        // Monday = strength, Tuesday = cardio, Wednesday = rest
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(1, cardioWorkout);
        schedule.setScheduleForDay(2, restDay);
        
        // Verify assignments
        assertEquals("Strength Day", schedule.getScheduleForDay(0).getName());
        assertEquals("Cardio Day", schedule.getScheduleForDay(1).getName());
        assertEquals("Recovery + Stretching", schedule.getScheduleForDay(2).getName());
        
        // Other days should remain as default rest days
        for (int i = 3; i < 7; i++) {
            assertTrue(schedule.getScheduleForDay(i) instanceof RestDay);
            assertEquals("Rest Day", schedule.getScheduleForDay(i).getName());
        }
    }

    @Test
    void testMetricActivationOnScheduleAssignment() {
        // Initial state should have no exercise metrics
        Map<String, Double> initialChestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(0.0, initialChestMetrics.getOrDefault("totalSets", 0.0), TEST_PRECISION);
        
        // Schedule strength workout on Monday
        schedule.setScheduleForDay(0, strengthWorkout);
        
        // Verify metrics are registered with Monday context
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        Map<String, Double> activeChestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(4.0, activeChestMetrics.get("totalSets"), TEST_PRECISION);
        
        // Replace with rest day
        schedule.clearScheduleForDay(0);
        
        // Verify metrics are deactivated
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        Map<String, Double> clearedChestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(0.0, clearedChestMetrics.getOrDefault("totalSets", 0.0), TEST_PRECISION);
    }

    @Test
    void testMultipleDayMetricInteractions() {
        // Schedule same workout on Monday and Thursday
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(3, strengthWorkout);
        
        // Verify metrics are registered for both contexts
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Thursday"));
        
        Map<String, Double> chestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(8.0, chestMetrics.get("totalSets"), TEST_PRECISION); // 4 sets * 2 days
        
        // Clear Monday's workout
        schedule.clearScheduleForDay(0);
        
        // Verify only Thursday's metrics remain
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Thursday"));
        
        chestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(4.0, chestMetrics.get("totalSets"), TEST_PRECISION);
    }

    @Test
    void testWorkoutReplacement() {
        // Start with strength workout on Monday
        schedule.setScheduleForDay(0, strengthWorkout);
        
        // Verify initial metrics
        assertTrue(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        Map<String, Double> initialChestMetrics = chest.getAggregatedExerciseMetrics();
        assertEquals(4.0, initialChestMetrics.get("totalSets"), TEST_PRECISION);
        
        // Replace with cardio workout
        schedule.setScheduleForDay(0, cardioWorkout);
        
        // Verify strength metrics are removed and cardio metrics are added
        assertFalse(((ExerciseAssociator) dumbbell).containsExercise("Bench Press", "Monday"));
        assertTrue(((ExerciseAssociator) treadmill).containsExercise("Running", "Monday"));
        
        Map<String, Double> finalChestMetrics = chest.getAggregatedExerciseMetrics();
        Map<String, Double> finalQuadMetrics = quad.getAggregatedExerciseMetrics();
        assertEquals(0.0, finalChestMetrics.getOrDefault("totalSets", 0.0), TEST_PRECISION);
        assertEquals(1800.0, finalQuadMetrics.get("totalEnduranceDuration"), TEST_PRECISION);
    }

    @Test
    void testInvalidScheduleOperations() {
        assertThrows(IllegalArgumentException.class, () -> schedule.setScheduleForDay(-1, strengthWorkout));
        assertThrows(IllegalArgumentException.class, () -> schedule.setScheduleForDay(7, strengthWorkout));
        assertThrows(IllegalArgumentException.class, () -> schedule.setScheduleForDay(0, null));
        assertThrows(IllegalArgumentException.class, () -> schedule.clearScheduleForDay(-1));
        assertThrows(IllegalArgumentException.class, () -> schedule.clearScheduleForDay(7));
        assertThrows(IllegalArgumentException.class, () -> schedule.getScheduleForDay(-1));
        assertThrows(IllegalArgumentException.class, () -> schedule.getScheduleForDay(7));
    }

    @Test
    void testWeekSummaryFormat() {
        schedule.setScheduleForDay(0, strengthWorkout);
        schedule.setScheduleForDay(2, cardioWorkout);
        schedule.setScheduleForDay(5, restDay);
        
        String summary = schedule.getWeekSummary();
        
        assertTrue(summary.contains("Monday: Strength Day"));
        assertTrue(summary.contains("Wednesday: Cardio Day"));
        assertTrue(summary.contains("Saturday: Recovery + Stretching"));
        assertTrue(summary.contains("Tuesday: Rest Day"));
        assertTrue(summary.contains("Thursday: Rest Day"));
        assertTrue(summary.contains("Friday: Rest Day"));
        assertTrue(summary.contains("Sunday: Rest Day"));
    }
}