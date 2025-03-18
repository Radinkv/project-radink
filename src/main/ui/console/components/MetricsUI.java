package ui.console.components;

import static ui.console.components.SharedUI.*;

/** This UI component manages metric viewing navigation based on Equipment or Muscle for the user.  */
public class MetricsUI {
    EquipmentMetricsUI equipmentMetricsUI;
    MuscleMetricsUI muscleMetricsUI;

    public MetricsUI() {
        this.equipmentMetricsUI = new EquipmentMetricsUI();
        this.muscleMetricsUI = new MuscleMetricsUI();
    }

    // EFFECTS: Display the metrics analysis menu and process user commands
    //          Present options to view equipment usage analysis, muscle group analysis, 
    //          or return to the main menu 
    //          Remain on this UI page until the user chooses to exit
    public void viewMetrics() {
        while (true) {
            clearScreen();
            displayMetricsMenu();
            String command = input.nextLine().trim();
            if (!processMetricsCommand(command)) {
                break;
            }
        }
    }

    // HELPER: for viewMetrics
    // EFFECTS: Display the metrics analysis menu
    private void displayMetricsMenu() {
        System.out.println("\n=== Workout Analytics Center ===");
        System.out.println("Select analysis category:");
        System.out.println("[1] Equipment Usage Analysis");
        System.out.println("[2] Muscle Group Analysis");
        System.out.println("[3] Return to Main Menu");
    }

    // HELPER: for viewMetrics
    // REQUIRES: command is not null
    // EFFECTS: Process metrics menu commands, return false if the user chooses to exit
    private boolean processMetricsCommand(String command) {
        switch (command) {
            case "1":
                equipmentMetricsUI.viewEquipmentMetrics();
                return true;
            case "2":
                muscleMetricsUI.viewMuscleMetrics();
                return true;
            case "3":
                return false;
            default:
                System.out.println("Invalid selection.");
                waitForEnter();
                return true;
        }
    }
}
