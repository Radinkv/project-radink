package model.muscle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
 * MUTABILITY: Immutable
 */
public class MuscleGroup {


    public MuscleGroup() {

    }

    public MuscleGroup(String name, ArrayList<Muscle> muscles) {

    }

    // EFFECTS: Return the set of muscles contained within this group
    public Set<Muscle> getMuscles() {
        return new HashSet<Muscle>(); // stub
    }

    // EFFECTS: Return the name of this MuscleGroup
    public String getName() {
        return ""; // stub
    }

    // EFFECTS: Return a map of cumulative metrics by cumulating individual muscle metrics data 
    //          from each Muscle in this MuscleGroup. If this group has no muscles, return an empty map
    public Map<String, Double> getGroupMetrics() {
        return new HashMap<String, Double>(); // stub
    }
}
