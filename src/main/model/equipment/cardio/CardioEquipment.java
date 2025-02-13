package model.equipment.cardio;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;

/**
 * REPRESENTS: the abstraction of exercise equipment used for cardio training.
 * 
 * USED BY:
 *      1. Exercises that involve sustained cardiovascular activity
 *      2. Equipment categories classified as 'Cardio Equipment'
 * 
 * PURPOSE: Defines common behavior for all cardio-based exercise equipment.
 *          These types of equipment are not weight-based and belong to the "Cardio Equipment" category.
 */
public abstract class CardioEquipment extends ExerciseAssociator implements Equipment {
    protected String equipmentType;
    protected boolean isWeightBased;
    protected String name;

    protected CardioEquipment() {
        equipmentType = "Cardio Equipment";
        isWeightBased = false;
    }

    // EFFECTS: Return 'Cardio' as the equipment type of this equipment.
    @Override
    public String getEquipmentType() {
        return equipmentType;
    }  

    // EFFECTS: Return false to specify that this equipment is not weight-based.
    @Override
    public boolean isWeightBased() {
        return isWeightBased;
    }
}
