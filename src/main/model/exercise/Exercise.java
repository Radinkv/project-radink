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
 * user-customization.
 * 
 * See Exercise subclasses for method specifications & clauses.
 */
public interface Exercise {
    String getName();

    Duration getDuration();

    String exerciseType();

    Equipment getRequiredEquipment();

    MuscleGroup getMusclesTargeted();

    Map<String, Double> getInfo(); 
}
