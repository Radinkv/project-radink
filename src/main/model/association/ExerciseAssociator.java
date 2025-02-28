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
 * 
 * NOTE: The relationship between containers of ExerciseAssociator (MuscleGroup, Exercise)
 *       is an association relationship. ExerciseAssociator does NOT maintain model object references.
 *       Instead, Exercises associate their metrics with ExerciseAssociator instances. This association
 *       occurs under a specific context (day of the week in WeeklySchedule) IFF the Exercise
 *       exists within a Workout that has been added to WeeklySchedule. In this scenario,
 *       WeeklySchedule aggregates the Exercise's metrics because it indicates that this
 *       ExerciseAssociator instance is actively being used by the Exercise object (which is
 *       contained within the user's Workout in the schedule).
 * 
 *       The purpose of this association is to display active Workout and Exercise volume and duration 
 *       metrics for Equipment and Muscle objects (which extend the ExerciseAssociator abstract class).
 */
public abstract class ExerciseAssociator {
    private Map<String, Map<String, Double>> exerciseMetrics;
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
    // EFFECTS: Create new exercise registration with filtered metrics if exerciseName-context metrics are not in this
    //          Return true only if exerciseName, context, exerciseInfo are non-null and registration is successful
    public boolean registerExercise(String exerciseName, String context, Map<String, Double> exerciseInfo) {
        String key = exerciseName + "-" + context;
        if (exerciseName == null || context == null || exerciseInfo == null 
                || exerciseMetrics.containsKey(key)) {
            return false;
        }
        Map<String, Double> filteredMetrics = new HashMap<String, Double>();
        for (Map.Entry<String, Double> entry : exerciseInfo.entrySet()) {
            if (VALID_METRICS.contains(entry.getKey())) {
                filteredMetrics.put(entry.getKey(), entry.getValue());
            }
        }
        exerciseMetrics.put(key, filteredMetrics);
        return true;
    }

    // MODIFIES: this
    // EFFECTS: Remove exercise metrics for the given exerciseName-context's metrics if it exists in this
    //          Return true only if exerciseName and context are non-null and removal is successful
    public boolean unregisterExercise(String exerciseName, String context) {
        if (exerciseName == null || context == null) {
            return false;
        }
        String key = exerciseName + "-" + context;
        return exerciseMetrics.remove(key) != null;
    }

    // EFFECTS: Return true if exerciseName-context pair exists in exerciseMetrics
    //          Return false if either parameter is null or a pair is not found
    public boolean containsExercise(String exerciseName, String context) {
        String key = exerciseName + "-" + context;
        if (exerciseName == null || context == null) {
            return false;
        }
        return exerciseMetrics.containsKey(key);
    }

    // EFFECTS: Sum all metric values across registered exercises
    //          Initialize missing metrics to 0.0
    //          Return a map of aggregated metrics
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

    // EFFECTS: Return the number of associated exercises
    public int getNumAssociatedExercises() {
        return exerciseMetrics.size();
    }

    // EFFECTS: Create a map with all valid metrics initialized to zero
    public static Map<String, Double> createZeroValueMetricsMap() {
        Map<String, Double> metrics = new HashMap<String, Double>();
        for (String metricName : VALID_METRICS) {
            metrics.put(metricName, 0.0);
        }
        return metrics;
    }

    public Map<String, Map<String, Double>> getRawExerciseMetrics() {
        return new HashMap<String, Map<String, Double>>(exerciseMetrics);
    }

    // FOR TESTING PURPOSES.
    public void clearExercises() {
        exerciseMetrics.clear();
    }    
}
