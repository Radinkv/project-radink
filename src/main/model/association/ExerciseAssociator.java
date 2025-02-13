package model.association;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * REPRESENTS: an entity that tracks associated exercises.
 * 
 * USED BY:
 *      1. Equipment (i.e. Dumbbell, Treadmill, Barbell)
 *      2. Muscle (Muscle class instantiations)
 * 
 * PURPOSE: This abstraction allows tracking exercise volume and duration across different types of workouts.
 */
public abstract class ExerciseAssociator {
    protected Map<String, Map<String, Double>> exerciseMetrics;
    private static final Set<String> VALID_METRICS = new HashSet<String>(Arrays.asList(
            "totalSets", 
            "totalReps", 
            "totalIntervalDuration",
            "totalEnduranceDuration", 
            "totalStrengthDuration",
            "totalDuration", 
            "totalRestTimeBetween"
    ));

    public ExerciseAssociator() {
        exerciseMetrics = new HashMap<String, Map<String, Double>>();
    }

    // MODIFIES: this
    // EFFECTS: Add the given exercise's data if the combination of exerciseName and context is not 
    //          already stored and return true
    //          Filters exerciseInfo keys/fields and only keeps expected metrics
    //          Returns false and makes no changes if one of:
    //                  - exerciseName is null
    //                  - context is null
    //                  - exerciseInfo is null
    //                  - the combination of exerciseName and context already exists
    public boolean registerExercise(String exerciseName, String context, Map<String, Double> exerciseInfo) {
        String key = exerciseName + "-" + context;
        if (exerciseName == null || context == null || exerciseInfo == null 
                || exerciseMetrics.containsKey(key)) {
            return false;
        }
        Map<String, Double> filteredMetrics = new HashMap<>();
        for (Map.Entry<String, Double> entry : exerciseInfo.entrySet()) {
            if (VALID_METRICS.contains(entry.getKey())) {
                filteredMetrics.put(entry.getKey(), entry.getValue());
            }
        }
        exerciseMetrics.put(key, filteredMetrics);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Remove the given exercise's metrics for the specified context if present and return true.
    //          Returns false and makes no changes if:
    //                  - exerciseName is null
    //                  - context is null
    //                  - no exercise exists for the given exerciseName and context combination
    public boolean unregisterExercise(String exerciseName, String context) {
        if (exerciseName == null || context == null) {
            return false;
        }
        String key = exerciseName + "-" + context;
        return exerciseMetrics.remove(key) != null;
    }

    // EFFECTS: Return true if an exercise with the given name exists for the specified context
    //          Returns false if:
    //              - exerciseName is null
    //              - context is null
    //              - no exercise exists for the given exerciseName and context combination
    public boolean containsExercise(String exerciseName, String context) {
        String key = exerciseName + "-" + context;
        if (exerciseName == null || context == null) {
            return false;
        }
        return exerciseMetrics.containsKey(key);
    }

    // EFFECTS: Returns aggregated metrics across all associated exercises.
    //          1. "totalSets": total sets across strength exercises
    //          2. "totalReps": total reps across strength exercises
    //          3. "totalIntervalDuration": total time in interval-based exercises
    //          4. "totalEnduranceDuration": total time in duration-based exercises
    //          5. "totalStrengthDuration": total time in strength-based exercises
    //          6. "totalDuration": total duration across all exercises
    //          7. "totalRestTimeBetween": total rest time in between active portions exercises
    //          Key values are 0 if this entity does not have any exercise(s) that contribute to that metric.
    public Map<String, Double> getAggregatedExerciseMetrics() {
        Map<String, Double> totalMetrics = createZeroValueMetricsMap();
        
        for (Map<String, Double> metrics : exerciseMetrics.values()) {
            for (Map.Entry<String, Double> entry : metrics.entrySet()) {
                String metric = entry.getKey();
                Double value = entry.getValue();
                totalMetrics.merge(metric, value, Double::sum);
            }
        }
        
        return totalMetrics;
    }

    // EFFECTS: Returns the number of associated exercises.
    public int getNumAssociatedExercises() {
        return exerciseMetrics.size();
    }

    // Helper method to create a map with all valid metrics initialized to zero
    private Map<String, Double> createZeroValueMetricsMap() {
        Map<String, Double> metrics = new HashMap<>();
        for (String metricName : VALID_METRICS) {
            metrics.put(metricName, 0.0);
        }
        return metrics;
    }

    // FOR TESTING PURPOSES.
    public void clearExercises() {
        exerciseMetrics.clear();
    }    
}
