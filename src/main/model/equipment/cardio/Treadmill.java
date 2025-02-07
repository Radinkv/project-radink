package model.equipment.cardio;

/**
 * Represents a type of cardio training equipment: the Treadmill.
 * 
 * This class is mutable. It serves as a categorical label for exercises that the user specifies are treadmill-based.
 * It stores a list of exercises that the user specifies are performed with a treadmill when building their exercise and
 * workout library.
 * 
 * If this class/object is instantiated (the user specifies one or more of their workouts with this equipment), only one
 * instance of this object is constructed to remain in the program. ALL associated exercises will refer to this same
 * equipment object.
 */
public class Treadmill extends CardioEquipment {
    
    // EFFECTS: Create an instance of this treadmill exercise equipment.
    public Treadmill() {

    }

    // Return 'Treadmill' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return ""; // stub
    }
}
