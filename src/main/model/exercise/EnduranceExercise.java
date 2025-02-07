package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;

public class EnduranceExercise implements Exercise {

    // REQUIRES: totalDuration > 0
    // EFFECTS: Create an instance of this endurance exercise, specifying:
    //          1. The name of this exercise
    //          2. The total duration of the endurance exercise, in minutes
    //          3. The equipment used in performing this endurance exercise
    public EnduranceExercise(String name, double totalDuration, Equipment equipmentUsed) {
        // stub
    }

    // EFFECTS: Return this exercise's name
    @Override
    public String getName() {
        return ""; // stub
    }

    // EFFECTS: Return this exercise's total duration, in minutes
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0); // stub
    }
    
    // EFFECTS: Return 'Interval' as this exercise's type
    @Override
    public String exerciseType() {
        return ""; // stub
    }

    // EFFECTS: Return the equipment type instantiation of this exercise
    @Override
    public Equipment getRequiredEquipment() {
        return new Treadmill(); // stub
    }

    // EFFECTS: Return three key-value pairs for this exercise: 
    //          1. 'Time On' and 'Time Off' (interval durations in seconds) 
    //          2. 'Repetitions' (number of repetitions)
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>(); // stub
    }
}