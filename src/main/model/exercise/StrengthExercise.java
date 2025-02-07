package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;

public class StrengthExercise implements Exercise {

    // REQUIRES: sets, reps, numSecondsPerRep > 0
    // EFFECTS: Create an instance of this strength exercise, specifying:
    //          1. The name of this exercise
    //          2. The number of: 
    //              a. Sets this exercise includes
    //              b. Reps this exercise includes
    //              c. Seconds per rep
    //              d. Minutes of rest-time
    //          3. The equipment used
    public StrengthExercise(String name, int sets, int reps, 
                            double numSecondsPerRep, double restTime, Equipment equipmentUsed) {

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

    // EFFECTS: Return the equipment type instantiation of this exercise
    @Override
    public Equipment getRequiredEquipment() {
        return new Treadmill(); // stub
    }

    // EFFECTS: Return four key-value pairs for information about this exercise: 
    //          1. 'Sets' and 'Reps' (number of repetitions) 
    //          2. 'Rest time' (duration in seconds)
    //          3. 'Time per rep' (duration in seconds) 
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
