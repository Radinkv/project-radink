package model.equipment;

/**
 * REPRESENTS: the basic abstraction of a piece of exercise equipment.
 * 
 * USED BY:
 *      1. Exercises that require specific equipment
 *      2. Equipment categories such as Strength, Cardio, and Bodyweight Equipment
 * 
 * PURPOSE: Define the fundamental properties of exercise equipment. Equipment represents a 
 *          physical mode of exercising, rather than an individual item (i.e. a single weight plate).
 *          It allows exercises to be categorized and analyzed based on their equipment usage.
 */
public interface Equipment {
    String getEquipmentType();

    String getEquipmentName();
    
    boolean isWeightBased();
}
