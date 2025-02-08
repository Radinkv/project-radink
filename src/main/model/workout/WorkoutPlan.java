package model.workout;

import java.util.List;
import java.util.Map;

import model.exercise.Exercise;

public abstract class WorkoutPlan {

    // EFFECTS: Return name of this workout plan
    public String getName() {
        return ""; // stub
    }

    // EFFECTS: Return total duration of this workout plan in seconds
    public abstract double getDuration();

    // EFFECTS: Return list of exercises this workout plan includes
    public abstract List<Exercise> getExercises();

    // EFFECTS: Return key-value pairs summarizing this workout plan
    public abstract Map<String, Double> getWorkoutSummary();

}
