package model.exercise;

import java.util.Map;
import java.time.Duration;

import model.equipment.Equipment;
import model.muscle.MuscleGroup;

/**
 * Represents the abstraction of a general exercise. This interface is implemented by different classes; 
 * types of exercises.
 * 
 * These different implementations represent real instances such as differentiating between strength training
 * exercises (sets and reps), resistance training exercises (time exercising and time resting) for a limited number of 
 * sets, or perhaps cardio exercises (20 minute continuous run).
 * 
 * The intent of this separation is to organise different ways of exercising for the program, allowing for the most/best
 * user-customization and analytical insight.
 * 
 * See Exercise subclasses for method specifications & clauses.
 */
public abstract class Exercise {

    // MODIFIES: this
    // EFFECTS: Dissociate the previous Muscle objects in this strength exercise's muscle group from this exercise.
    //          Then, change the previous muscle group of this strength exercise to this muscle group, associate with 
    //          the new muscles with this exercise, and then return true. However, if the muscles in muscleGroup are 
    //          the same as this current exercise's muscle group, return false and make no changes.
    public boolean modifyMuscleGroup(MuscleGroup muscleGroup) {
        return false;
    }

    public String getName() {
        return "";
    }

    abstract Duration getDuration();

    abstract String exerciseType();

    abstract Equipment getRequiredEquipment();

    public MuscleGroup getMusclesTargeted() {
        return new MuscleGroup();
    }

    abstract Map<String, Double> getInfo(); 
}
