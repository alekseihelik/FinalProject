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
	private Image backgroundImage2;
	private boolean finalArea;
	private boolean finalAreaLoaded;
	private int backgroundY;
	private long lastEnemySpawnedTime;
	private final long ENEMY_SPAWN_DELAY = 500;
	
	private int wave = 1;
	private final long WAVE_DELAY = 500;
	private long waveSpawnTime;
	
	private int enemiesPerWave = 10;
	private int enemyToSpawn = enemiesPerWave;

	public static int screenWidth;
	private int highscore;
	
	public GamePanel(int width) throws IOException {
		finalArea=false;
		finalAreaLoaded=false;
		player = new Player(this);
		this.addKeyListener(player);
		this.setFocusable(true);
		backgroundY = 0;
		backgroundImage = ImageIO.read(new File("backgrounds/MainArea.jpg"));
		backgroundImage2 = ImageIO.read(new File("backgrounds/FinalArea.jpg"));
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
				if(!finalAreaLoaded) {
					backgroundY += 1;
					if (backgroundY >= getHeight()) {
						backgroundY = 0;
					}
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
		if (wave <= 9){
			g2d.drawImage(backgroundImage, 0, backgroundY, getWidth(), getHeight(), null);
		    g2d.drawImage(backgroundImage, 0, backgroundY - getHeight(), getWidth(), getHeight(), null);
	    }
		else if(wave>9 && !finalArea){
			g2d.drawImage(backgroundImage2, 0, 0, getWidth(), getHeight(), null);
			finalAreaLoaded=true;
		}
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
		String text3 = "Power: " + player.getPower();
		String text4 = "Lives: " + player.getLives();
		g.drawString(text, (g.getClipBounds().width - g.getFontMetrics().stringWidth(text))-10, g.getClipBounds().height - g.getFontMetrics().getHeight());
		g.drawString(text2, (g.getClipBounds().width - g.getFontMetrics().stringWidth(text2))-10, (g.getClipBounds().height - g.getFontMetrics().getHeight())-30);
		g.drawString(text3,(g.getClipBounds().width - g.getFontMetrics().stringWidth(text3))-10,(g.getClipBounds().height - g.getFontMetrics().getHeight())-60);
		g.drawString(text4,(g.getClipBounds().width - g.getFontMetrics().stringWidth(text4))-10,(g.getClipBounds().height - g.getFontMetrics().getHeight())-90);
		if (enemyToSpawn > 0 && System.currentTimeMillis() >= waveSpawnTime && System.currentTimeMillis() >= lastEnemySpawnedTime + ENEMY_SPAWN_DELAY) {
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
			try {
				enemy.update();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (enemy.isAlive()) {
				enemy.draw(g2d);
			} else {
				enemies.remove(i);
				i--;
			}
		}
		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).update();
			if(bullets.get(i).isAlive()){
				if(player.getPower()<15) {
					bullets.get(i).draw(g2d);
				}
				else if(player.getPower()<30){
					bullets.get(i).draw2(g2d);
				}
				else if(player.getPower()<45){
					bullets.get(i).draw3(g2d);
				}
				else{
					bullets.get(i).draw4(g2d);
				}
			}
			else{
				bullets.remove(i);
				i--;
			}
		}
		if (enemies.size() == 0 && enemyToSpawn == 0) {
			wave++;
			waveSpawnTime = System.currentTimeMillis() + WAVE_DELAY;
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
					if (enemy.getHitsToDie() > 1) {
						enemy.decrementHit();
					} else {
						int powerChance = (int) (Math.random() * 3) + 1;
						if(powerChance == 1){
							player.increasePower();
						}
						enemy.setAlive(false);
						player.increaseScore(5);
						enemies.remove(j);
						j--;
					}
					bullet.setAlive(false);
					break;
				}
			}
			if (!bullet.isAlive()) {
				bullets.remove(i);
				i--;
			}
		}
		for (int i = 0; i < Enemy.enemyBullets.size(); i++) {
			EnemyBullet bullet = Enemy.enemyBullets.get(i);
			Rectangle bulletHitbox = bullet.getHitbox();
			bullet.update();
			if (bullet.isAlive()) {
				bullet.draw(g2d);
				if (bulletHitbox.intersects(player.getHitbox())) {
					if (player.getLives() > 1) {
						player.decrementLives();
					} else {
						player.die();
					}
					bullet.setAlive(false);
					break;
				}
				if (!bullet.isAlive()) {
					Enemy.enemyBullets.remove(i);
					i--;
				}
			} else {
				Enemy.enemyBullets.remove(i);
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
}