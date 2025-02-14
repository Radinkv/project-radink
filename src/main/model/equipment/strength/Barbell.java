package model.equipment.strength;

/**
 * REPRESENTS: a type of cardio training equipment: Barbell. A categorical label for 
 * exercises performed using a barbell.
 * 
 * USED BY:
 *      1. Exercises classified as barbell-based
 *      2. Workouts that include barbell exercises
 * 
 * PURPOSE: This class ensures all barbell exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable (Except ExerciseAssociator)
 */
public class Barbell extends StrengthEquipment {

    // EFFECTS: Create an instance of this Barbell exercise equipment.
    public Barbell() {
        super();
        name = "Barbell";
    }

    // EFFECTS: Return 'Barbell' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
