package model.equipment.bodyweight;

import model.equipment.BaseEquipment;

/*
 * Represents the abstraction of exercise equipments that revolve around BODYWEIGHT.
 * These type of equipments have different behaviours within the program. 
 * In particular, exercise equipments which implement BodyWeightEquipment are:
 * 1. Not weight based.
 * 2. Are of 'Bodyweight Equipment' type.  
 */
public abstract class BodyWeightEquipment extends BaseEquipment {

    // EFFECTS: Return 'Bodyweightzi' as the equipment type of this equipment.
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
