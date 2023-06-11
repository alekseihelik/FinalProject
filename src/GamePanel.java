import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private Player player;
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	public static ArrayList<Enemy> enemies = new ArrayList<>();
	private Image backgroundImage;
	private int backgroundY;
	private long lastEnemySpawnedTime;
	private final long enemySpawnDelay = 500;
	
	private int wave = 1;
	private final long waveDelay = 500;
	private long waveSpawnTime;
	
	private int enemiesPerWave = 10;
	private int enemyToSpawn = enemiesPerWave;

	public static int screenWidth;
	private int currentBullet;
	private int highscore;
	
	public GamePanel(int width) throws IOException {
		player = new Player(this);
		this.addKeyListener(player);
		this.setFocusable(true);
		backgroundY = 0;
		backgroundImage = ImageIO.read(new File("backgrounds/top-down placeholder.jpg"));
		this.screenWidth = width;
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
				backgroundY += 1;
				if (backgroundY >= getHeight()) {
					backgroundY = 0;
				}
				try {
					Thread.sleep(1);
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
		g.setFont(new Font("Arial", Font.BOLD, 24));
		g.setColor(Color.red);
		String text = "Score: " + player.getScore();
		try (BufferedReader br = new BufferedReader(new FileReader("src/highscore.txt"))) {
			String line = br.readLine();
			highscore = Integer.parseInt(line);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		String text2 = "Highscore: " + highscore;
		g.drawString(text, (g.getClipBounds().width - g.getFontMetrics().stringWidth(text))-10, g.getClipBounds().height - g.getFontMetrics().getHeight());
		g.drawString(text2, (g.getClipBounds().width - g.getFontMetrics().stringWidth(text2))-10, (g.getClipBounds().height - g.getFontMetrics().getHeight())-30);
		if (enemyToSpawn > 0 && System.currentTimeMillis() >= waveSpawnTime && System.currentTimeMillis() >= lastEnemySpawnedTime + enemySpawnDelay) {
			lastEnemySpawnedTime = System.currentTimeMillis();
			enemyToSpawn--;
			try {
				if(wave == 1 || wave == 4 || wave == 7) {
					Enemy spawnedEnemy = new Enemy(0, 0, this);
					enemies.add(spawnedEnemy);
				}
				if(wave == 2 || wave == 5 || wave == 8){
					Enemy spawnedEnemy = new Enemy(1,screenWidth,this);
					enemies.add(spawnedEnemy);
				}
				if(wave == 3 || wave == 6 || wave == 9){
					Enemy spawnedEnemy = new Enemy(2, screenWidth/2, this);
					enemies.add(spawnedEnemy);
				}
			} catch (IOException e) {}
		}
		player.update();
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update();
			if (enemy.isAlive()) {
				enemy.draw(g2d);
			} else {
				enemies.remove(i);
				i--;
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			currentBullet=i;
			bullets.get(i).update();
			if(bullets.get(i).isAlive()){
				bullets.get(i).draw(g2d);
			}
			else{
				bullets.remove(i);
				i--;
			}
			currentBullet = i;
		}
		if (enemies.size() == 0 && enemyToSpawn == 0) {
			wave++;
			waveSpawnTime = System.currentTimeMillis() + waveDelay;
			if(wave<=3) {
				enemyToSpawn = enemiesPerWave;
			}
			else if(wave<=6){
				enemyToSpawn = enemiesPerWave + 10;
			}
			else if(wave<=9){
				enemyToSpawn = enemiesPerWave + 20;
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
			Rectangle bulletHitbox = bullet.getHitbox();
			for (int j = 0; j < enemies.size(); j++) {
				Enemy enemy = enemies.get(j);
				Rectangle enemyHitbox = enemy.getHitbox();
				if (bulletHitbox.intersects(enemyHitbox)) {
					bullet.setAlive(false);
					enemy.setAlive(false);
					player.increaseScore(5);
					enemies.remove(enemy);
					j--;
				}
			}
			if (!bullet.isAlive()) {
				bullets.remove(bullet);
				i--;
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

	public static ArrayList<Bullet> getBullets() {
		return bullets;
	}

	public static ArrayList<Enemy> getEnemies() {
		return enemies;
	}

	public int getCurrentBullet() {
		return currentBullet;
	}

	public void setCurrentBullet(int currentBullet) {
		this.currentBullet = currentBullet;
	}
}