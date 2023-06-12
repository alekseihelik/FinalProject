import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class MainGUI extends JFrame {
    private Timer timer;

    public MainGUI() throws IOException {
        setResizable(false);
        setScreenSize();
        gameIcon();
        setTitle("Bullet Hell");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GamePanel backgroundPanel = new GamePanel(this.getWidth());
        setContentPane(backgroundPanel);
        setVisible(true);
        backgroundPanel.startGameLoop();
        timer = new Timer(1000, new TimerListener());
        timer.start();
    }

    private void gameIcon() {
        try {
            BufferedImage iconImage = ImageIO.read(new File("sprites/Bullet.png"));
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

    private static class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }
}
