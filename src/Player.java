import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player implements KeyListener {
    private Rectangle hitbox;
    private final int SPEED = 5;
    private boolean alive;
    private int lives;
    private int score;
    private int x;
    private int y;
    private MainGUI mainGUI;
    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private BufferedImage playerImage;

    public Player(MainGUI mainGUI) throws IOException {
        playerImage = ImageIO.read(new File("sprites/Placeholder.png"));
        hitbox = new Rectangle(x,y,playerImage.getWidth(),playerImage.getHeight());
        score = 0;
        x = 450;
        y = 600;
        alive = true;
        lives = 3;
        this.mainGUI = mainGUI;
        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;
        startGameLoop();
    }

    public BufferedImage getPlayerImage(){
        return playerImage;
    }
    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveUp() {
        y -= 100;
    }

    public void moveDown() {
        y += 100;
    }

    public void moveLeft() {
        x -= 100;
    }

    public void moveRight() {
        x += 100;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            isMovingUp = true;
        } else if (key == KeyEvent.VK_S) {
            isMovingDown = true;
        } else if (key == KeyEvent.VK_A) {
            isMovingLeft = true;
        } else if (key == KeyEvent.VK_D) {
            isMovingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            isMovingUp = false;
        } else if (key == KeyEvent.VK_S) {
            isMovingDown = false;
        } else if (key == KeyEvent.VK_A) {
            isMovingLeft = false;
        } else if (key == KeyEvent.VK_D) {
            isMovingRight = false;
        }
    }

    private void startGameLoop() {
        Thread gameLoop = new Thread(() -> {
            long desiredFrameRate = 60; // Adjust the desired frame rate here
            long frameTime = 1000 / desiredFrameRate;
            long lastUpdateTime = System.currentTimeMillis();

            while (true) {
                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - lastUpdateTime;

                if (elapsedTime >= frameTime) {
                    update();
                    lastUpdateTime = currentTime;
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

    private void update() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int dx = 0;
        int dy = 0;

        if (isMovingUp) {
            if(y-SPEED>-10) {
                dy -= SPEED;
            }
        }
        if (isMovingDown) {
            if(y+SPEED<screenSize.getHeight()-150) {
                dy += SPEED;
            }
        }
        if (isMovingLeft) {
            if(x-SPEED>-25) {
                dx -= SPEED;
            }
        }
        if (isMovingRight) {
            if(x+SPEED<screenSize.getHeight()-170) {
                dx += SPEED;
            }
        }

        if (dx != 0 && dy != 0) {
            // Diagonal movement
            dx /= Math.sqrt(2);
            dy /= Math.sqrt(2);
        }

        x += dx;
        y += dy;

        mainGUI.repaint();
    }

    public void death() {
        if (lives <= 0) {
            alive = false;
        }
    }
}