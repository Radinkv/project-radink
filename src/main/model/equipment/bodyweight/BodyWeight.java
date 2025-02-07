package model.equipment.bodyweight;

/**
 * Represents a type of cardio training equipment: Bodyweight.
 * 
 * This class is mutable. It serves as a categorical label for exercises that the user specifies are bodyweight-based.
 * It stores a list of exercises that the user specifies are performed with bodyweight when building their exercise and
 * workout library.
 * 
 * If this class/object is instantiated (the user specifies one or more of their workouts with this equipment), only one
 * instance of this object is constructed to remain in the program. ALL associated exercises will refer to this same
 * equipment object.
 */
public class BodyWeight extends BodyWeightEquipment {
    
    // EFFECTS: Create an instance of this bodyweight 'equipment'.
    public BodyWeight() {

    }

    // Return 'Bodyweight' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return ""; // stub
    }
}
