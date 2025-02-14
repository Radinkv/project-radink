package model.exercise;

import java.util.HashMap;
import java.util.Map;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;
import model.muscle.MuscleGroup;

/**
 * REPRESENTS: an interval-based exercise performed for a set duration
 * 
 * USED BY:
 *      1. Workouts that include interval exercises
 *      2. MuscleGroups that track interval-based exercise impacts
 *      3. Equipment that categorizes exercises based on usage
 * 
 * PURPOSE: Models an interval exercise where the primary variable is time spent performing it.
 *          Tracks exercise name, duration, equipment used, and targeted muscles.
 * 
 * MUTABILITY: Immutable 
 */
public class IntervalExercise extends Exercise {

    // REQUIRES: timeOn, timeOff, numRepetitions > 0
    /* EFFECTS: Creates an instance of an interval-based exercise, initializing this exercise's:
             1. Name
             2. Active time (seconds) and rest time (seconds) per each repetition
             3. Number of repetitions
             4. Equipment used for this exercise
             5. Targeted muscle group(s) */
    public IntervalExercise(String name, double timeOn, double timeOff, 
            int numRepetitions, Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        super(name, "Interval", equipmentUsed, musclesTargeted);
        
        // Handling extreme values:
        // Limit each repetition's time (active AND rest) so that the total (time * repetitions)
        // remains below Double.MAX_VALUE
        // Dividing by (2 * max(1, repetitions)) safely gives this margin
        // The 2 * multiplication could be removed, but it's extremely safe
        // For normal inputs, the values will remain unchanged
        // safeTimeOn is always > 0 (at least Double.MIN_NORMAL) and safeTimeOff is >= 0
        double safeTimeOn = Math.max(Double.MIN_NORMAL, 
                Math.min(timeOn, Double.MAX_VALUE / (2 * Math.max(1, numRepetitions))));

        double safeTimeOff = Math.max(0, 
                Math.min(timeOff, Double.MAX_VALUE / (2 * Math.max(1, numRepetitions))));

                
        int safeReps = Math.max(0, Math.min(numRepetitions, Integer.MAX_VALUE / 2));

        exerciseInfo.put("timeOn", safeTimeOn);
        exerciseInfo.put("timeOff", safeTimeOff);
        exerciseInfo.put("repititions", (double) safeReps);
    }

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Send a copy of this Exercise's getInfo, along with this exercise's name
    //          to Equipment and MuscleGroup. If already present, make no changes.
    //          Do nothing if this exercise has null Equipment or MuscleGroup, respectively.
    @Override
    public void activateMetrics(String context) {
        Map<String, Double> metrics = convertInfoToAssociatorFormat();
        
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
    //          and MuscleGroup. If not present, make no changes.
    //          Do nothing if this exercise has null Equipment or MuscleGroup, respectively.
    @Override
    public void deactivateMetrics(String context) {
        if (requiredEquipment instanceof ExerciseAssociator) {
            ((ExerciseAssociator) requiredEquipment).unregisterExercise(getName(), context);
        }
        if (musclesTargeted != null) {
            musclesTargeted.unregisterMusclesFromMetrics(getName(), context);
        }
    }

    // EFFECTS: Return this interval exercise's total duration in minutes.
    //          (Calculated as the sum of the active and rest portions, then converted from seconds to minutes.)
    @Override
    public double getDuration() {
        double timeOn = exerciseInfo.get("timeOn");
        double timeOff = exerciseInfo.get("timeOff");
        double repetitions = exerciseInfo.get("repititions");
        return (timeOn + timeOff) * repetitions;
    }
    
    // EFFECTS: Return key-value pairs of the raw exercise information:
    //          "timeOn", "timeOff", and "repititions".
    @Override
    public Map<String, Double> getInfo() {
        Map<String, Double> info = new HashMap<String, Double>(exerciseInfo);
        info.put("totalDuration", getDuration());
        return info;
    }

    // EFFECTS: Converts the raw interval exercise info into the aggregated metrics expected by ExerciseAssociator.
    //          Includes:
    //              1. "totalIntervalDuration"
    //              2. "totalRestTimeBetween"
    //              3. "totalDuration"
    public Map<String, Double> convertInfoToAssociatorFormat() {
        Map<String, Double> metrics = new HashMap<String, Double>();
        double timeOff = exerciseInfo.get("timeOff");
        double repetitions = exerciseInfo.get("repititions");
        metrics.put("totalIntervalDuration", getDuration());
        double totalRestTime = timeOff * repetitions;
        metrics.put("totalRestTimeBetween", totalRestTime);
        metrics.put("totalDuration", getDuration());
        return metrics;
    }
}
