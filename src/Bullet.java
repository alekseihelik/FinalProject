import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bullet {
    private int x;
    private int y;
    private boolean alive;
    private BufferedImage bulletImage;

    public Bullet(int x, int y) throws IOException {
        bulletImage = ImageIO.read(new File("sprites/Bullet.png"));
        this.x = x;
        this.y = y;
        this.alive = true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getBulletImage() {
        return bulletImage;
    }

    public boolean isAlive() {
        return alive;
    }

    public void update() {
        y -= 10; // Adjust the bullet's speed here
        if (y < 0) {
            alive = false;
        }
    }


}
