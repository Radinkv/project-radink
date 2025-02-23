package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;
import model.equipment.strength.Dumbbell;
import model.exercise.EnduranceExercise;
import model.exercise.Exercise;
import model.exercise.StrengthExercise;
import model.muscle.Muscle;
import model.muscle.MuscleGroup;
import model.workout.RestDay;
import model.workout.Workout;
import model.workout.WorkoutLibrary;
import model.workout.WorkoutPlan;

/** This class tests the mutability of WorkoutLibrary in handling WorkoutPlan objects.
 * 
 * NOTE: WorkoutPlan objects are unmodifiable, EXCEPT their list of exercises. WorkoutLibrary
 * should NOT modify that list under any circumstances.
 */
public class TestWorkoutLibrary {
    private WorkoutLibrary library;
    private WorkoutPlan strengthWorkout;
    private WorkoutPlan cardioWorkout;
    private WorkoutPlan restDay;
    private ArrayList<Exercise> strengthExercises;
    private ArrayList<Exercise> cardioExercises;
    private Equipment dumbbell;
    private Equipment treadmill;
    private MuscleGroup biceps;
    private MuscleGroup cardio;

    @BeforeEach
    void runBefore() {
        library = new WorkoutLibrary();

        dumbbell = new Dumbbell();
        treadmill = new Treadmill();
        
        ArrayList<Muscle> muscles = new ArrayList<Muscle>();
        muscles.add(new Muscle("Biceps"));
        biceps = new MuscleGroup("Biceps", muscles);
        
        muscles = new ArrayList<Muscle>();
        muscles.add(new Muscle("Heart"));
        muscles.add(new Muscle("Quads"));
        cardio = new MuscleGroup("Cardio", muscles);

        strengthExercises = new ArrayList<Exercise>();
        strengthExercises.add(new StrengthExercise("Dumbbell Curls", 4, 8, 2, 120, dumbbell, biceps));
        strengthExercises.add(new StrengthExercise("Hammer Curls", 3, 10, 2, 90, dumbbell, biceps));
        
        cardioExercises = new ArrayList<Exercise>();
        cardioExercises.add(new EnduranceExercise("Treadmill Run", 30.0, treadmill, cardio));

        strengthWorkout = new Workout("Arm Day", strengthExercises);
        cardioWorkout = new Workout("Cardio Day", cardioExercises);
        restDay = new RestDay("Recovery");
    }

    @Test
    void testEmptyLibrary() {
        assertTrue(library.getAllWorkouts().isEmpty());
        assertNull(library.getWorkout("Any Workout"));
        assertThrows(IllegalArgumentException.class, () -> library.removeWorkout("Any Workout"));
    }

    @Test
    void testAddOneWorkoutAndGetNullWorkout() {
        library.addWorkout(strengthWorkout);
        
        // complete strengthWorkout state within the library
        assertEquals(strengthWorkout, library.getWorkout("Arm Day"));
        assertEquals(1, library.getAllWorkouts().size());
        assertTrue(library.getAllWorkouts().contains(strengthWorkout));

        assertThrows(IllegalArgumentException.class, () -> library.getWorkout(null));
    }

    @Test
    void testAddOneRestDay() {
        library.addWorkout(restDay);
        
        // complete restDay state within the library
        assertEquals(restDay, library.getWorkout("Recovery"));
        assertEquals(1, library.getAllWorkouts().size());
        assertTrue(library.getAllWorkouts().contains(restDay));
    }

    @Test
    void testAddMultipleWorkoutPlans() {
        library.addWorkout(strengthWorkout);
        library.addWorkout(cardioWorkout);
        library.addWorkout(restDay);
        
        assertEquals(3, library.getAllWorkouts().size());
        assertEquals(strengthWorkout, library.getWorkout("Arm Day"));
        assertEquals(cardioWorkout, library.getWorkout("Cardio Day"));
        assertEquals(restDay, library.getWorkout("Recovery"));
        
        List<WorkoutPlan> allWorkouts = library.getAllWorkouts();
        assertTrue(allWorkouts.contains(strengthWorkout));
        assertTrue(allWorkouts.contains(cardioWorkout));
        assertTrue(allWorkouts.contains(restDay));
    }

