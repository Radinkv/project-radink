package ui.components;

import static ui.components.SharedUI.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.association.ExerciseAssociator;
import model.equipment.Equipment;

/** An Equipment Metrics UI class that represents a section of the main UI, dedicated to showing the user
 * detailed statistics about their overall equipment-exercise usage variety, by equipment type, for the intent
 * of allowing users to see and improve their gym routines with a better balance.
 */
public class EquipmentMetricsUI {

    // EFFECTS: Equipment metrics viewing text/console UI that achieves the following:
    //          1. Present an overview of Equipment utilization across the entire system 
    //             (shared and stored data in PredefinedData)
    //          2. Then, allow users to browse (high-level view minute metrics) and select a
    //             specific Equipment for in-depth analysis
    //          3. When a user selects an equipment, they can see detailed metrics for that individual
    //             Equipment based on Everything ExerciseAssociator and this program supports tracking
    //          4. They can exit the metrics view and at any time. Each step of the UI handles
    //             user input and navigation within the metrics exploration system
    // REMINDER: An Equipment will have NO metrics if it is not part of any exercise, that is part of a workout,
    //           that is within WeeklySchedule. It would be considered INACTIVE by the program (thus no metrics given)
    //           if it is in that state.
    // NOTE: While loops are used for the user to be able to back-track in the UI
    public void viewEquipmentMetrics() {
        Map<String, Equipment> equipment = predefinedData.getAllEquipment();
        while (true) {
            clearScreen();
            displayEquipmentMetricsOverview(equipment); // Display high-level Equipment data
            String command = input.nextLine().trim();
            if (command.equalsIgnoreCase("b")) {
                break;
            }
            processEquipmentMetricsSelection(equipment, command); // User selects Equipment to view detailed metrics UI
        }
    }

    // HELPER: for viewEquipmentMetrics
    // REQUIRES: equipment is not null
    // EFFECTS: Prepare and display the main overview of Equipment usage metrics sorted by their utilization
    private void displayEquipmentMetricsOverview(Map<String, Equipment> equipment) {
        System.out.println("\n=== High-Level Equipment Usage Analysis ===");
        System.out.println("Weekly utilization by Equipment type:");
        System.out.println("=====================================");
        // List for sorting and user-selection ability sequentially beyond an unordered Map
        List<Map.Entry<String, Equipment>> equipmentList = 
                    new ArrayList<Map.Entry<String, Equipment>>(equipment.entrySet());
        double maxDuration = findMaxEquipmentDuration(equipmentList);
        displayEquipmentMetricsList(equipmentList, maxDuration);
        System.out.println("\nSelect Equipment for detailed analysis " + BACK_OPTION + ":");
    }

    // HELPER: for displayEquipmentMetricsOverview
    // REQUIRES: equipmentList is not null
    // EFFECTS: Calculate the maximum duration of exercise for all Equipment 
    //          to properly ratio the visual progress bar for each Equipment
    private double findMaxEquipmentDuration(List<Map.Entry<String, Equipment>> equipmentList) {
        double maxDuration = 0.0;
        for (Map.Entry<String, Equipment> entry : equipmentList) {
            Equipment equipment = entry.getValue();
            if (equipment instanceof ExerciseAssociator) {
                ExerciseAssociator exerciseAssociator = (ExerciseAssociator) equipment;
                double duration = exerciseAssociator.getAggregatedExerciseMetrics().get("totalDuration");
                maxDuration = Math.max(maxDuration, duration);
            }
        }
        return maxDuration;
    }
    
    // HELPER: for displayEquipmentMetricsOverview
    // REQUIRES: equipmentList is not null
    // EFFECTS: Iterate through Equipment list and display metrics for each equipment
    private void displayEquipmentMetricsList(List<Map.Entry<String, Equipment>> equipmentList,
            double maxDuration) {
        for (int i = 0; i < equipmentList.size(); i++) {
            Map.Entry<String, Equipment> entry = equipmentList.get(i);
            displayEquipmentMetricsEntry(i, entry, maxDuration);
        }
    }

    // HELPER: for displayEquipmentMetricsList
    // REQUIRES: entry is not null and entry.getValue() is an instance of ExerciseAssociator
    // EFFECTS: Format and display a single Equipment entry with its usage metrics 
    //          and a visual progress bar representing its total weekly time utilization
    private void displayEquipmentMetricsEntry(int index, Map.Entry<String, Equipment> entry,
            double maxDuration) {
        Equipment eq = entry.getValue();
        Map<String, Double> metrics = ((ExerciseAssociator) eq).getAggregatedExerciseMetrics();
        double totalSeconds = metrics.get("totalDuration");
        System.out.printf("[%d] %-15s: %s (%s/week)\n", 
                index, 
                truncateString(entry.getKey(), 15),
                generateProgressBar(totalSeconds, maxDuration),
                formatDuration(Math.round(totalSeconds))
        );
    }

