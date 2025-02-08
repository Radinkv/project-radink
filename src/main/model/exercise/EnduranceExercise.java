package model.exercise;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import model.equipment.Equipment;
import model.equipment.cardio.Treadmill;
import model.muscle.MuscleGroup;

public class EnduranceExercise extends Exercise {

    // REQUIRES: totalDuration > 0
    /* EFFECTS: Create an instance of this endurance exercise, initializing:
             1. The name of this exercise
             2. The total duration of this endurance exercise, in minutes
             3. The equipment used in performing this endurance exercise
             4. The muscle group (muscle(s)) targeted by this endurance-based exercise */
    public EnduranceExercise(String name, double totalDuration, 
                            Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        // stub
    }

    // EFFECTS: Return this exercise's name
    @Override
    public String getName() {
        return ""; // stub
    }

    // EFFECTS: Return this endurance exercise's total duration, in minutes
    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(0); // stub
    }
    
    // EFFECTS: Return 'Interval' as this exercise's type
    @Override
    public String exerciseType() {
        return ""; // stub
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

    /* EFFECTS: Return three key-value pairs for this endurance exercise: 
             1, 2. 'Time On' and 'Time Off' (interval durations in seconds) 
             3. 'Repetitions' (number of repetitions) */
    @Override
    public Map<String, Double> getInfo() {
        return new HashMap<String, Double>(); // stub
    }
}