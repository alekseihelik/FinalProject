import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class MainGUI extends JFrame {

    private BufferedImage backgroundImage;

    public MainGUI() {
        // Set the size to full screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height);

        // Set the title
        setTitle("Shooting Game");

        // Set the icon image
        try {
            URL iconUrl = new URL("https://t3.ftcdn.net/jpg/03/56/25/78/360_F_356257839_bt05Ezt9V60WnC6cSAwQqL5UJmERjJ9A.jpg"); // Replace with the actual URL of the icon image
            BufferedImage iconImage = ImageIO.read(iconUrl);
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the layout
        setLayout(new BorderLayout());

        // Load the background image
        try {
            URL imageUrl = new URL("https://img.freepik.com/premium-vector/space-sky-pixel-art-vector-background_612779-86.jpg"); // Replace with the actual image URL
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a custom JPanel for drawing the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());

        // Create the label for "Shooting Game" at the center top
        JLabel titleLabel = new JLabel("Shooting Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 30));
        titleLabel.setForeground(Color.RED); // Set the title color to red
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);

        // Set the custom JPanel as the content pane
        setContentPane(backgroundPanel);

        // Handle window close event
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}