import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private Player player;
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	private Image backgroundImage;
	private int backgroundY;

	public GamePanel() throws IOException {
		player = new Player(this);
		this.addKeyListener(player);
		this.setFocusable(true);
		backgroundY = 0;
		backgroundImage = ImageIO.read(new File("backgrounds/top-down placeholder.jpg")); // Replace with your background image path
	}

	public void startGameLoop() {
		Thread gameLoop = new Thread(() -> {
			long desiredFrameRate = 60;
			long frameTime = 1000 / desiredFrameRate;
			long lastUpdateTime = System.currentTimeMillis();

			while (true) {
				long currentTime = System.currentTimeMillis();
				long elapsedTime = currentTime - lastUpdateTime;

				if (elapsedTime >= frameTime) {
					lastUpdateTime = currentTime;
					this.repaint();
				}

				backgroundY += 1; // Adjust the scrolling speed as desired
				if (backgroundY >= getHeight()) {
					backgroundY = 0;
				}

				try {
					Thread.sleep(1); // Small delay to avoid excessive CPU usage
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		gameLoop.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(backgroundImage, 0, backgroundY, getWidth(), getHeight(), null);
		g2d.drawImage(backgroundImage, 0, backgroundY - getHeight(), getWidth(), getHeight(), null);
		player.update();
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).isAlive()){
				bullets.get(i).draw(g2d);
			}
			else{
				bullets.remove(i);
			}
		}
		player.draw(g2d);
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;
		return new Dimension(screenHeight - 100, screenHeight - 50);
	}
}