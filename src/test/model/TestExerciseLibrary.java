package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.equipment.*;
import model.equipment.strength.*;
import model.equipment.cardio.*;
import model.equipment.bodyweight.*;
import model.exercise.*;
import model.muscle.*;

public class TestExerciseLibrary {
    private ExerciseLibrary library;
    private Exercise strengthExercise;
    private Exercise enduranceExercise;
    private Exercise intervalExercise;
    private Equipment treadmill;
    private Equipment dumbbell;
    private Equipment bodyweight;

    @BeforeEach
    void runBefore() {
        library = new ExerciseLibrary();
        treadmill = new Treadmill();
        dumbbell = new Dumbbell();
        bodyweight = new BodyWeight();
        ArrayList<Muscle> muscles = new ArrayList<Muscle>();
        
        muscles.add(new Muscle("Abs"));
        muscles.add(new Muscle("Shoulders"));
        muscles.add(new Muscle("Triceps"));
        intervalExercise = new IntervalExercise("Ab Plank", 45, 45, 10, 
                                                bodyweight, new MuscleGroup("Abs & Arms", muscles));
        
        muscles = new ArrayList<Muscle>();  
        muscles.add(new Muscle("Biceps"));
        strengthExercise = new StrengthExercise("Dumbbell Curls", 4, 8,  2,
                                                 120, dumbbell, new MuscleGroup("Biceps", muscles));

        muscles = new ArrayList<Muscle>();
        muscles.add(new Muscle("Heart"));
        muscles.add(new Muscle("Quads"));
        enduranceExercise = new EnduranceExercise("Treadmill Run", 30.0, treadmill, new MuscleGroup("Cardio", muscles));
    }

    // Exercise objects are immutable (side effects do not modify 'this')
    @Test
    void testEmptyLibrary() {
        assertFalse(library.containsExercise("Any Exercise"));
        assertNull(library.getExercise("Any Exercise"));
        assertTrue(library.getAllExercises().isEmpty());
    }

    @Test
    void testAddExercise() {
        assertTrue(library.addExercise(strengthExercise));
        assertTrue(library.containsExercise("Dumbbell Curls"));
        assertEquals(strengthExercise, library.getExercise("Dumbbell Curls"));
    }

    @Test
    void testAddMultipleExerciseTypes() {
        assertTrue(library.addExercise(strengthExercise));
        assertTrue(library.addExercise(enduranceExercise));
        assertTrue(library.addExercise(intervalExercise));

        // Verify complete state
        assertEquals(3, library.getAllExercises().size());
        assertTrue(library.containsExercise("Dumbbell Curls"));
        assertTrue(library.containsExercise("Treadmill Run"));
        assertTrue(library.containsExercise("Ab Plank"));
        assertEquals(strengthExercise, library.getExercise("Dumbbell Curls"));
        assertEquals(enduranceExercise, library.getExercise("Treadmill Run"));
        assertEquals(intervalExercise, library.getExercise("Ab Plank"));
    }

    @Test
    void testAddDuplicateExercise() {
        assertTrue(library.addExercise(strengthExercise));
        assertFalse(library.addExercise(strengthExercise));
        assertEquals(1, library.getAllExercises().size());
        assertEquals(strengthExercise, library.getExercise("Dumbbell Curls"));
    }

    @Test
    void testAddNullExercise() {
        assertFalse(library.addExercise(null));
        assertTrue(library.getAllExercises().isEmpty());
    }

    @Test
    void testRemoveNullExercise() {
        assertFalse(library.removeExercise(null));
    }

    @Test
    void testRemoveExercise() {
        // Initial state
        assertTrue(library.addExercise(strengthExercise));
        assertTrue(library.addExercise(enduranceExercise));
        assertEquals(2, library.getAllExercises().size());
        
        // Verify initial correct storage
        Exercise retrievedStrength = library.getExercise("Dumbbell Curls");
        Exercise retrievedEndurance = library.getExercise("Treadmill Run");
        assertEquals(strengthExercise, retrievedStrength);
        assertEquals(enduranceExercise, retrievedEndurance);
        assertEquals("Dumbbell Curls", retrievedStrength.getName());
        assertEquals("Treadmill Run", retrievedEndurance.getName());
        
        // Remove one exercise
        assertTrue(library.removeExercise("Dumbbell Curls"));
        
        // Verify complete state after removal
        assertFalse(library.containsExercise("Dumbbell Curls"));
        assertNull(library.getExercise("Dumbbell Curls"));
        assertTrue(library.containsExercise("Treadmill Run"));
        assertEquals(1, library.getAllExercises().size());
        
        // Verify remaining exercise is still intact
        Map<String, Exercise> remainingExercises = library.getAllExercises();
        assertTrue(remainingExercises.containsKey("Treadmill Run"));
        assertEquals(enduranceExercise, remainingExercises.get("Treadmill Run"));
        assertEquals("Treadmill Run", remainingExercises.get("Treadmill Run").getName());
    }

    @Test
    void testCompleteExerciseAddRemoveCycle() {
        // Verify initial empty state
        assertTrue(library.getAllExercises().isEmpty());
        assertFalse(library.containsExercise("Ab Plank"));
        assertNull(library.getExercise("Ab Plank"));
        
        // Add exercise and verify state
        assertTrue(library.addExercise(intervalExercise));
        assertTrue(library.containsExercise("Ab Plank"));
        assertEquals(1, library.getAllExercises().size());
        
        // Verify retrieved exercise properties
        Exercise retrieved = library.getExercise("Ab Plank");
        assertEquals(intervalExercise, retrieved);
        assertEquals("Ab Plank", retrieved.getName());
        assertEquals("Interval", retrieved.exerciseType());
        assertEquals(bodyweight, retrieved.getRequiredEquipment());
        assertEquals("Body Weight", retrieved.getRequiredEquipment().getEquipmentName());
        
        // Verify muscle group
        MuscleGroup muscles = retrieved.getMusclesTargeted();
        assertEquals("Abs & Arms", muscles.getName());
        
        // Remove exercise and verify complete empty state
        assertTrue(library.removeExercise("Ab Plank"));
        assertFalse(library.containsExercise("Ab Plank"));
        assertNull(library.getExercise("Ab Plank"));
        assertTrue(library.getAllExercises().isEmpty());
        assertEquals(0, library.getAllExercises().size());
    }
}
