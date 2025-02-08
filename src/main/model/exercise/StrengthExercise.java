package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.muscle.MuscleGroup;


/**
 * REPRESENTS: an strength-based exercise performed for a set duration
 * 
 * USED BY:
 *      1. Workouts that include strength exercises
 *      2. MuscleGroups that track strength-based exercise impacts
 *      3. Equipment that categorizes exercises based on usage
 * 
 * PURPOSE: Models an strength exercise where the primary variable is time spent performing it
 *          Tracks exercise name, duration, equipment used, and targeted muscles
 * 
 * MUTABILITY: Immutable
 */
public class StrengthExercise extends Exercise {

    // REQUIRES: 
    /* EFFECTS: Create an instance of a strength/hypertrophy exercise, initializing:
                1. This exercise's name
                2. Number of sets and reps
                3, 4. Time per rep (seconds) and rest time (minutes)
                5. The equipment used for this exercise
                6. Targeted muscle group(s) 
                IF ANY OF sets, reps, numSecondsPerRep < 0, they are set to 0, respectively */
    public StrengthExercise(String name, int sets, int reps, double numSecondsPerRep, 
                            double restTime, Equipment equipmentUsed, MuscleGroup musclesTargeted) {

    }
    
    // EFFECTS: Calculate and return this exercise's total duration, rounded to the nearest minute.
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0);
    }

    // EFFECTS: Return key-value pairs of information about this exercise: 
    //          1, 2. 'sets' and 'seps' (number of repetitions) 
    //          3. 'restTime' (duration in seconds)
    //          4. 'timePerRep' (duration in seconds)
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
