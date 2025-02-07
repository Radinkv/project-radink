package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;
import model.muscle.MuscleGroup;

public class StrengthExercise implements Exercise {

    // REQUIRES: sets, reps, numSecondsPerRep > 0
    /* EFFECTS: Create an instance of a strength/hypertrophy exercise, initializing:
             1. This exercise's name
             2. Number of sets and reps
             3. Time per rep (seconds) and rest time (minutes)
             4. The equipment used for this exercise
             5. Targeted muscle group(s) */
    public StrengthExercise(String name, int sets, int reps, double numSecondsPerRep, 
                            double restTime, Equipment equipmentUsed, MuscleGroup musclesTargeted) {

    }

    // EFFECTS: Return this exercise's name
    @Override
    public String getName() {
        return "";
    }

    // EFFECTS: Return this exercise's total duration, in minutes
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0);
    }
    
    // EFFECTS: Return 'Interval' as this exercise's type
    @Override
    public String exerciseType() {
        return "";
    }

    // EFFECTS: Return the equipment this endurance exercise targets
    @Override
    public Equipment getRequiredEquipment() {
        return new Treadmill(); // stub
    }

    // EFFECTS: Return the group of muscles this endurance exercise targets
    @Override
    public MuscleGroup getMusclesTargeted() {
        return new MuscleGroup(); // stub
    }

    /* EFFECTS: Return four key-value pairs for information about this exercise: 
             1. 'Sets' and 'Reps' (number of repetitions) 
             2. 'Rest time' (duration in seconds)
             3. 'Time per rep' (duration in seconds) */
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
