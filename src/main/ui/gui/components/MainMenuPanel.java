package ui.gui.components;

import javax.swing.*;

import ui.gui.WorkoutAppGUI;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * This panel represents the main menu of the workout planning application.
 * It provides navigation buttons to access all main features of the application.
 */
public class MainMenuPanel extends JPanel {

    // EFFECTS: Construct this MainMenuPanel with title and navigation buttons
    //          Set up the layout, create title and button components
    public MainMenuPanel() {
        setupPanel();
        
        JLabel titleLabel = createTitleLabel();
        JPanel buttonPanel = createButtonPanel();
        
        add(titleLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
    }

    // HELPER: for MainMenuPanel
    // EFFECTS: Configure the panel with BorderLayout and set background color
    //          Prepare the panel's basic display properties
    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(SharedGuiComponents.PRIMARY_COLOR);
    }

    // HELPER: for MainMenuPanel
    // EFFECTS: Create a centered title label with padding and return the label
    //          Return a styled JLabel with the application title
    private JLabel createTitleLabel() {
        JLabel titleLabel = SharedGuiComponents.createTitleLabel("Workout Planning System");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // HELPER: for MainMenuPanel
    // EFFECTS: Create and return a panel containing navigation buttons arranged vertically
    //          Return a JPanel with buttons for each application feature
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        
        GridBagConstraints gbc = createButtonConstraints();
        addMenuButtons(panel, gbc);
        
        return panel;
    }

    // HELPER: for createButtonPanel
    // EFFECTS: Return GridBagConstraints configured for vertical button arrangement
    //          Set up layout constraints for consistent button display
    private GridBagConstraints createButtonConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 50, 10, 50);
        return gbc;
    }

    // HELPER: for createButtonPanel
    // EFFECTS: Create navigation buttons with labels and destinations and add them to the panel
    //          Add all feature navigation buttons to the specified panel
    private void addMenuButtons(JPanel panel, GridBagConstraints gbc) {
        String[] buttonLabels = getButtonLabels();
        String[] destinations = getDestinations();
        
        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createMenuButton(buttonLabels[i], destinations[i]);
            panel.add(button, gbc);
        }
    }

    // HELPER: for addMenuButtons
    // EFFECTS: Return an array of strings containing the text for each navigation button
    //          Provide labels for all main menu options in the application
    private String[] getButtonLabels() {
        return new String[] {
            "Create Exercise", 
            "Manage Exercises", 
            "Create Workout", 
            "Manage Workouts", 
            "Save Program State", 
            "Load Program State", 
            "Exit"
        };
    }

    // HELPER: for addMenuButtons
    // EFFECTS: Return an array of strings representing the navigation destinations for each button
    //          Provide panel names or actions corresponding to each button
    private String[] getDestinations() {
        return new String[] {
            "ExerciseCreation", 
            "ExerciseManagement", 
            "WorkoutCreation", 
            "WorkoutManagement", 
            "Save", 
            "Load", 
            "Exit"
        };
    }

    // HELPER: for addMenuButtons
    // EFFECTS: Create and return a styled button with the given label and action listener
    //          Configure button appearance and attach appropriate navigation behavior
    private JButton createMenuButton(String label, String destination) {
        JButton button = SharedGuiComponents.createStyledButton(label);
        button.setPreferredSize(new Dimension(300, 40));
        
        button.addActionListener(createButtonActionListener(destination));
        
        return button;
    }

    // HELPER: for createMenuButton
    // EFFECTS: Return an ActionListener that either navigates to a panel, performs a save/load operation,
    //          or exits the application based on the specified destination
    //          Map button clicks to appropriate application actions
    private ActionListener createButtonActionListener(String destination) {
        return e -> {
            if (destination.equals("Exit")) {
                System.exit(0); // Exit without saving
            } else if (destination.equals("Save")) {
                new PersistencePanel().saveState();
            } else if (destination.equals("Load")) {
                new PersistencePanel().loadState();
            } else {
                ((WorkoutAppGUI) SwingUtilities.getWindowAncestor(this)).navigateTo(destination);
            }
        };
    }
}