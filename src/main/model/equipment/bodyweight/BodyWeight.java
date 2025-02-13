package model.equipment.bodyweight;

/**
 * REPRESENTS: a type of cardio training equipment: Bodyweight. A categorical label for 
 * exercises performed using bodyweight.
 * 
 * USED BY:
 *      1. Exercises classified as bodyweight-based
 *      2. Workouts that include bodyweight exercises
 * 
 * PURPOSE: This class ensures all bodyweight exercises refer to a single instance of this equipment,
 *          which allows for cumulating exercise metrics unique to this euipment.
 * 
 * MUTABILITY: Immutable
 */
public class BodyWeight extends BodyWeightEquipment {

    // EFFECTS: Constructs an instance of BodyWeight equipment.
    public BodyWeight() {
        super();
        name = "Body Weight";
    }

    // EFFECTS: Return 'Body Weight' as the equipment name of this equipment.
    @Override
    public String getEquipmentName() {
        return name;
    }
}
