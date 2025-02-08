package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;
import model.muscle.MuscleGroup;

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

    /* EFFECTS: Return three key-value pairs for this exercise: 
             1. 'Time On' and 'Time Off' (interval durations in seconds) 
             2. 'Repetitions' (number of repetitions) */
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>();
    }
}
