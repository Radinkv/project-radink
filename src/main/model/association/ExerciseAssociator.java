package model.association;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.exercise.Exercise;

/**
 * This class represents a base class for entities that can associate exercises.
 * 
 * This applies to:
 *      1. Equipment (i.e. Dumbbell, Treadmill, Barbell)
 *      2. Muscle Groups (instantiations of the Muscle class/object)
 * 
 * The intent behind doing so is to be able to draw metrics on time and volume splits based on both equipment
 * type and by muscle group. Furthermore, this abstraction ensures that both Equipment and Muscle share identical
 * exercise association behavior. By doing so, it becomes simpler to draw analytics from various, complex workout 
 * splits.
 */
public abstract class ExerciseAssociator {

    // MODIFIES: this
    // EFFECTS: Adds the given exercise to this entities' list of associated exercises, if not already present.
    public void associateExercise(Exercise exercise) {
        return; //stub
    }

    // MODIFIES: this
    // EFFECTS: Removes the given exercise from this entities' list of associated exercises by exercise name; 
    // otherwise no changes if not present.
    public void dissociateExercise(String exerciseName) {
        return; //stub
    }

    /* EFFECTS: Returns key-value pairs of exercise metrics accumulated across all associated exercises.
     * Currently, the following keys may be present:
     *      1. 'setsAssociated': total number of sets across all strength-based exercises
     *      2. 'repsAssociated': total number of reps across all strength-based exercises
     *      3. 'intervalExerciseTimeSpent': total time spent in interval-based exercises
     *      4. 'totalDuration': total duration across all exercises
     * Keys are only included if at least one associated exercise contributes to that metric */
    public Map<String, Double> getAggregatedExerciseMetrics() {
        return new HashMap<String, Double>();
    }
    
    // EFFECTS: Return/provide the list of all exercises associated with this entity.
    public List<Exercise> getAssociatedExercises() {
        return new ArrayList<>(); //stub
    }
}
