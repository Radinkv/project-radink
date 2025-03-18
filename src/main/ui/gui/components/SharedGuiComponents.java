package ui.gui.components;

import model.exercise.ExerciseLibrary;
import model.workout.WeeklySchedule;
import model.workout.WorkoutLibrary;
import utility.PredefinedData;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains static, shared helper methods and fields that different GUI components collectively use.
 * Its purpose is to minimize redundant code, as it acts as a miscellaneous library which different sections
 * of UI can point to in order to simplify their tasks in any way, shape, or form.
 * 
 * REMINDER, THIS IS NOT A UI CLASS. HOWEVER, EACH UI CLASS HAS ACCESS TO EACH OF THESE FIELDS AND METHODS!
 */
public class SharedGuiComponents {
    protected static ExerciseLibrary exerciseLibrary;
    protected static WorkoutLibrary workoutLibrary;
    protected static WeeklySchedule weeklySchedule;
    protected static PredefinedData predefinedData;
    protected static JFrame mainFrame;
    
    protected static final Color PRIMARY_COLOR = new Color(60, 63, 65);
    protected static final Color SECONDARY_COLOR = new Color(43, 43, 43);
    protected static final Color ACCENT_COLOR = new Color(104, 151, 187);
    protected static final Color TEXT_COLOR = new Color(220, 220, 220);
    
    protected static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 18);
    protected static final Font REGULAR_FONT = new Font("Arial", Font.PLAIN, 14);
    
    // Helper class for weekly schedule statistics
    protected static class WeeklyStats {
        int totalWorkouts = 0;
        int restDays = 0;
        double totalDuration = 0;
    }

    // REQUIRES: NONE of the fields are null or contain null elements
    // EFFECTS: Initialize the object-pointing fields needed by multiple UI components 
    //          (called once from WorkoutAppGUI constructor)
    public static void initializeItems(ExerciseLibrary exerciseLib, 
                                      WorkoutLibrary workoutLib, 
                                      WeeklySchedule weeklySched,
                                      PredefinedData predefData,
                                      JFrame frame) {
        exerciseLibrary = exerciseLib;
        workoutLibrary = workoutLib;
        weeklySchedule = weeklySched;
        predefinedData = predefData;
        mainFrame = frame;
    }

    // EFFECTS: Create a styled button with consistent appearance
    protected static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        applyButtonStyle(button);
        return button;
    }

    // HELPER: for createStyledButton
    // EFFECTS: Apply consistent styling to a button
    private static void applyButtonStyle(JButton button) {
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }

    // EFFECTS: Create a styled panel with consistent appearance
    protected static JPanel createStyledPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(PRIMARY_COLOR);
        return panel;
    }

    // EFFECTS: Create a styled label with consistent appearance
    protected static JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        applyLabelStyle(label, REGULAR_FONT);
        return label;
    }

    // EFFECTS: Create a styled title label with consistent appearance
    protected static JLabel createTitleLabel(String text) {
        JLabel label = new JLabel(text);
        applyLabelStyle(label, TITLE_FONT);
        return label;
    }

    // HELPER: for createStyledLabel, createTitleLabel
    // EFFECTS: Apply consistent styling to a label
    private static void applyLabelStyle(JLabel label, Font font) {
        label.setForeground(TEXT_COLOR);
        label.setFont(font);
    }

    // EFFECTS: Format duration, given in seconds to a UI displayable string
    protected static String formatDuration(long seconds) {
        if (seconds == 0) {
            return "0 seconds";
        }
        return buildDurationString(seconds);
    }

    // HELPER: for formatDuration
    // EFFECTS: Build a formatted duration string from seconds
    protected static String buildDurationString(long seconds) {
        long[] timeUnits = convertSecondsToTimeUnits(seconds);
        long days = timeUnits[0];
        long hours = timeUnits[1];
        long minutes = timeUnits[2];
        long secs = timeUnits[3];
    
        StringBuilder duration = new StringBuilder();
        appendTimeUnit(duration, days, "day");
        appendTimeUnit(duration, hours, "hour");
        appendTimeUnit(duration, minutes, "minute");
        
        if (secs > 0 && duration.length() == 0) {
            appendTimeUnit(duration, secs, "second");
        }
        
        return duration.toString().trim();
    }

    // HELPER: for buildDurationString
    // EFFECTS: Convert seconds to days, hours, minutes, and remaining seconds
    private static long[] convertSecondsToTimeUnits(long seconds) {
        long days = seconds / (24 * 3600);
        seconds = seconds % (24 * 3600);
        long hours = seconds / 3600;
        seconds = seconds % 3600;
        long minutes = seconds / 60;
        seconds = seconds % 60;
        
        return new long[]{days, hours, minutes, seconds};
    }

    // HELPER: for buildDurationString
    // EFFECTS: Append a time unit to the duration string if the duration value is above 0
    private static void appendTimeUnit(StringBuilder duration, long value, String unit) {
        if (value > 0) {
            if (duration.length() > 0) {
                duration.append(" ");
            }
            duration.append(value).append(" ").append(unit);
            if (value > 1) {
                duration.append("s");
            }
            duration.append(" ");
        }
    }

    // EFFECTS: Show an error message dialog
    protected static void showError(String message) {
        showMessageDialog(message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: Show an information message dialog
    protected static void showInfo(String message) {
        showMessageDialog(message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // HELPER: for showError, showInfo
    // EFFECTS: Display a message dialog with the specified parameters
    private static void showMessageDialog(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(mainFrame, message, title, messageType);
    }

    // EFFECTS: Show a confirmation dialog and return the user's choice
    protected static boolean showConfirmation(String message) {
        int result = JOptionPane.showConfirmDialog(mainFrame, message, "Confirm", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }
}