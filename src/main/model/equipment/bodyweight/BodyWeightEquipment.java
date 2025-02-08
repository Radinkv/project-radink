package model.equipment.bodyweight;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;

/**
 * REPRESENTS: the abstraction of exercise equipment based on bodyweight.
 * 
 * USED BY:
 *      1. Exercises that do not require external weights
 *      2. Equipment categories classified as 'Bodyweight Equipment'
 * 
 * PURPOSE: Defines common behavior for all bodyweight-based exercise equipment.
 *          These types of equipment are not weight-based and belong to the "Bodyweight Equipment" category.
 */
public abstract class BodyWeightEquipment extends ExerciseAssociator implements Equipment {

    // EFFECTS: Return 'Bodyweight' as the equipment type of this equipment.
    @Override
    public String getEquipmentType() {
        return ""; // stub
    }  

    // EFFECTS: Return false to specify that this equipment is not weight-based.
    @Override
    public boolean isWeightBased() {
        return false; // stub
    }
}
