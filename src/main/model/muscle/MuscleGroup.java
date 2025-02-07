package model.muscle;

import java.util.HashSet;
import java.util.Set;

import model.exercise.Exercise;

/**
 * This class represents a group of muscles, which an exercise effectively targets.
 * 
 * Each exercise has a unique MuscleGroup object, representing each set of Muscles targeted by the given exercise.
 * However, it is important to note that these various instances of MuscleGroup will point to the same initiations
 * of Muscle objects (created right at runtime) so that each Muscle object retains cumulative analytical data across
 * multiple exercises, which allows for both accurate tracking and workout analysis.
 */
public class MuscleGroup {
    
    // REQUIRES: muscleGroup and pairedExercise are not null
    // EFFECTS: Create an instance of this MuscleGroup object and associate this muscle group with the given exercise.
    //          Also associates each Muscle in muscleGroup to the given pairedExercise, respectively.
    public MuscleGroup(Set<Muscle> muscleGroup, Exercise pairedExercise) {
        // stub
    }

    // REQUIRES: muscleGroup is not null
    // EFFECTS: Create an instance of this MuscleGroup object WITHOUT associating it with an exercise.
    public MuscleGroup(Set<Muscle> muscleGroup) {
        // stub
    }

    public MuscleGroup() {
        // stub
    }

    // REQUIRES: muscle is not null
    // MODIFIES: this
    // EFFECTS: Add the given muscle to this muscle group, if not already present. Return true if the given muscle
    //          is not already within the muscle group and thus successfully added, false otherwise
    public boolean addMuscle(Muscle muscle) {
        return false; // stub
    }

    // REQUIRES: muscleName points to (is the name of) a Muscle object's name.
    // MODIFIES: this
    // EFFECTS: Remove the given muscle from this muscle group
    public void removeMuscle(String muscleName) {
        return; // stub
    }

    // REQUIRES: exercise is not null
    // MODIFIES: this
    // EFFECTS: If this muscle group was previously associated with an exercise, dissociate that exercise from
    //          each muscle in the group. Then, associate this muscle group with the new given exercise, ensuring
    //          that all muscles in the group also associate with it. Otherwise, associate each muscle in the group
    //          with this exercise, and associate this muscle group with the given exercise.
    public void associateMuscleGroupWithExercise(Exercise exercise) {
        // stub
    }
    
    // MODIFIES: this
    // EFFECTS: If this muscle group is currently associated with an exercise, dissociate the exercise from this 
    //          muscle group and then dissociate each muscle in this group from that exercise. Otherwise no changes
    public void dissociateMuscleGroupFromExercise() {
        // stub
    }

    public Set<Muscle> getMusclesInGroup() {
        return new HashSet<Muscle>(); // stub
    }
}
