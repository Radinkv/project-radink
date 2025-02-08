package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.muscle.MuscleGroup;

/**
 * REPRESENTS: an interval-based exercise performed for a set duration
 * 
 * USED BY:
 *      1. Workouts that include interval exercises
 *      2. MuscleGroups that track interval-based exercise impacts
 *      3. Equipment that categorizes exercises based on usage
 * 
 * PURPOSE: Models an interval exercise where the primary variable is time spent performing it
 *          Tracks exercise name, duration, equipment used, and targeted muscles
 * 
 * MUTABILITY: Immutable 
 */
public class IntervalExercise extends Exercise {

    // REQUIRES: timeOn, timeOff, numRepetitions > 0
    /* EFFECTS: Creates an instance of a interval-based exercise, initializing this exercise's:
             1. Name
             2. Active time (seconds) and rest time (seconds) per each repetition
             3. Number of repetitions
             4. Equipment used for this exercise
             5. Targeted muscle group(s) */
    public IntervalExercise(String name, double timeOn, double timeOff, 
                            int numRepititions, Equipment equipmentUsed, MuscleGroup musclesTargeted) {

    }

    // EFFECTS: Return this interval exercise's total duration, in minutes
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0);
    }
    
    // EFFECTS: Return key-value pairs of information about this exercise: 
    //        1, 2. 'timeOn' and 'timeOff' (interval durations in seconds) 
    //        3. 'repititions' (number of timeOn -> timeOff repetitions)
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
