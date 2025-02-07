package model.workout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exercise.Exercise;

public class Workout {
    
    // EFFECTS: Instantiate this workout with a list of exercises, given in their order of completion.
    public Workout(List<Exercise> exercises) {

    }
    
    // REQUIRES: 0 <= insertionIndex < workoutExerciseList.size()
    // MODIFIES: this
    // EFFECTS: Add the given exercise to this workout's exercise list at the given insertionIndex, allowing duplicates
    public void addExercise(Exercise exercise, int insertionIndex) {
        return; // stub
    }

    // REQUIRES: 0 <= index < workoutExerciseList.size()
    // MODIFIES: this
    // EFFECTS: Remove the exercise at the given index from this workout's exercise list.
    public void removeExercise(int index) {
        return; // stub
    }

    public Map<String, Double> getWorkoutSummary() {
        return new HashMap<String,Double>();
    }

    public String getWorkoutName() {
        return ""; // stub
    }
}
