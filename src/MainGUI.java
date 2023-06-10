import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class MainGUI extends JFrame {
    private Player player;

    public MainGUI() throws IOException {
        setScreenSize();
        gameIcon();
        setTitle("Bullet Hell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel backgroundPanel = new GamePanel();
        setContentPane(backgroundPanel);
        setVisible(true);
        backgroundPanel.startGameLoop();
    }

    private void gameIcon() {
        try {
            URL iconUrl = new URL("https://t3.ftcdn.net/jpg/03/56/25/78/360_F_356257839_bt05Ezt9V60WnC6cSAwQqL5UJmERjJ9A.jpg");
            BufferedImage iconImage = ImageIO.read(iconUrl);
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScreenSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setSize(screenHeight - 100, screenHeight - 50);
        setLocation((screenWidth - (screenHeight - 100)) / 2, ((screenHeight - (screenHeight - 100)) / 2) - 50);
    }
}