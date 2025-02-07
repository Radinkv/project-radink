package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;

public class IntervalExercise implements Exercise {

    // REQUIRES: timeOn, timeOff, numRepetitions > 0
    // EFFECTS: Create an instance of this interval exercise, specifying:
    public IntervalExercise(String name, double timeOn, double timeOff, 
                            int numRepititions, Equipment equipmentUsed) {

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

    // EFFECTS: Return three key-value pairs for this exercise: 
    //          1. 'Time On' and 'Time Off' (interval durations in seconds) 
    //          2. 'Repetitions' (number of repetitions)
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
