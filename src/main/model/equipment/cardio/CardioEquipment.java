package model.equipment.cardio;

import model.equipment.BaseEquipment;

/*
 * Represents the abstraction of exercise equipments that revolve around CARDIO.
 * These type of equipments have different behaviours within the program. 
 * In particular, exercise equipments which implement CardioEquipment are:
 * 1. Weight based.
 * 2. Are of 'Cardio Equipment' type.  
 */
public abstract class CardioEquipment extends BaseEquipment {

    // EFFECTS: Return 'Cardio' as the equipment type of this equipment.
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
