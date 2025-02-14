package utility;

import model.equipment.Equipment;
import model.equipment.bodyweight.BodyWeight;
import model.equipment.cardio.Treadmill;
import model.equipment.strength.Barbell;
import model.equipment.strength.Cable;
import model.equipment.strength.Dumbbell;
import model.equipment.strength.Machine;
import model.muscle.Muscle;
import model.muscle.MuscleGroup;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * REPRESENTS: A centralized repository of all predefined Muscles, MuscleGroups, and Equipment.
 * 
 * USED BY:
 *      1. UI components that need access to predefined exercise elements.
 *      2. Exercise objects that reference specific Equipment and MuscleGroups.
 *      3. Workout and scheduling systems for consistency across entities.
 * 
 * PURPOSE: This class ensures all core domain entities are pre-initialized before runtime,
 *          providing a single access point for muscle, muscle group, and equipment references.
 * 
 * MUTABILITY: Mutable (entities track exercise associations and update dynamically).
 */
public class PredefinedData {

    // The Equipment instances
    private static Treadmill TREADMILL = new Treadmill();
    private static BodyWeight BODYWEIGHT = new BodyWeight();
    private static Dumbbell DUMBBELL = new Dumbbell();
    private static Barbell BARBELL = new Barbell();
    private static Cable CABLE = new Cable();
    private static Machine MACHINE = new Machine();

    // The Muscle instances
    private static Muscle BICEP = new Muscle("Biceps");
    private static Muscle BRACHIALIS = new Muscle("Brachialis");
    private static Muscle TRICEP = new Muscle("Triceps");
    private static Muscle FOREARM = new Muscle("Forearm");
    private static Muscle QUAD = new Muscle("Quadriceps");
    private static Muscle HAMSTRING = new Muscle("Hamstrings");
    private static Muscle LATS = new Muscle("Lats");
    private static Muscle TRAPS = new Muscle("Traps");
    private static Muscle LOWER_BACK = new Muscle("Lower Back");
    private static Muscle CHEST = new Muscle("Chest");
    private static Muscle DELTOID = new Muscle("Deltoid");
    private static Muscle GLUTE = new Muscle("Glutes");
    private static Muscle CALF = new Muscle("Calves");
    private static Muscle ABS = new Muscle("Abs");
    private static Muscle OBLIQUES = new Muscle("Obliques");
    
    // The MuscleGroup instances built from Muscle instances
    private static MuscleGroup UPPER_BODY = 
            new MuscleGroup("Upper Body", new ArrayList<Muscle>(List.of(CHEST, DELTOID, BICEP, TRICEP, FOREARM)));
    private static MuscleGroup LOWER_BODY = 
            new MuscleGroup("Lower Body", new ArrayList<Muscle>(List.of(QUAD, HAMSTRING, GLUTE, CALF)));
    private static MuscleGroup BACK = 
            new MuscleGroup("Back", new ArrayList<Muscle>(List.of(LATS, TRAPS, LOWER_BACK)));
    private static MuscleGroup CHEST_SHOULDERS = 
            new MuscleGroup("Chest & Shoulders", new ArrayList<Muscle>(List.of(CHEST, DELTOID)));
    private static MuscleGroup ARMS = 
            new MuscleGroup("Arms", new ArrayList<Muscle>(List.of(BICEP, BRACHIALIS, TRICEP, FOREARM)));
    private static MuscleGroup LEGS = 
            new MuscleGroup("Legs", new ArrayList<Muscle>(List.of(QUAD, HAMSTRING, GLUTE, CALF)));
    private static MuscleGroup CORE = 
            new MuscleGroup("Core", new ArrayList<Muscle>(List.of(LOWER_BACK, ABS, OBLIQUES)));

    // EFFECTS: Return an unmodifiable map containing all predefined Muscle instances mapped by name
    public Map<String, Muscle> getAllMuscles() {
        Map<String, Muscle> muscles = new HashMap<String, Muscle>();
        muscles.put("Biceps", BICEP);
        muscles.put("Brachialis", BRACHIALIS);
        muscles.put("Triceps", TRICEP);
        muscles.put("Forearm", FOREARM);
        muscles.put("Quadriceps", QUAD);
        muscles.put("Hamstrings", HAMSTRING);
        muscles.put("Lats", LATS);
        muscles.put("Traps", TRAPS);
        muscles.put("Lower Back", LOWER_BACK);
        muscles.put("Chest", CHEST);
        muscles.put("Deltoid", DELTOID);
        muscles.put("Glutes", GLUTE);
        muscles.put("Calves", CALF);
        muscles.put("Abs", ABS);
        muscles.put("Obliques", OBLIQUES);
        return Collections.unmodifiableMap(muscles);
    }

    // EFFECTS: Returns an unmodifiable map containing all predefined MuscleGroup instances mapped by name
    public Map<String, MuscleGroup> getAllMuscleGroups() {
        Map<String, MuscleGroup> groups = new HashMap<String, MuscleGroup>();
        groups.put("Upper Body", UPPER_BODY);
        groups.put("Lower Body", LOWER_BODY);
        groups.put("Back", BACK);
        groups.put("Legs", LEGS);
        groups.put("Chest & Shoulders", CHEST_SHOULDERS);
        groups.put("Arms", ARMS);
        groups.put("Core", CORE);
        return Collections.unmodifiableMap(groups);
    }

    // EFFECTS: Returns an unmodifiable map containing all predefined Equipment instances mapped by name
    public Map<String, Equipment> getAllEquipment() {
        Map<String, Equipment> equipment = new HashMap<String, Equipment>();
        equipment.put("Treadmill", TREADMILL);
        equipment.put("Bodyweight", BODYWEIGHT);
        equipment.put("Dumbbell", DUMBBELL);
        equipment.put("Barbell", BARBELL);
        equipment.put("Cable", CABLE);
        equipment.put("Machine", MACHINE);
        return Collections.unmodifiableMap(equipment);
    }

    // EFFECTS: Returns the Muscle instance corresponding to the given name, or null if not found
    public Muscle findMuscle(String name) {
        return getAllMuscles().get(name);
    }

    // EFFECTS: Returns the Equipment instance corresponding to the given name, or null if not found
    public Equipment findEquipment(String name) {
        return getAllEquipment().get(name);
    }
}
