package model.equipment.strength;

import model.equipment.BaseEquipment;

/*
 * Represents the abstraction of exercise equipments that revolve around STRENGTH.
 * These type of equipments have different behaviours within the program. 
 * In particular, exercise equipments which implement StrengthEquipment are:
 * 1. Weight based.
 * 2. Are of 'Strength Equipment' type.  
 */
public abstract class StrengthEquipment extends BaseEquipment {

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
