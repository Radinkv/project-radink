package model.equipment.strength;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;


/**
 * REPRESENTS: the abstraction of exercise equipment used for strength training.
 * 
 * USED BY:
 *      1. Exercises that involve resistance or weightlifting
 *      2. Equipment categories classified as 'Strength Equipment'
 * 
 * PURPOSE: Define common behavior for all strength-based exercise equipment.
 *          These types of equipment are weight-based and are in the "Strength Equipment" category.
 */
public abstract class StrengthEquipment extends ExerciseAssociator implements Equipment {

    // EFFECTS: Return 'Strength' as the equipment type of this equipment.
    @Override
    public String getEquipmentType() {
        return ""; // stub
    }  

    // EFFECTS: Return true to specify that this equipment is not weight-based.
    @Override
    public boolean isWeightBased() {
        return false; // stub
    }
}
