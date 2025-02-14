package model.exercise;

import java.util.HashMap;
import java.util.Map;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;
import model.muscle.MuscleGroup;


/**
 * REPRESENTS: an strength-based exercise performed for a set duration
 * 
 * USED BY:
 *      1. Workouts that include strength exercises
 *      2. MuscleGroups that track strength-based exercise impacts
 *      3. Equipment that categorizes exercises based on usage
 * 
 * PURPOSE: Models an strength exercise where the primary variable is time spent performing it
 *          Tracks exercise name, duration, equipment used, and targeted muscles
 * 
 * MUTABILITY: Immutable
 */
public class StrengthExercise extends Exercise {

    // EFFECTS: Create an instance of a strength/hypertrophy exercise, initializing:
    //          1. This exercise's name
    //          2. Number of sets and reps
    //          3, 4. Time per rep (seconds) and rest time (minutes)
    //          5. The equipment used for this exercise
    //          6. Targeted muscle group(s) 
    //          IF ANY OF sets, reps, numSecondsPerRep < 0, they are defaulted (1 for sets and reps, 0 for others)
    public StrengthExercise(String name, int sets, int reps, double numSecondsPerRep, 
            double restTime, Equipment equipmentUsed, MuscleGroup musclesTargeted) {
        super(name, "Strength", equipmentUsed, musclesTargeted);

        // Handle extreme values to prevent potential overflow (for robustness)
        double maxSafeValue = Math.cbrt(Double.MAX_VALUE);

        // Non negative values at all times
        double safeSets = Math.min(Math.max(1, sets), maxSafeValue);
        double safeReps = Math.min(Math.max(1, reps), maxSafeValue);
        double safeSecondsPerRep = Math.min(Math.max(0, numSecondsPerRep), maxSafeValue);
        double safeRestTime = Math.min(Math.max(0, restTime), maxSafeValue);

        exerciseInfo.put("sets", safeSets);
        exerciseInfo.put("reps", safeReps);
        exerciseInfo.put("timePerRep", safeSecondsPerRep);
        exerciseInfo.put("restTime", safeRestTime);
    }

    // MODIFIES: MuscleGroup, Equipment
    // EFFECTS: Send a copy of this Exercise's getInfo, along with this exercise's name
    //          to Equipment and MuscleGroup. If already present, make no changes
    //          Do nothing if this exercise has null Equipment or MuscleGroup, respectively
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
    //          and MuscleGroup; If not present, make no changes
    //          Do nothing if this exercise has null Equipment or MuscleGroup
    @Override
    public void deactivateMetrics(String context) {
        if (requiredEquipment instanceof ExerciseAssociator) {
            ((ExerciseAssociator) requiredEquipment).unregisterExercise(getName(), context);
        }
        if (musclesTargeted != null) {
            musclesTargeted.unregisterMusclesFromMetrics(getName(), context);
        }
    }

    // EFFECTS: Calculate and return this exercise's total duration, in seconds
    @Override
    public double getDuration() {
        double sets = exerciseInfo.get("sets");
        double reps = exerciseInfo.get("reps");
        double timePerRep = exerciseInfo.get("timePerRep");
        double restTime = exerciseInfo.get("restTime");
        double activeTime = sets * reps * timePerRep;
        double restTimeSeconds = sets * (restTime * 60);
        return activeTime + restTimeSeconds;
    }

    // EFFECTS: Return key-value pairs of information about this exercise in the form: 
    //          1, 2. 'sets' and 'reps' (number of repetitions) 
    //          3. 'restTime' (duration in seconds)
    //          4. 'timePerRep' (duration in seconds)
    @Override
    public Map<String, Double> getInfo() {
        Map<String, Double> info = new HashMap<String, Double>();
        // Include all metrics with safe defaults
        info.put("sets", exerciseInfo.get("sets"));
        info.put("reps", exerciseInfo.get("reps"));
        info.put("timePerRep", exerciseInfo.get("timePerRep"));
        info.put("restTime", exerciseInfo.get("restTime"));
        info.put("totalDuration", getDuration());
        return info;
    }
    
    // EFFECTS: Converts the raw strength exercise info into the aggregated metrics expected by ExerciseAssociator
    //          from a StrengthExercise; Includes:
    //              1. "totalSets"
    //              2. "totalReps"
    //              3. "totalStrengthDuration"
    //              4. "totalRestTimeBetween"
    //              5. "totalDuration"
    public Map<String, Double> convertInfoToAssociatorFormat() {
        Map<String, Double> metrics = new HashMap<String, Double>();
        double sets = exerciseInfo.get("sets");
        double reps = exerciseInfo.get("reps");
        double restTime = exerciseInfo.get("restTime");
        metrics.put("totalSets", sets);
        metrics.put("totalReps", reps * sets);
        metrics.put("totalStrengthDuration", getDuration());
        double restTimeSeconds = sets * (restTime * 60);
        metrics.put("totalRestTimeBetween", restTimeSeconds);
        metrics.put("totalDuration", getDuration());
        return metrics;
    }
}
