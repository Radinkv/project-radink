package model.equipment.strength;

/**
 * REPRESENTS: a type of cardio training equipment: Machine. A categorical label for 
 * exercises performed using a machine.
 * 
 * USED BY:
 *      1. Exercises classified as machine-based
 *      2. Workouts that include machine exercises
 * 
 * PURPOSE: This class ensures all machine exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable (Except ExerciseAssociator)
 */
public class Machine extends StrengthEquipment {

    // EFFECTS: Create an instance of this Machine exercise equipment.
    public Machine() {
        super();
        name = "Machine";
    }

    // EFFECTS: Return 'Machine' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
