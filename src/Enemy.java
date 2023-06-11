import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Enemy {

    private boolean alive;
    private boolean shot;
    private BufferedImage image;
    private Rectangle hitbox;
    private int x;
    private int y;
    private int path;
    private GamePanel gp;
    private ArrayList<EnemyBullet> enemyBullets = new ArrayList<EnemyBullet>();

    public Enemy(int path, int x, GamePanel gp) throws IOException {
        alive = true;
        shot = false;
        BufferedImage originalImage = ImageIO.read(new File("sprites/EnemyPlaceholder.png"));
        image = resizeImage(originalImage, originalImage.getWidth()/3, originalImage.getHeight()/3);
        hitbox = new Rectangle(x,y,image.getWidth(),image.getHeight());
        y=-100;
        this.x = x;
        this.gp = gp;
        this.path = path;
    }
    
    public void update() throws IOException {
    	if (isAlive()) {
            hitbox = new Rectangle(x,y,image.getWidth(),image.getHeight());
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
    		if (x >= gp.screenWidth + 150 || x <= image.getWidth()-200 || y>= screenSize.height+100 || !(isAlive())) {
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

    public BufferedImage getImage() {
        return image;
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
        g2d.drawImage(getImage(), this.getX(), this.getY(), null);
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
        EnemyBullet bullet = new EnemyBullet(x, y);
        enemyBullets.add(bullet);
    }
}
