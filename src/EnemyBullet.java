import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class EnemyBullet {
    private int x;
    private int y;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 60;

    private boolean alive;
    private BufferedImage bulletImage;
    private Rectangle hitbox;

    public EnemyBullet(int x, int y) throws IOException {
        bulletImage = ImageIO.read(new File("sprites/Bullet.png"));
        this.x = x;
        this.y = y;
        this.alive = true;
        hitbox = new Rectangle(x,y,bulletImage.getWidth(),bulletImage.getHeight());
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

    public void draw(Graphics2D g2d) {
        g2d.drawImage(bulletImage, x, y, WIDTH, HEIGHT, null);
    }

    public void update() {
        if (alive) {
           y += 10;
            hitbox = new Rectangle(x,y,bulletImage.getWidth(),bulletImage.getHeight());
            if (y <= -HEIGHT) {
                alive = false;
            }
        }
    }

    private static BufferedImage rotate(BufferedImage bimg, Double angle) {
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = bimg.getWidth();
        int h = bimg.getHeight();
        int n = (int) Math.floor(w*cos + h*sin);
        int nh = (int) Math.floor(h*cos + w*sin);
        BufferedImage rotated = new BufferedImage(n, nh, bimg.getType());
        Graphics2D graphic = rotated.createGraphics();
        graphic.translate((n-w)/2, (nh-h)/2);
        graphic.rotate(Math.toRadians(angle), w/2, h/2);
        graphic.drawRenderedImage(bimg, null);
        graphic.dispose();
        return rotated;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
