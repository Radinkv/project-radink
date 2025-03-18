package model.exercise;

import java.util.HashMap;
import java.util.Map;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;
import model.muscle.MuscleGroup;

/**
 * REPRESENTS: an abstract base class for different types of exercises
 * 
 * USED BY:
 *      1. WeeklySchedule to activate metrics for this exercise based on equipment and muscle group
 *      2. Strength exercises that track sets, reps, and time per rep
 *      3. Endurance exercises that track total duration
 *      4. Interval-based exercises that track time spent exercising and resting
 * 
 * PURPOSE: Defines a standard interface for different styles of exercises
 *          Allow structured user exercise customization and analytical tracking
 *          Enforce separation of exercise types based on training style
 * 
 * See Exercise subclasses for detailed method specifications
 */
public abstract class Exercise {
    protected Map<String, Double> exerciseInfo;
    protected String name;
    protected String type;
    protected Equipment requiredEquipment;
    protected MuscleGroup musclesTargeted;

    protected Exercise(String name, String type, Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        exerciseInfo = new HashMap<String, Double>();
        this.type = (type != null && !type.trim().isEmpty()) ? type : "Unknown Type";
        this.name = (name != null && !name.trim().isEmpty()) ? name : "Unnamed Exercise";
        this.requiredEquipment = equipmentUsed;
        this.musclesTargeted = musclesTargeted;
    }
    
    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Send a copy of this Exercise's getInfo, along with this exercise's name
    //          to Equipment and MuscleGroup; If already present, make no changes
    //          Do nothing if this exercise has null Equipment or MuscleGroup
    public void activateMetrics(String context) {
        Map<String, Double> metrics = convertInfoToAssociatorFormat();
        // Safety; Equipment does not HAVE to be ExerciseAssociator
        // However, this program currently does design each instance of Equipment as an instance of ExerciseAssociator
        if (requiredEquipment instanceof ExerciseAssociator) { 
            ((ExerciseAssociator) requiredEquipment).registerExercise(getName(), context, 
                new HashMap<String, Double>(metrics));
        }
        if (musclesTargeted != null) {
            musclesTargeted.registerMusclesForMetrics(getName(), context, new HashMap<String, Double>(metrics));
        }
    }


    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Remove copy of this Exercise's getInfo from Equipment
    //          and MuscleGroup; If not present, make no changes
    //          Do nothing if this exercise has null Equipment or MuscleGroup
    public void deactivateMetrics(String context) {
        if (requiredEquipment instanceof ExerciseAssociator) {
            ((ExerciseAssociator) requiredEquipment).unregisterExercise(getName(), context);
        }
        if (musclesTargeted != null) {
            musclesTargeted.unregisterMusclesFromMetrics(getName(), context);
        }
    }

    // EFFECTS: Return name of this exercise
    public String getName() {
        return name;
    }

    // EFFECTS: Return this exercise's training style
    public String exerciseType() {
        return type;
    }

    // EFFECTS: Return equipment used for this exercise
    public Equipment getRequiredEquipment() {
        return requiredEquipment;
    }

    // EFFECTS: Return muscle group targeted by this exercise
    public MuscleGroup getMusclesTargeted() {
        return musclesTargeted;
    }

    public abstract double getDuration();

    public abstract Map<String, Double> getInfo();

    public abstract Map<String, Double> convertInfoToAssociatorFormat();
}
