package model.workout;

import java.util.List;

public class WorkoutLibrary {

    // EFFECTS: Initialize this WorkoutLibrary beginning with an initial list of workouts to store.  
    public WorkoutLibrary(List<Workout> workouts) {
        
    }

    // EFFECTS: Initialize this WorkoutLibrary without a list of initial workouts to store.  
    public WorkoutLibrary() {
        
    }

    // REQUIRES: workout is not null
    // MODIFIES: this
    // EFFECTS: Add workout to this workout library to store
    public void addWorkoutToLibrary(Workout workout) {

    }
    
    // REQUIRES: workoutName points to (is the name of) a workout in this workout library.
    // MODIFIES: this
    // EFFECTS: Remove the given workout, by name, from this workout library.
    public void removeWorkoutFromLibrary(String workoutName) {

    }

    public void getWorkoutByNameFromLibrary(String name) {

    }

    public void getWorkouts() {

    }
}
