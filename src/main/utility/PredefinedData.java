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
 */
public class PredefinedData {
    // Equipment instances
    private static final Treadmill TREADMILL = new Treadmill();
    private static final BodyWeight BODYWEIGHT = new BodyWeight();
    private static final Dumbbell DUMBBELL = new Dumbbell();
    private static final Barbell BARBELL = new Barbell();
    private static final Cable CABLE = new Cable();
    private static final Machine MACHINE = new Machine();

    // Basic Muscle instances
    private static final Muscle BICEP = new Muscle("Biceps");
    private static final Muscle BRACHIALIS = new Muscle("Brachialis");
    private static final Muscle TRICEP = new Muscle("Triceps");

    // Updated Forearm muscles (three subdivisions)
    private static final Muscle FOREARM_FLEXORS = new Muscle("Forearm Flexors");
    private static final Muscle FOREARM_EXTENSORS = new Muscle("Forearm Extensors");
    private static final Muscle BRACHIORADIALIS = new Muscle("Brachioradialis");

    // Other basic muscles
    private static final Muscle QUAD = new Muscle("Quadriceps");
    private static final Muscle HAMSTRING = new Muscle("Hamstrings");
    private static final Muscle GLUTE = new Muscle("Glutes");
    private static final Muscle CALF = new Muscle("Calves");
    private static final Muscle ABS = new Muscle("Abs");
    private static final Muscle OBLIQUES = new Muscle("Obliques");
    private static final Muscle REAR_DELTOID = new Muscle("Rear Deltoid");
    private static final Muscle FRONT_DELTOID = new Muscle("Front Deltoid");
    private static final Muscle SIDE_DELTOID = new Muscle("Side Deltoid");
    private static final Muscle UPPER_CHEST = new Muscle("Upper Chest");
    private static final Muscle MIDDLE_CHEST = new Muscle("Middle Chest");
    private static final Muscle LOWER_CHEST = new Muscle("Lower Chest");
    private static final Muscle UPPER_BACK_MUSCLE = new Muscle("Upper Back");
    private static final Muscle MID_BACK_MUSCLE = new Muscle("Mid Back");
    private static final Muscle LOWER_BACK_MUSCLE = new Muscle("Lower Back");
    private static final Muscle LATS = new Muscle("Lats");
    private static final Muscle TRAPS = new Muscle("Traps");

    // Hip-related muscles
    private static final Muscle HIP_FLEXOR = new Muscle("Hip Flexor");
    private static final Muscle HIP_ADDUCTOR = new Muscle("Hip Adductor");
    private static final Muscle HIP_ABDUCTOR = new Muscle("Hip Abductor");
    private static final Muscle PIRIFORMIS = new Muscle("Piriformis");
    private static final Muscle TFL = new Muscle("Tensor Fasciae Latae");
    private static final Muscle IT_BAND = new Muscle("IT Band");

    // Specific Muscle Groups
    private static final MuscleGroup CHEST = 
            new MuscleGroup("Chest", new ArrayList<Muscle>(List.of(UPPER_CHEST, MIDDLE_CHEST, LOWER_CHEST)));
    private static final MuscleGroup SHOULDER = 
            new MuscleGroup("Shoulders", new ArrayList<Muscle>(List.of(FRONT_DELTOID, SIDE_DELTOID, REAR_DELTOID)));
    private static final MuscleGroup BICEPS = 
            new MuscleGroup("Biceps", new ArrayList<Muscle>(List.of(BICEP, BRACHIALIS)));
    private static final MuscleGroup TRICEPS = 
            new MuscleGroup("Triceps", new ArrayList<Muscle>(List.of(TRICEP)));
    private static final MuscleGroup FOREARMS = 
            new MuscleGroup("Forearms", 
                    new ArrayList<Muscle>(List.of(FOREARM_FLEXORS, FOREARM_EXTENSORS, BRACHIORADIALIS)));
    private static final MuscleGroup UPPER_BACK = 
            new MuscleGroup("Upper Back", new ArrayList<Muscle>(List.of(TRAPS, UPPER_BACK_MUSCLE, REAR_DELTOID)));
    private static final MuscleGroup MID_BACK = 
            new MuscleGroup("Mid Back", new ArrayList<Muscle>(List.of(LATS, MID_BACK_MUSCLE)));
    private static final MuscleGroup LOWER_BACK = 
            new MuscleGroup("Lower Back", new ArrayList<Muscle>(List.of(LOWER_BACK_MUSCLE)));
    private static final MuscleGroup CORE = 
            new MuscleGroup("Core", new ArrayList<Muscle>(List.of(ABS, OBLIQUES, LOWER_BACK_MUSCLE)));
    private static final MuscleGroup QUADS = 
            new MuscleGroup("Quadriceps", new ArrayList<Muscle>(List.of(QUAD)));
    private static final MuscleGroup HAMSTRINGS = 
            new MuscleGroup("Hamstrings", new ArrayList<Muscle>(List.of(HAMSTRING)));
    private static final MuscleGroup CALVES = 
            new MuscleGroup("Calves", new ArrayList<Muscle>(List.of(CALF)));
    private static final MuscleGroup HIPS = 
            new MuscleGroup("Hips", 
                    new ArrayList<Muscle>(List.of(HIP_FLEXOR, HIP_ADDUCTOR, HIP_ABDUCTOR, PIRIFORMIS, TFL, IT_BAND)));

