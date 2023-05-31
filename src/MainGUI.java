import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import javax.imageio.ImageIO;

public class MainGUI extends JFrame {
    private BufferedImage backgroundImage;
    private boolean gameStarted;
    private Player player;

    public MainGUI() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int guiWidth = screenHeight - 100;
        int guiHeight = screenHeight - 100;
        int x = (screenWidth - guiWidth) / 2;
        int y = (screenHeight - guiHeight) / 2;

        setSize(guiWidth, guiHeight);
        setLocation(x, y);
        setTitle("Shooting Game");
        try {
            URL iconUrl = new URL("https://t3.ftcdn.net/jpg/03/56/25/78/360_F_356257839_bt05Ezt9V60WnC6cSAwQqL5UJmERjJ9A.jpg");
            BufferedImage iconImage = ImageIO.read(iconUrl);
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        try {
            URL imageUrl = new URL("https://img.itch.zone/aW1nLzkzMzY1NjMucG5n/315x250%23c/Gb%2BH2t.png");
            backgroundImage = ImageIO.read(imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null && !gameStarted) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
                if (!gameStarted) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setFont(new Font("Monospaced", Font.BOLD, 50));
                    FontMetrics fm = g2d.getFontMetrics();
                    int stringWidth = fm.stringWidth("Start Game");
                    int x = (getWidth() - stringWidth) / 2;
                    int y = (getHeight() - fm.getHeight()) / 2 - fm.getAscent() + 20;
                    g2d.setColor(Color.RED);
                    g2d.drawString("Start Game", x, y);
                } else {
                    if (player != null) {
                        BufferedImage playerImage = getPlayerImage();
                        g.drawImage(playerImage, player.getX(), player.getY(), null);
                        revalidate();
                    }
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!gameStarted) {
                    gameStarted = true;
                    backgroundPanel.removeAll();
                    backgroundPanel.revalidate();
                    backgroundPanel.repaint();
                    player = new Player();
                    addKeyListener(player);
                    setFocusable(true);
                    requestFocus();
                }
            }
        });
        JLabel titleLabel = new JLabel("Shooting Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 70));
        titleLabel.setForeground(Color.RED);
        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        setContentPane(backgroundPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        revalidate();
    }

    private BufferedImage getPlayerImage() {
        try {
            return ImageIO.read(new File("sprites/Placeholder.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}