    @Test
    void testAddDuplicateWorkout() {
        library.addWorkout(strengthWorkout);
        
        assertThrows(IllegalArgumentException.class, () -> library.addWorkout(strengthWorkout));
        assertEquals(1, library.getAllWorkouts().size());
        assertEquals(strengthWorkout, library.getWorkout("Arm Day"));

        List<WorkoutPlan> allWorkouts = library.getAllWorkouts();
        assertTrue(allWorkouts.contains(strengthWorkout));
    }

    @Test
    void testAddNullWorkout() {
        assertThrows(IllegalArgumentException.class, () -> library.addWorkout(null));
        assertTrue(library.getAllWorkouts().isEmpty());
    }

    @Test
    void testRemoveWorkout() {
        // Add workouts and verify initial state
        library.addWorkout(strengthWorkout);
        library.addWorkout(cardioWorkout);
        assertEquals(2, library.getAllWorkouts().size());
        
        // Remove one workout
        library.removeWorkout("Arm Day");
        assertEquals(1, library.getAllWorkouts().size());
        
        // remaining state
        assertEquals(cardioWorkout, library.getWorkout("Cardio Day"));
        assertNull(library.getWorkout("Arm Day"));
        
        List<WorkoutPlan> remaining = library.getAllWorkouts();
        assertFalse(remaining.contains(strengthWorkout));
        assertTrue(remaining.contains(cardioWorkout));
    }

    @Test
    void testRemoveNullWorkoutName() {
        assertThrows(IllegalArgumentException.class, () -> library.removeWorkout(null));
    }

    @Test
    void testRemoveNonexistentWorkout() {
        assertThrows(IllegalArgumentException.class, () -> library.removeWorkout("Nonexistent Workout"));
    }

    @Test
    void testCompleteWorkoutAddRemoveCycle() {
        // initial empty state
        assertTrue(library.getAllWorkouts().isEmpty());
        assertNull(library.getWorkout("Arm Day"));
        
        // Add workout and verify complete state
        library.addWorkout(strengthWorkout);
        assertEquals(1, library.getAllWorkouts().size());
        
        // retrieved workout properties
        WorkoutPlan retrieved = library.getWorkout("Arm Day");
        assertEquals(strengthWorkout, retrieved);
        assertEquals("Arm Day", retrieved.getName());
        assertEquals(strengthExercises, retrieved.getExercises());
        
        // Remove workout and verify empty state
        library.removeWorkout("Arm Day");
        assertTrue(library.getAllWorkouts().isEmpty());
        assertNull(library.getWorkout("Arm Day"));
    }

    // WorkoutLibrary should NOT modify Workout objects
    @Test
    void testNoSideEffectsOnWorkoutExerciseList() {
        library.addWorkout(strengthWorkout);
        List<Exercise> originalExercises = new ArrayList<Exercise>(strengthExercises);
        
        // Get workout and modify its exercise list
        WorkoutPlan stored = library.getWorkout("Arm Day");
        stored.getExercises().clear(); // Attempt to modify the list
        
        // Verify original workout's exercises weren't affected
        assertEquals(originalExercises, strengthWorkout.getExercises()); // Should pass due to defensive copying
    }

    @Test
    void testDefensiveCopyingOnRetrieval() {
        library.addWorkout(strengthWorkout);
        
        List<Exercise> firstList = library.getWorkout("Arm Day").getExercises();
        List<Exercise> secondList = library.getWorkout("Arm Day").getExercises();
        
        // Lists should be equal but not the same instance for defensive copying
        assertEquals(firstList, secondList);
        assertNotSame(firstList, secondList); 
    }

    @Test
    void testGetAllWorkoutsDefensiveCopy() {
        library.addWorkout(strengthWorkout);
        library.addWorkout(cardioWorkout);
        
        List<WorkoutPlan> workouts = library.getAllWorkouts();
        workouts.clear(); // Attempt to modify the returned list
        
        assertEquals(2, library.getAllWorkouts().size()); // Should pass due to defensive copying
    }
}
