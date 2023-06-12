import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Enemy {

    private boolean alive;
    private boolean shot;
    private BufferedImage image1;
    private BufferedImage image2;
    private BufferedImage image3;
    private BufferedImage image4;
    private BufferedImage currentImage;
    private Rectangle hitbox;
    private int x;
    private int y;
    private int path;
    private GamePanel gp;
    public static ArrayList<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();
    private int hitsToDie = 3;
    
    private final long shootCooldown = 1000;
    private long canShootTime;

    public Enemy(int path, int x, GamePanel gp) throws IOException {
        alive = true;
        shot = false;
        BufferedImage originalImage1 = ImageIO.read(new File("sprites/EnemyWingsUp.png"));
        BufferedImage originalImage2 = ImageIO.read(new File("sprites/EnemyWingsFlat.png"));
        BufferedImage originalImage3 = ImageIO.read(new File("sprites/EnemyWingsDown.png"));
        image1 = resizeImage(originalImage1, originalImage1.getWidth()/3, originalImage1.getHeight()/3);
        image2 = resizeImage(originalImage2, originalImage2.getWidth()/3, originalImage2.getHeight()/3);
        image3 = resizeImage(originalImage3, originalImage3.getWidth()/3, originalImage3.getHeight()/3);
        image4 = resizeImage(originalImage2, originalImage2.getWidth()/3, originalImage2.getHeight()/3);
        currentImage = image1;
        hitbox = new Rectangle(x,y,currentImage.getWidth(),currentImage.getHeight());
        y=-100;
        this.x = x;
        this.gp = gp;
        this.path = path;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentImage == image1) {
                    currentImage = image2;
                }
                    else if (currentImage == image2) {
                        currentImage = image3;
                    } else if (currentImage == image3) {
                        currentImage = image4;
                    } else if (currentImage == image4) {
                        currentImage = image1;
                    }
                }
        }, 0, 50);
    }
    
    public int getHitsToDie() {
    	return hitsToDie;
    }
    
    public void decrementHit() {
    	hitsToDie--;
    }
    

    
    public void update() throws IOException {
    	if (isAlive()) {
    		shoot();
            hitbox = new Rectangle(x,y,currentImage.getWidth(),currentImage.getHeight());
    		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    		if (path == 0) {
    			x += 4;
    			y += 4;
    		}
            else if(path == 1){
                x-=4;
                y+=4;
            }
            else if(path == 2){
                y += 8;
            }
    		if (x >= gp.screenWidth + 150 || x <= currentImage.getWidth()-200 || y>= screenSize.height+100 || !(isAlive())) {
    			alive = false;
    		}
    		
    	}
    }

    public boolean isAlive() {
        if(isShot()){
            alive = false;
        }
        return alive;
    }

    public boolean isShot() {
        return shot;
    }


    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getCurrentImage(), this.getX(), this.getY(), null);
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void shoot() throws IOException {
    	if (System.currentTimeMillis() >= canShootTime) {
    		canShootTime = System.currentTimeMillis() + shootCooldown;
	        EnemyBullet bullet = new EnemyBullet(x, y);
	        enemyBullets.add(bullet);
    	}
    }
    

    public BufferedImage getCurrentImage() {
        return currentImage;
    }
}
