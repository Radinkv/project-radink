package ui.console.components;

import static ui.console.components.SharedUI.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.muscle.Muscle;
import model.muscle.MuscleGroup;

/** A Muscle Metrics UI class that represents a section of the main UI, dedicated to showing the user
 * detailed statistics about their overall muscle-exercise usage variety, by muscle group and type, 
 * for the intent of allowing users to see and improve their gym routines with a better balance.
 */
public class MuscleMetricsUI {

    // EFFECTS: Muscle metrics viewing text/console UI that achieves the following:
    //          1. Present an overview of Muscle utilization across the entire system 
    //             (shared and stored data in PredefinedData)
    //          2. Then, allow users to browse (high-level view minute metrics) and select a
    //             specific Muscle for in-depth analysis
    //          3. When a user selects a Muscle, they can see detailed metrics for that individual
    //             Muscle based on Everything ExerciseAssociator and this program supports tracking
    //          4. They can exit the metrics view at any time. Each step of the UI handles
    //             user input and navigation within the metrics exploration system
    // REMINDER: A Muscle will have NO metrics if it is not part of any Exercise, that is part of a Workout,
    //           that is within WeeklySchedule. It would be considered INACTIVE by the program (thus no metrics given)
    //           if it is in that state.
    // NOTES: These methods mirror those of the EquipmentMetricsUI, but note that MuscleGroup is one level higher
    //        than Muscle, so additional abstraction is applied.
    //        While loops are used for the user to be able to back-track in the UI
    public void viewMuscleMetrics() {
        Map<String, MuscleGroup> muscleGroups = predefinedData.getAllMuscleGroups();
        while (true) {
            clearScreen();
            displayMuscleMetricsOverview(muscleGroups); // Display high-level MuscleGroup usage data
            String command = input.nextLine().trim();

            if (command.equalsIgnoreCase("b")) {
                break;
            }
            processMuscleGroupSelection(muscleGroups, command); // User selects a muscle group for detailed analysis
        }
    }

    // HELPER: for viewMuscleMetrics
    // REQUIRES: muscleGroups is not null
    // EFFECTS: Display overview of muscle group metrics
    private void displayMuscleMetricsOverview(Map<String, MuscleGroup> muscleGroups) {
        System.out.println("\n=== Muscle Group Analysis ===");
        System.out.println("Weekly training distribution by muscle group:");
        System.out.println("=========================================");
        // List for sorting and user-selection ability sequentially beyond an unordered Map
        List<Map.Entry<String, MuscleGroup>> groupList = 
                new ArrayList<Map.Entry<String, MuscleGroup>>(muscleGroups.entrySet());
        double maxDuration = findMaxMuscleGroupDuration(groupList);
        displayMuscleGroupList(groupList, maxDuration);
        System.out.println("\nSelect muscle group for detailed analysis " + BACK_OPTION + ":");
    }

    // HELPER: for displayMuscleMetricsOverview
    // REQUIRES: groupList is not null
    // EFFECTS: Find maximum duration across all muscle groups using a for-loop
    private double findMaxMuscleGroupDuration(List<Map.Entry<String, MuscleGroup>> groupList) {
        double max = 0.0;
        for (Map.Entry<String, MuscleGroup> entry : groupList) {
            double duration = entry.getValue().getGroupMetrics().get("totalDuration");
            if (duration > max) {
                max = duration;
            }
        }
        return max;
    }

    // HELPER: for displayMuscleMetricsOverview
    // REQUIRES: groupList is not null
    // EFFECTS: Display list of muscle groups with metrics
    private void displayMuscleGroupList(List<Map.Entry<String, MuscleGroup>> groupList, double maxDuration) {
        for (int i = 0; i < groupList.size(); i++) {
            Map.Entry<String, MuscleGroup> entry = groupList.get(i);
            displayMuscleGroupEntry(i, entry, maxDuration);
        }
    }

    // HELPER: for displayMuscleGroupList
    // REQUIRES: entry is not null and entry.getValue() is not null
    // EFFECTS: Display metrics for a single muscle group entry
    private void displayMuscleGroupEntry(int index, Map.Entry<String, MuscleGroup> entry, double maxDuration) {
        MuscleGroup group = entry.getValue();
        Map<String, Double> metrics = group.getGroupMetrics();
        double totalSeconds = metrics.get("totalDuration");
        
        System.out.printf("[%d] %-15s: %s (%s/week)\n", 
                index, 
                truncateString(entry.getKey(), 15),
                generateProgressBar(totalSeconds, maxDuration),
                formatDuration(Math.round(totalSeconds))
        );
    }

