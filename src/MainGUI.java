import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.text.AttributedCharacterIterator;
import javax.imageio.ImageIO;

public class MainGUI extends JFrame {
    private BufferedImage backgroundImage;
    public MainGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(850,screenSize.height-100);
        setTitle("Shooting Game");
        try {
            URL iconUrl = new URL("https://t3.ftcdn.net/jpg/03/56/25/78/360_F_356257839_bt05Ezt9V60WnC6cSAwQqL5UJmERjJ9A.jpg"); // Replace with the actual URL of the icon image
            BufferedImage iconImage = ImageIO.read(iconUrl);
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        try {
            URL imageUrl = new URL("https://opengameart.org/sites/default/files/preview-trees-and-bushesx2.png"); // Replace with the actual image URL
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        JLabel titleLabel = new JLabel("Shooting Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 70));
        titleLabel.setForeground(Color.RED);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        setContentPane(backgroundPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        backgroundPanel.add(startButton(), BorderLayout.CENTER);
        backgroundPanel.add(settingsButton(), BorderLayout.SOUTH);
        setVisible(true);
        revalidate();
    }

    private JButton startButton(){
        JButton start = new JButton("Start Game");
        start.setFocusable(false);
        start.setOpaque(false);
        start.setContentAreaFilled(false);
        start.setBorderPainted(false);
        start.setSize(100,100);
        start.setForeground(Color.yellow);
        start.setFont(new Font("Monospaced", Font.PLAIN, 50));
        return start;
    }

    private JButton settingsButton() {
        JButton settings = new JButton("Settings");
        settings.setFocusable(false);
        settings.setOpaque(false);
        settings.setContentAreaFilled(false);
        settings.setBorderPainted(false);
        settings.setSize(100, 100);
        settings.setForeground(Color.blue);
        settings.setFont(new Font("Monospaced", Font.PLAIN, 50));
        return settings;
    }
}