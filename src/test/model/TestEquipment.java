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
        assertEquals(bodyweight.getEquipmentName(), "Body Weight");
        assertEquals(treadmill.getEquipmentName(), "Treadmill");
        assertEquals(barbell.getEquipmentName(), "Barbell");
        assertEquals(cable.getEquipmentName(), "Cable");
        assertEquals(dumbbell.getEquipmentName(), "Dumbbell");
        assertEquals(machine.getEquipmentName(), "Machine");
    }

    @Test
    void testEquipmentType() {
        assertEquals(bodyweight.getEquipmentType(), "Body Weight");
        assertEquals(treadmill.getEquipmentType(), "Cardio");
        assertEquals(barbell.getEquipmentType(), "Strength");
        assertEquals(cable.getEquipmentType(), "Strength");
        assertEquals(dumbbell.getEquipmentType(), "Strength");
        assertEquals(machine.getEquipmentType(), "Strength");
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
