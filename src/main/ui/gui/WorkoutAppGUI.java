package ui.gui;

import model.exercise.*;
import model.workout.*;
import ui.gui.components.*;
import utility.PredefinedData;

import javax.swing.*;


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class represents the main graphical user interface for the workout planning program.
 * It provides functionality for creating and managing exercises, workouts, schedules, and viewing metrics.
 * 
 * The Core Features Summarized:
 * 
 * Exercise creation and management - 
 * Workout planning and organization -
 * Weekly schedule management - 
 * Equipment usage metric analysis -
 * Muscle group usage metric analysis -
 * Muscle usage metric analysis -
 * Exercise training split metric analysis 
 */
public class WorkoutAppGUI extends JFrame {

    private final ExerciseLibrary exerciseLibrary;
    private final WorkoutLibrary workoutLibrary;
    private final PredefinedData predefinedData;
    private final WeeklySchedule weeklySchedule;
    
    private JPanel mainPanel;
    private CardLayout cardLayout;
    
    private MainMenuPanel mainMenuPanel;
    private ExerciseCreationPanel exerciseCreationPanel;
    private ExerciseManagementPanel exerciseManagementPanel;
    private WorkoutCreationPanel workoutCreationPanel;
    private WorkoutManagementPanel workoutManagementPanel;
    private PersistencePanel persistencePanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setupLookAndFeel();
            new WorkoutAppGUI();
        });
    }

    // HELPER: for main
    // EFFECTS: Configure the application to use the native look and feel of the 
    //          operating system rather than Java's default cross-platform visual look
    private static void setupLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EFFECTS: Initialize the WorkoutAppGUI by setting up libraries, predefined data, schedule,
    //          and all UI components; Then, display the application window
    public WorkoutAppGUI() {
        this.exerciseLibrary = new ExerciseLibrary();
        this.workoutLibrary = new WorkoutLibrary();
        this.predefinedData = new PredefinedData();
        this.weeklySchedule = new WeeklySchedule();
        
        setupFrame();
        initializePanels();
        setupMainPanel();
        
        setVisible(true);
        showSplashScreen();
    }

    // HELPER: for WorkoutAppGUI
    // EFFECTS: Set up the main JFrame display properties for this application
    private void setupFrame() {
        setTitle("Workout Planning System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setupWindowListener();
    }

    // HELPER: for setupFrame
    // EFFECTS: Set up window listener for exit confirmation
    private void setupWindowListener() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                persistencePanel.promptSaveOnExit();
            }
        });
    }

    // HELPER: for WorkoutAppGUI
    // EFFECTS: Initialize all panel components for the application
    private void initializePanels() {
        initializeSharedComponents();
        createPanels();
    }

    // HELPER: for initializePanels
    // EFFECTS: Initialize shared model components used by all panels
    private void initializeSharedComponents() {
        SharedGuiComponents.initializeItems(
                exerciseLibrary, 
                workoutLibrary, 
                weeklySchedule, 
                predefinedData, 
                this
        );
    }

    // HELPER: for initializePanels
    // EFFECTS: Create all panel instances
    private void createPanels() {
        mainMenuPanel = new MainMenuPanel();
        exerciseCreationPanel = new ExerciseCreationPanel();
        exerciseManagementPanel = new ExerciseManagementPanel();
        workoutCreationPanel = new WorkoutCreationPanel();
        workoutManagementPanel = new WorkoutManagementPanel();
        persistencePanel = new PersistencePanel();
    }

    // HELPER: for WorkoutAppGUI
    // EFFECTS: Set up the main panel with card layout for navigation between different panels
    private void setupMainPanel() {
        createMainPanelWithCardLayout();
        addPanelsToMainPanel();
        showInitialPanel();
    }

    // HELPER: for setupMainPanel
    // EFFECTS: Create main panel with card layout
    private void createMainPanelWithCardLayout() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        add(mainPanel);
    }

    // HELPER: for setupMainPanel
    // EFFECTS: Add all panels to the main panel
    // NOTE: A panel is selected for user-interaction by name in the back-end (see navigateTo)
    private void addPanelsToMainPanel() {
        mainPanel.add(mainMenuPanel, "MainMenu");
        mainPanel.add(exerciseCreationPanel, "ExerciseCreation");
        mainPanel.add(exerciseManagementPanel, "ExerciseManagement");
        mainPanel.add(workoutCreationPanel, "WorkoutCreation");
        mainPanel.add(workoutManagementPanel, "WorkoutManagement");
    }

    // HELPER: for setupMainPanel
    // EFFECTS: Show the initial panel (main menu)
    private void showInitialPanel() {
        cardLayout.show(mainPanel, "MainMenu");
    }

    // HELPER: for WorkoutAppGUI
    // EFFECTS: Display a splash screen with an application logo (IMAGE) when starting
    private void showSplashScreen() {
        SplashScreenGui splashScreen = new SplashScreenGui();
        splashScreen.setVisible(true);
        
        scheduleSplashScreenClose(splashScreen);
    }

    // HELPER: for showSplashScreen
    // EFFECTS: Schedule the splash screen to close after a delay
    private void scheduleSplashScreenClose(SplashScreenGui splashScreen) {
        Timer timer = new Timer(2500, e -> {
            splashScreen.dispose();
            toFront();
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    // EFFECTS: Navigate to the specified panel by panelName
    public void navigateTo(String panelName) {
        cardLayout.show(mainPanel, panelName);
    }
}