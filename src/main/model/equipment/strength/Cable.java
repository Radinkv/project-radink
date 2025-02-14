package model.equipment.strength;

/**
 * REPRESENTS: a type of cardio training equipment: Cable. A categorical label for 
 * exercises performed using cables.
 * 
 * USED BY:
 *      1. Exercises classified as cable-based
 *      2. Workouts that include cable exercises
 * 
 * PURPOSE: This class ensures all cable exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable (Except ExerciseAssociator)
 */
public class Cable extends StrengthEquipment {
    
    // EFFECTS: Create an instance of this Cable exercise equipment.
    public Cable() {
        super();
        name = "Cable";
    }

    // EFFECTS: Return 'Cable' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
