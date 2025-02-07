package model.equipment;

/**
 * Represents the basic abstraction of a piece of exercise equipment.
 * All instantiated instances of exercise equipments MUST be able to:
 * 
 * 1. Associate exercise(s) the user specifies that use the specified equipment
 * 
 * 2. Retrieve these exercises, and:
 * 
 * 3. Provide basic insights about the equipment and its usage.
 * 
 * Equipment does not represent an individual item like a weight plate, but rather a PHYSICAL mode, 
 * or style, of exercise (apart from the method of exercise; i.e. strength training, endurance training, etc.).
 * 
 * See abstract classes for detailed method specification clauses.
*/
public interface Equipment {
    String getEquipmentType();

    String getEquipmentName();
    
    boolean isWeightBased();
}
