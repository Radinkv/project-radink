package ui.gui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * This class represents a splash screen that is displayed when the application starts.
 * It shows the application logo and name to provide visual feedback during application loading.
 */
public class SplashScreenGui extends JDialog {

    // EFFECTS: Instantiate the splash screen with the application logo and name
    public SplashScreenGui() {
        setupDialog();
        setupContent();
        pack();
        centerOnScreen();
    }

    // HELPER: for SplashScreenGui
    // EFFECTS: Sets up the dialog properties for an undecorated modal window
    private void setupDialog() {
        setUndecorated(true);
        setModal(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    // HELPER: for SplashScreenGui
    // EFFECTS: Sets up the content of the splash screen with logo and title
    private void setupContent() {
        JPanel panel = createMainPanel();
        JLabel logoLabel = createLogoLabel();
        JLabel titleLabel = createTitleLabel();
        
        panel.add(logoLabel, BorderLayout.CENTER);
        panel.add(titleLabel, BorderLayout.SOUTH);
        
        add(panel);
    }

    // HELPER: for setupContent
    // EFFECTS: Creates the main panel for the splash screen with background and border
    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(SharedGuiComponents.PRIMARY_COLOR);
        panel.setBorder(BorderFactory.createLineBorder(SharedGuiComponents.ACCENT_COLOR, 2));
        panel.setPreferredSize(new Dimension(400, 300));
        return panel;
    }

    // HELPER: for setupContent
    // EFFECTS: Creates the logo label with an image or placeholder if image not found
    private JLabel createLogoLabel() {
        JLabel logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        ImageIcon icon = loadLogoImage();
        logoLabel.setIcon(icon);
        
        return logoLabel;
    }

    // HELPER: for createLogoLabel
    // EFFECTS: Loads the logo image from resources or creates a placeholder if not found
    private ImageIcon loadLogoImage() {
        try {
            // Try to load from resources
            return new ImageIcon(getClass().getResource("workout_logo.jpeg"));
        } catch (Exception e) {
            // If image not found, create a placeholder
            return createPlaceholderIcon();
        }
    }

    // HELPER: for loadLogoImage
    // EFFECTS: Creates a placeholder icon with a colored circle and letter when the logo image is not available
    private ImageIcon createPlaceholderIcon() {
        // Create a colored rectangle as placeholder
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        // Draw background circle
        // g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(SharedGuiComponents.ACCENT_COLOR);
        g2d.fillOval(0, 0, 200, 200);
        
        // Draw "W" text
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 48));
        g2d.drawString("W", 75, 120);
        
        g2d.dispose();
        
        return new ImageIcon(img);
    }

    // HELPER: for setupContent
    // EFFECTS: Creates the title label for the application name with styling
    private JLabel createTitleLabel() {
        JLabel titleLabel = new JLabel("Workout Planning System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        return titleLabel;
    }

    // HELPER: for SplashScreenGui
    // EFFECTS: Centers the dialog on the screen based on screen dimensions
    private void centerOnScreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
                (screenSize.width - getWidth()) / 2,
                (screenSize.height - getHeight()) / 2
        );
    }
}