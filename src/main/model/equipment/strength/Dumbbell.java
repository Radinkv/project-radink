package model.equipment.strength;

/**
 * REPRESENTS: a type of cardio training equipment: Dumbbell. A categorical label for 
 * exercises performed using a dumbbell.
 * 
 * USED BY:
 *      1. Exercises classified as dumbbell-based
 *      2. Workouts that include dumbbell exercises
 * 
 * PURPOSE: This class ensures all dumbbell exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable
 */
public class Dumbbell extends StrengthEquipment {

    // EFFECTS: Create an instance of this Dumbbell exercise equipment.
    public Dumbbell() {
        super();
        name = "Dumbbell";
    }

    // EFFECTS: Return 'Dumbbell' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