    // HELPER: for viewMuscleMetrics
    // REQUIRES: muscleGroups is not null, command is not null
    // EFFECTS: Process muscle group selection for detailed view
    private void processMuscleGroupSelection(Map<String, MuscleGroup> muscleGroups, String command) {
        try {
            int selection = Integer.parseInt(command);
            List<MuscleGroup> groupList = new ArrayList<MuscleGroup>(muscleGroups.values());
            
            if (selection >= 0 && selection < groupList.size()) {
                displayMuscleGroupAnalysis(groupList.get(selection)); // User selected valid possible selection
            } else {
                System.out.println(INVALID_INPUT);
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for processMuscleGroupSelection
    // REQUIRES: group is not null
    // EFFECTS: Display detailed analysis for a muscle group
    private void displayMuscleGroupAnalysis(MuscleGroup group) {
        while (true) {
            displayMuscleGroupDetails(group);
            String command = input.nextLine().trim();
            if (command.equalsIgnoreCase("b")) {
                break;
            }
            processMuscleSelection(group, command);
        }
    }

    // HELPER: for displayMuscleGroupAnalysis
    // REQUIRES: group is not null
    // EFFECTS: Display metrics/analysis for a muscle group
    private void displayMuscleGroupDetails(MuscleGroup group) {
        System.out.println("\n=== Analysis: " + group.getName() + " ===");
        System.out.println("==========================================");
        
        Map<String, Double> groupMetrics = group.getGroupMetrics();
        displayGroupOverview(groupMetrics);
        displayMuscleList(group);
    }

    // HELPER: for displayMuscleGroupDetails
    // REQUIRES: groupMetrics is not null
    // EFFECTS: Display overview metrics for a muscle group
    private void displayGroupOverview(Map<String, Double> groupMetrics) {
        double totalDuration = groupMetrics.get("totalDuration");
        
        System.out.println("\nOverall Training Volume:");
        System.out.println("Total Weekly Training Time: " + formatDuration(Math.round(totalDuration)));
        
        displayTrainingTypeDistribution(groupMetrics);
    }

    // HELPER: for displayGroupOverview
    // REQUIRES: metrics is not null
    // EFFECTS: Display training type distribution for a muscle group
    private void displayTrainingTypeDistribution(Map<String, Double> metrics) {
        System.out.println("\nTraining Type Distribution:");
        Map<String, Double> distribution = calculateTrainingDistribution(metrics);
        // Here, use the value from metrics; since keys are guaranteed by ExerciseAssociator, call get("totalDuration")
        displayTrainingDistribution(distribution, metrics.get("totalDuration"));
    }

    // HELPER: for displayMuscleGroupDetails
    // REQUIRES: group is not null
    // EFFECTS: Display list of individual muscles in a group
    private void displayMuscleList(MuscleGroup group) {
        System.out.println("\nMuscle-Specific Training:");
        List<Muscle> muscles = new ArrayList<Muscle>(group.getMuscles());
        double maxDuration = findMaxMuscleDuration(muscles);
        
        for (int i = 0; i < muscles.size(); i++) {
            displayMuscleEntry(i, muscles.get(i), maxDuration);
        }
    }

    // HELPER: for displayMuscleList
    // REQUIRES: muscles is not null
    // EFFECTS: Find maximum duration across individual muscles using a for-loop
    private double findMaxMuscleDuration(List<Muscle> muscles) {
        double max = 0.0;
        for (Muscle m : muscles) {
            double duration = m.getAggregatedExerciseMetrics().get("totalDuration");
            if (duration > max) {
                max = duration;
            }
        }
        return max;
    }

    // HELPER: for displayMuscleList
    // REQUIRES: muscle is not null
    // EFFECTS: Display metrics for a single muscle
    private void displayMuscleEntry(int index, Muscle muscle, double maxDuration) {
        Map<String, Double> metrics = muscle.getAggregatedExerciseMetrics();
        double duration = metrics.get("totalDuration");
        
        System.out.printf("[%d] %-15s: %s (%s/week)\n", 
                index, 
                truncateString(muscle.getName(), 15),
                generateProgressBar(duration, maxDuration),
                formatDuration(Math.round(duration))
        );
    }

    // HELPER: for displayMuscleGroupAnalysis
    // REQUIRES: group is not null, command is not null
    // EFFECTS: Read the user's numerical choice for selecting/appointing to a muscle from the group
    //          If the number is valid, show detailed information for that muscle
    //          If not ask user to try again
    private void processMuscleSelection(MuscleGroup group, String command) {
        try {
            int selection = Integer.parseInt(command);
            List<Muscle> muscles = new ArrayList<Muscle>(group.getMuscles());
            
            if (selection >= 0 && selection < muscles.size()) {
                displayDetailedMuscleMetrics(muscles.get(selection));
            } else {
                System.out.println("Invalid selection.");
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for processMuscleSelection
    // REQUIRES: muscle is not null
    // EFFECTS: Display detailed metrics for an individual muscle
    private void displayDetailedMuscleMetrics(Muscle muscle) {
        Map<String, Double> metrics = muscle.getAggregatedExerciseMetrics();
        displayMuscleHeader(muscle);
        if (metrics.get("totalDuration") > 0) {
            displayMuscleTrainingVolume(metrics);
            displayMuscleTrainingDistribution(metrics);
        }
        if (metrics.get("totalSets") > 0 || metrics.get("totalReps") > 0) {
            displayMuscleStrengthMetrics(metrics);
        }
        displayMuscleCoverage(muscle, metrics);
        waitForEnter();
    }

    // HELPER: for displayDetailedMuscleMetrics
    // REQUIRES: muscle is not null
    // EFFECTS: Display muscle header information
    private void displayMuscleHeader(Muscle muscle) {
        System.out.println("\n=== Detailed Analysis: " + muscle.getName() + " ===");
        System.out.println("==============================================");
    }

    // HELPER: for displayDetailedMuscleMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Display training volume metrics for a muscle
    private void displayMuscleTrainingVolume(Map<String, Double> metrics) {
        double totalDuration = metrics.get("totalDuration");
        if (totalDuration > 0) {
            System.out.println("\nTraining Volume:");
            System.out.println("Total Weekly Time: " + formatDuration(Math.round(totalDuration)));
        }
    }

    // HELPER: for displayDetailedMuscleMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Display training distribution for a muscle
    private void displayMuscleTrainingDistribution(Map<String, Double> metrics) {
        System.out.println("\nTraining Type Distribution:");
        Map<String, Double> distribution = calculateTrainingDistribution(metrics);
        displayTrainingDistribution(distribution, metrics.get("totalDuration"));
    }

    // HELPER: for displayDetailedMuscleMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Display strength metrics for a muscle
    private void displayMuscleStrengthMetrics(Map<String, Double> metrics) {
        System.out.println("\nStrength Training Metrics:");
        System.out.printf("Total Sets: %.0f\n", metrics.get("totalSets"));
        System.out.printf("Total Reps: %.0f\n", metrics.get("totalReps"));
        
        double avgReps = metrics.get("totalReps") / Math.max(1, metrics.get("totalSets"));
        System.out.printf("Average Reps per Set: %.1f\n", avgReps);
    }

    // HELPER: for displayDetailedMuscleMetrics
    // REQUIRES: muscle is not null and metrics is not null
    // EFFECTS: Display exercise coverage metrics for a muscle
    private void displayMuscleCoverage(Muscle muscle, Map<String, Double> metrics) {
        System.out.println("\nTraining Coverage:");
        int exerciseCount = muscle.getNumAssociatedExercises();
        System.out.printf("Targeting Exercises: %d\n", exerciseCount);
        
        if (exerciseCount > 0) {
            displayAverageMetrics(metrics, exerciseCount);
        }
    }

    // HELPER: for displayMuscleCoverage
    // REQUIRES: metrics is not null and exerciseCount > 0
    // EFFECTS: Display average metrics per exercise
    private void displayAverageMetrics(Map<String, Double> metrics, int exerciseCount) {
        double totalDuration = metrics.get("totalDuration");
        if (totalDuration > 0) {
            System.out.printf("Average Time per Exercise: %s\n", 
                    formatDuration(Math.round(totalDuration / exerciseCount))
            );
        }
        double totalSets = metrics.get("totalSets");
        if (totalSets > 0) {
            System.out.printf("Average Sets per Exercise: %.1f\n", totalSets / exerciseCount);
        }
    }
}
