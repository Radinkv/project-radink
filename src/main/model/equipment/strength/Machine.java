package model.equipment.strength;

/**
 * Represents a type of cardio training equipment: the Machine.
 * 
 * This class is mutable. It serves as a categorical label for exercises that the user specifies are machine-based.
 * It stores a list of exercises that the user specifies are performed with a machine when building their exercise and
 * workout library.
 * 
 * If this class/object is instantiated (the user specifies one or more of their workouts with this equipment), only one
 * instance of this object is constructed to remain in the program. ALL associated exercises will refer to this same
 * equipment object.
 */
public class Machine extends StrengthEquipment {

    // EFFECTS: Create an instance of this Machine exercise equipment.
    public Machine() {

    }

    // EFFECTS: Return 'Machine' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return ""; // stub
    }
}
