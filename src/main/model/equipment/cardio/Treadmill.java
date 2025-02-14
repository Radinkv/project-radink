package model.equipment.cardio;

/**
 * REPRESENTS: a type of cardio training equipment: Treadmill. A categorical label for 
 * exercises performed using treadmill.
 * 
 * USED BY:
 *      1. Exercises classified as treadmill-based
 *      2. Workouts that include treadmill exercises
 * 
 * PURPOSE: This class ensures all treadmill exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable (Except ExerciseAssociator)
 */
public class Treadmill extends CardioEquipment {

    // EFFECTS: Create an instance of this treadmill exercise equipment.
    public Treadmill() {
        super();
        name = "Treadmill";
    }

    // EFFECTS: Return 'Treadmill' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
