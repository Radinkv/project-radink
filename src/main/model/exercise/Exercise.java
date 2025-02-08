package model.exercise;

import java.util.Map;
import java.time.Duration;

import model.equipment.Equipment;
import model.equipment.bodyweight.BodyWeight;
import model.muscle.MuscleGroup;

/**
 * REPRESENTS: an abstract base class for different types of exercises
 * 
 * USED BY:
 *      1. Strength exercises that track sets, reps, and time per rep
 *      2. Endurance exercises that track total duration
 *      3. Interval-based exercises that track time spent exercising and resting
 * 
 * PURPOSE: Defines a standard interface for different styles of exercises
 *          Allow structured user exercise customization and analytical tracking
 *          Enforce separation of exercise types based on training style
 * 
 * See Exercise subclasses for detailed method specifications
 */
public abstract class Exercise {

    // EFFECTS: Return name of this endurance exercise
    public String getName() {
        return ""; // stub
    }
    
    abstract Duration getDuration();

    // EFFECTS: Return this exercise's training style
    public String exerciseType() {
        return ""; // stub
    }

    // EFFECTS: Return equipment used for this endurance exercise
    public Equipment getRequiredEquipment() {
        return new BodyWeight(); // stub
    }

    // EFFECTS: Return muscle group targeted by this exercise
    public MuscleGroup getMusclesTargeted() {
        return new MuscleGroup(); // stub
    }

    abstract Map<String, Double> getInfo(); 
}
