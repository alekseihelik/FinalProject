import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        image = ImageIO.read(new File("sprites/Enemy.png"));
        hitbox = new Rectangle(x,y,image.getWidth(),image.getHeight());
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

}
