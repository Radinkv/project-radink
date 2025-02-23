package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.equipment.Equipment;
import model.equipment.bodyweight.BodyWeight;
import model.equipment.cardio.Treadmill;
import model.equipment.strength.Barbell;
import model.equipment.strength.Cable;
import model.equipment.strength.Dumbbell;
import model.equipment.strength.Machine;

/** This class tests the IMMUTABLE aspects of Equipment instantiations. */
public class TestEquipment {
    private Equipment bodyweight;
    private Equipment treadmill;
    private Equipment barbell;
    private Equipment cable;
    private Equipment dumbbell;
    private Equipment machine;
    
    @BeforeEach
    void runBefore() {
        bodyweight = new BodyWeight();
        treadmill = new Treadmill();
        barbell = new Barbell();
        cable = new Cable();
        dumbbell = new Dumbbell();
        machine = new Machine();
    }

    @Test
    void testEquipmentName() {
        assertEquals(bodyweight.getEquipmentName(), "Bodyweight");
        assertEquals(treadmill.getEquipmentName(), "Treadmill");
        assertEquals(barbell.getEquipmentName(), "Barbell");
        assertEquals(cable.getEquipmentName(), "Cable");
        assertEquals(dumbbell.getEquipmentName(), "Dumbbell");
        assertEquals(machine.getEquipmentName(), "Machine");
    }

    @Test
    void testEquipmentType() {
        assertEquals(bodyweight.getEquipmentType(), "Body Weight Equipment");
        assertEquals(treadmill.getEquipmentType(), "Cardio Equipment");
        assertEquals(barbell.getEquipmentType(), "Strength Equipment");
        assertEquals(cable.getEquipmentType(), "Strength Equipment");
        assertEquals(dumbbell.getEquipmentType(), "Strength Equipment");
        assertEquals(machine.getEquipmentType(), "Strength Equipment");
    }

    @Test
    void testEquipmentIsWeightBased() {
        assertFalse(bodyweight.isWeightBased());
        assertFalse(treadmill.isWeightBased());
        assertTrue(barbell.isWeightBased());
        assertTrue(cable.isWeightBased());
        assertTrue(dumbbell.isWeightBased());
        assertTrue(machine.isWeightBased());
    }
}
