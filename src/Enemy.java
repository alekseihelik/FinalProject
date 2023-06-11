import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Enemy {

    private boolean alive;
    private boolean shot;
    private BufferedImage image;
    private Rectangle hitbox;
    private int x;
    private int y;

    public Enemy() throws IOException {
        alive = true;
        shot = false;
        BufferedImage originalImage = ImageIO.read(new File("sprites/EnemyPlaceholder.png"));
        image = resizeImage(originalImage, originalImage.getWidth()/3, originalImage.getHeight()/3);
        hitbox = new Rectangle(x,y,image.getWidth(),image.getHeight());
        x=0;
        y=-100;
    }

    public boolean isAlive() {
        if(shot){
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

}