    // Compound Movement Groups
    // Deadlift involves a strong grip, so we use Forearm Flexors
    private static final MuscleGroup DEADLIFT = 
            new MuscleGroup("Deadlift Muscles", 
                    new ArrayList<Muscle>(List.of(QUAD, HAMSTRING, GLUTE, 
                            LOWER_BACK_MUSCLE, TRAPS, LATS, FOREARM_FLEXORS)));
    private static final MuscleGroup SQUAT = 
            new MuscleGroup("Squat Muscles", 
                    new ArrayList<Muscle>(List.of(QUAD, HAMSTRING, GLUTE, LOWER_BACK_MUSCLE)));
    private static final MuscleGroup BENCH = 
            new MuscleGroup("Bench Muscles", 
                    new ArrayList<Muscle>(List.of(MIDDLE_CHEST, FRONT_DELTOID, TRICEP)));
    private static final MuscleGroup OVERHEAD_PRESS = 
            new MuscleGroup("Overhead Press Muscles", 
                    new ArrayList<Muscle>(List.of(FRONT_DELTOID, SIDE_DELTOID, TRICEP, UPPER_CHEST)));
    private static final MuscleGroup LUNGE = 
            new MuscleGroup("Lunge Muscles", 
                    new ArrayList<Muscle>(List.of(QUAD, HAMSTRING, GLUTE, HIP_FLEXOR, HIP_ADDUCTOR, ABS)));
    // Row movements benefit from the brachioradialis (often targeted by hammer or reverse grip curls)
    private static final MuscleGroup ROW = 
            new MuscleGroup("Row Muscles", 
                    new ArrayList<Muscle>(List.of(UPPER_BACK_MUSCLE, MID_BACK_MUSCLE, LATS, 
                            REAR_DELTOID, BICEP, BRACHIORADIALIS)));

    // EFFECTS: Returns an unmodifiable map containing all predefined MuscleGroup instances mapped by name
    public Map<String, Muscle> getAllMuscles() {
        Map<String, Muscle> muscles = new HashMap<String, Muscle>();
        // Basic muscles
        muscles.put("Biceps", BICEP);
        muscles.put("Brachialis", BRACHIALIS);
        muscles.put("Triceps", TRICEP);
        muscles.put("Forearm Flexors", FOREARM_FLEXORS);
        muscles.put("Forearm Extensors", FOREARM_EXTENSORS);
        muscles.put("Brachioradialis", BRACHIORADIALIS);
        muscles.put("Quadriceps", QUAD);
        muscles.put("Hamstrings", HAMSTRING);
        muscles.put("Glutes", GLUTE);
        muscles.put("Calves", CALF);
        muscles.put("Abs", ABS);
        muscles.put("Obliques", OBLIQUES);
        muscles.put("Rear Deltoid", REAR_DELTOID);
        muscles.put("Front Deltoid", FRONT_DELTOID);
        muscles.put("Side Deltoid", SIDE_DELTOID);
        muscles.put("Upper Chest", UPPER_CHEST);
        muscles.put("Middle Chest", MIDDLE_CHEST);
        muscles.put("Lower Chest", LOWER_CHEST);
        muscles.put("Upper Back", UPPER_BACK_MUSCLE);
        muscles.put("Mid Back", MID_BACK_MUSCLE);
        muscles.put("Lower Back", LOWER_BACK_MUSCLE);
        muscles.put("Lats", LATS);
        muscles.put("Traps", TRAPS);
        muscles.put("Hip Flexor", HIP_FLEXOR);
        muscles.put("Hip Adductor", HIP_ADDUCTOR);
        muscles.put("Hip Abductor", HIP_ABDUCTOR);
        muscles.put("Piriformis", PIRIFORMIS);
        muscles.put("Tensor Fasciae Latae", TFL);
        muscles.put("IT Band", IT_BAND);
        return Collections.unmodifiableMap(muscles);
    }

    // EFFECTS: Return an unmodifiable map containing all predefined Muscle instances mapped by name
    public Map<String, MuscleGroup> getAllMuscleGroups() {
        Map<String, MuscleGroup> groups = new HashMap<String, MuscleGroup>();
        // Compound groups
        groups.put("Deadlift Muscles", DEADLIFT);
        groups.put("Squat Muscles", SQUAT);
        groups.put("Bench Muscles", BENCH);
        groups.put("Overhead Press Muscles", OVERHEAD_PRESS);
        groups.put("Lunge Muscles", LUNGE);
        groups.put("Row Muscles", ROW);
        // Specific groups
        groups.put("Chest", CHEST);
        groups.put("Shoulders", SHOULDER);
        groups.put("Biceps", BICEPS);
        groups.put("Triceps", TRICEPS);
        groups.put("Forearms", FOREARMS);
        groups.put("Upper Back", UPPER_BACK);
        groups.put("Mid Back", MID_BACK);
        groups.put("Lower Back", LOWER_BACK);
        groups.put("Core", CORE);
        groups.put("Quadriceps", QUADS);
        groups.put("Hamstrings", HAMSTRINGS);
        groups.put("Calves", CALVES);
        groups.put("Hips", HIPS);
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

    // EFFECTS: Returns the MuscleGroup instance corresponding to the given name, or default if not found
    public MuscleGroup findMuscleGroup(String name) {
        MuscleGroup findMuscleGroup = getAllMuscleGroups().get(name);
        return (findMuscleGroup != null) ? findMuscleGroup : defaultMuscleGroup();
    }

    // EFFECTS: Returns the Equipment instance corresponding to the given name, or default if not found
    public Equipment findEquipment(String name) {
        Equipment findEquipment = getAllEquipment().get(name);
        return (findEquipment != null) ? findEquipment : defaultEquipment();
    }

    // EFFECTS: Return null initiated MuscleGroup as default
    private MuscleGroup defaultMuscleGroup() {
        return new MuscleGroup(null, null);
    }

    // EFFECTS: Return Bodyweight Equipment (no equipment) as default
    private Equipment defaultEquipment() {
        return getAllEquipment().get("Bodyweight");
    }
}
