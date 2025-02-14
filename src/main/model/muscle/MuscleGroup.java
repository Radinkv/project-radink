package model.muscle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.association.ExerciseAssociator;

/**
 * REPRESENTS: A collection of muscles that are trained together
 * 
 * USED BY:
 *      1. Exercise classes that target multiple muscles
 *      2. Workout classes that cumulate Exercise objects
 * 
 * PURPOSE: Tracks the impact of exercises on a group of muscles
 *          Calls register/unregister on each individual muscle
 * 
 * MUTABILITY: Mutable (Muscle (ExerciseAssociator subclass) objects in this MuscleGroup)
 */
public class MuscleGroup {
    private String name;
    private Set<Muscle> muscles;


    // EFFECTS: Initialize MuscleGroup with given name
    //          Add all non-null muscles from provided list to group
    //          If muscles list is null or empty, create empty group
    public MuscleGroup(String name, List<Muscle> muscles) {
        this.name = name;
        this.muscles = new HashSet<Muscle>();

        if (muscles != null) {
            for (Muscle m : muscles) {
                if (m != null) {
                    this.muscles.add(m);
                }
            }
        }
    }

    // MODIFIES: this, Muscle (ExerciseAssociator)
    // EFFECTS: Register exercise metrics for all muscles in group within given context
    //          Return true only if all registrations are successful and 
    //          exerciseName, context, exerciseInfo are each non-null
    public boolean registerMusclesForMetrics(String exerciseName, String context, Map<String, Double> exerciseInfo) {
        if (exerciseName == null || context == null || exerciseInfo == null || muscles.isEmpty()) {
            return false;
        }
        boolean success = true;
        for (Muscle muscle : muscles) {
            success &= muscle.registerExercise(exerciseName, context, exerciseInfo);
        }
        return success;
    }


    // MODIFIES: this, Muscle (ExerciseAssociator)
    // EFFECTS: Remove exercise metrics from all muscles in group for given context
    //          Return true only if all unregistrations are successful and 
    //          exerciseName, context, exerciseInfo are each non-null
    public boolean unregisterMusclesFromMetrics(String exerciseName, String context) {
        if (exerciseName == null || context == null) {
            return false;
        }
        boolean success = true;
        for (Muscle muscle : muscles) {
            success &= muscle.unregisterExercise(exerciseName, context);
        }
        return success;
    }

    // EFFECTS: Return collection of (the set) of muscles contained within this group
    public Set<Muscle> getMuscles() {
        return muscles;
    }

    // EFFECTS: Return the name of this MuscleGroup
    public String getName() {
        return name;
    }

    // EFFECTS: Aggregate unique exercise metrics across all muscles in group
    //          Avoid double counting the same exercise instance's metrics shared between muscles
    //          Return empty map if group has no muscles
    public Map<String, Double> getGroupMetrics() {
        Map<String, Double> groupMetrics = new HashMap<String, Double>();
        if (muscles.isEmpty()) {
            return groupMetrics;
        }

        // Create a map with all valid metrics initialized to zero
        groupMetrics = ExerciseAssociator.createZeroValueMetricsMap();

        // Track which `exerciseName-context` combinations have been processed
        Set<String> processedKeys = new HashSet<String>();

        // Process each muscle's exercise metrics
        for (Muscle muscle : muscles) {

            // Get raw metrics containing exercise-context: metrics, level data
            Map<String, Map<String, Double>> rawMetrics = muscle.getRawExerciseMetrics();

            // Iterate through each exercise-context entry
            for (Map.Entry<String, Map<String, Double>> entry : rawMetrics.entrySet()) {
                String exerciseKey = entry.getKey();

                // Only process this exercise-context combination if not seen before
                if (processedKeys.add(exerciseKey)) {
                    Map<String, Double> metrics = entry.getValue();

                    // Add each metric value to totals in ExerciseAssociator format
                    for (Map.Entry<String, Double> metricEntry : metrics.entrySet()) {
                        groupMetrics.merge(metricEntry.getKey(), metricEntry.getValue(), Double::sum);
                    }
                }
            }
        }
        return groupMetrics;
    }
}
