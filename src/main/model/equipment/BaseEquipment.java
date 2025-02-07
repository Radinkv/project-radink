package model.equipment;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import model.exercise.Exercise;

/*
 * Represents the first layer of abstraction of an Equipment object (representing a label for a given type 
 * of exercise equipment). All instantiated equipment objects share the same implementation of:
 * 
 * 1. Associating the exercise(s) the user specifies that use the specified equipment with the given equipment, 
 *    no matter the type;
 * 
 * 2. Retrieve this/these exercise/s;
 * 
 * 3. Removing associate exercise(s) should the use decide to pivot from an exercise, or using this equipment 
 *    in general.
 * 
 * 4. Calculating the total DURATION of time spent using this equipment, whether the exercises involved 
 *    revolves around INTERVALS and REST PERIODS (interval training), SETS and REPS (strength training), 
 *    or go-until-exhaustion (endurance training). Thus, the duration-fetching implementation of each 
 *    exercise equipment object instance is SHARED and equally arbitrary: the same.
*/
public abstract class BaseEquipment implements Equipment {

    // MODIFIES: this
    // EFFECTS: Adds the given exercise to this equipment’s list of associated exercises, if not already present.
    @Override
    public void associateExercise(Exercise exercise) {
        return; //stub
    }

    // MODIFIES: this
    // EFFECTS: Removes the given exercise from this equipment’s list of associated exercises; nothing if not present.
    @Override
    public void dissociateExercise(Exercise exercise) {
        return; //stub
    }

    // EFFECTS: Return total duration (in minutes) of all associated exercises that use this equipment
    @Override
    public Duration getAssociatedExercisesTotalDuration() {
        return Duration.ofMinutes(0); // stub
    }
    
    // EFFECTS: Return/provide the list of all exercises associated with this equipment.
    @Override
    public List<Exercise> getAssociatedExercises() {
        return new ArrayList<>(); //stub
    }
}