    // HELPER: for viewEquipmentMetrics
    // REQUIRES: equipment is not null, command is not null
    // EFFECTS: Process user's selection of Equipment for detailed metrics view,
    //          make sure input is valid (retry user if not) and then display the
    //          detailed metrics for that exercise
    private void processEquipmentMetricsSelection(Map<String, Equipment> equipment, String command) {
        try {
            int selection = Integer.parseInt(command);
            List<Equipment> equipmentList = new ArrayList<Equipment>(equipment.values());
            if (selection >= 0 && selection < equipmentList.size()) { // Selection is within the expected range
                displayDetailedEquipmentMetrics(equipmentList.get(selection));
            } else {
                System.out.println(INVALID_INPUT);
                waitForEnter();
            }
        } catch (NumberFormatException e) {
            System.out.println(INVALID_INPUT);
            waitForEnter();
        }
    }

    // HELPER: for processEquipmentMetricsSelection
    // REQUIRES: equipment is not null and is an instance of ExerciseAssociator
    // EFFECTS: Display metrics for a specific piece of equipment
    private void displayDetailedEquipmentMetrics(Equipment equipment) {
        Map<String, Double> metrics = ((ExerciseAssociator) equipment).getAggregatedExerciseMetrics();
        displayEquipmentHeader(equipment);
        if (metrics.get("totalDuration") > 0) {
            displayEquipmentTimeMetrics(metrics);
            displayEquipmentTrainingDistribution(metrics);
        }      
        // Show strength section if and only if there are strength metrics
        if (equipment.isWeightBased()  
                && (metrics.get("totalSets") > 0  
                || metrics.get("totalReps") > 0)) {
            displayStrengthMetrics(metrics);
        }
        displayEquipmentCoverage(equipment, metrics);
        waitForEnter();
    }

    // HELPER: for displayDetailedEquipmentMetrics
    // REQUIRES: equipment is not null
    // EFFECTS: Display information header for the selected equipment
    private void displayEquipmentHeader(Equipment equipment) {
        System.out.println("\n=== Detailed Equipment Analysis: " + equipment.getEquipmentName() + " ===");
        System.out.println("Type: " + equipment.getEquipmentType());
        System.out.println("=========================================");
    }

    // HELPER: for displayDetailedEquipmentMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Display time-based metrics including total training time and rest time
    private void displayEquipmentTimeMetrics(Map<String, Double> metrics) {
        double totalDuration = metrics.get("totalDuration");
        double restTime = metrics.get("totalRestTimeBetween");
        if (totalDuration > 0 || restTime > 0) {
            System.out.println("\nTraining Time Distribution:");
            if (totalDuration > 0) {
                System.out.println("Total Time: " + formatDuration(Math.round(totalDuration)));
            }
            if (restTime > 0) {
                System.out.println("Total Rest Time: " + formatDuration(Math.round(restTime)));
            }
        }
    }

    // HELPER: for displayDetailedEquipmentMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Calculate and display training type distribution for the selected equipment
    private void displayEquipmentTrainingDistribution(Map<String, Double> metrics) {
        Map<String, Double> typeDistribution = calculateTrainingDistribution(metrics);
        displayTrainingDistribution(typeDistribution, metrics.get("totalDuration"));
    }

    // HELPER: for displayDetailedEquipmentMetrics
    // REQUIRES: metrics is not null
    // EFFECTS: Display strength training specific metrics for weight-based equipment
    private void displayStrengthMetrics(Map<String, Double> metrics) {
        System.out.println("\nStrength Training Metrics:");
        System.out.printf("Total Sets: %.0f\n", metrics.get("totalSets"));
        System.out.printf("Total Reps: %.0f\n", metrics.get("totalReps"));
        double avgReps = metrics.get("totalReps") / Math.max(1, metrics.get("totalSets"));
        System.out.printf("Average Reps per Set: %.1f\n", avgReps);
    }

    // HELPER: for displayDetailedEquipmentMetrics
    // REQUIRES: equipment is not null and metrics is not null
    // EFFECTS: Display exercise coverage metrics showing associated exercises 
    //          and average time per exercise
    private void displayEquipmentCoverage(Equipment equipment, Map<String, Double> metrics) {
        System.out.println("\nExercise Coverage:");
        int exerciseCount = ((ExerciseAssociator) equipment).getNumAssociatedExercises();
        System.out.printf("Associated Exercises: %d\n", exerciseCount);
        if (exerciseCount > 0) {
            double avgTime = metrics.get("totalDuration") / exerciseCount;
            System.out.printf("Average Time per Exercise: %s\n", 
                    formatDuration(Math.round(avgTime))
            );
        }
    }
}
