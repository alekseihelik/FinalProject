import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Player implements KeyListener {
    private Rectangle hitbox;
    private final int SPEED = 5;
    private int power;
    private int lives;
    private int score;
    private final int COOLDOWN = 130;
    private boolean cooldownActive;
    private int x;
    private int y;
    private GamePanel gamePanel;
    private boolean isMovingUp;
    private boolean isMovingDown;
    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isShooting;
    private static final float VOLUME = -20.0f;
    private BufferedImage playerImage1;
    private BufferedImage playerImage2;
    private BufferedImage currentImage;
    private int highscore;
    

    private long bulletCooldown;
    private final long bulletCooldownTime = 130;

    public Player(GamePanel panel) throws IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cooldownActive = false;
        BufferedImage originalImage1 = ImageIO.read(new File("sprites/Player1.png"));
        BufferedImage originalImage2 = ImageIO.read(new File("sprites/Player2.png"));
        int scaledWidth = originalImage1.getWidth() / 3;
        int scaledHeight = originalImage1.getHeight() / 3;
        playerImage1 = resizeImage(originalImage1, scaledWidth, scaledHeight);
        playerImage2 = resizeImage(originalImage2, scaledWidth, scaledHeight);
        currentImage = playerImage1;
        hitbox = new Rectangle(x, y, currentImage.getWidth(), currentImage.getHeight());
        score = 0;
        x = (int) (screenSize.getHeight() - 180) / 2;
        y = 600;
        lives = 3;
        this.gamePanel = panel;
        isMovingUp = false;
        isMovingDown = false;
        isMovingLeft = false;
        isMovingRight = false;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (currentImage == playerImage1) {
                    currentImage = playerImage2;
                } else {
                    currentImage = playerImage1;
                }
            }
        }, 0, 100);
    }

    public BufferedImage getPlayerImage() {
        return currentImage;
    }
    public int getLives() {
        return lives;
    }

    public int getScore() {
        return score;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void decrementLives() {
    	lives--;
    }
    
    public void die() {

    }
    
    public void shootBullet() {
        if (lives>0) {
            try {
                if (new Date().getTime() > bulletCooldown) {
                    bulletCooldown = new Date().getTime() + bulletCooldownTime;
                    Bullet bullet = new Bullet(x + playerImage1.getWidth()/2 - Bullet.WIDTH/2, y - Bullet.HEIGHT - Bullet.DISTANCE_TO_PLAYER+50);
                    GamePanel.bullets.add(bullet);
                }
            } catch (IOException e) {}

        }
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
        } else if (key == KeyEvent.VK_SPACE) {
            isShooting = true;
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
        } else if (key == KeyEvent.VK_SPACE) {
            isShooting = false;
        }
    }

    public void draw(Graphics2D g2d) {
        g2d.drawImage(getPlayerImage(), this.getX(), this.getY(), null);

    }

    public void update() {
        if(lives>0) {
            String filePath = "src/highscore.txt";
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line = br.readLine();
                int highscore = Integer.parseInt(line);
                if (score > highscore) {
                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
                        bw.write(String.valueOf(score));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int dx = 0;
                int dy = 0;

                if (isMovingUp) {
                    if (y - SPEED > -10) {
                        dy -= SPEED;
                    }
                }
                if (isMovingDown) {
                    if (y + SPEED < screenSize.getHeight() - 150) {
                        dy += SPEED;
                    }
                }
                if (isMovingLeft) {
                    if (x - SPEED > -25) {
                        dx -= SPEED;
                    }
                }
                if (isMovingRight) {
                    if (x + SPEED < screenSize.getHeight() - 170) {
                        dx += SPEED;
                    }
                }

                if (dx != 0 && dy != 0) {
                    dx /= Math.sqrt(2);
                    dy /= Math.sqrt(2);
                }

                if (isShooting) {
                    shootBullet();
                    if(!cooldownActive) {
                        try {
                            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sound effects/ShotSoundEffect.wav"));
                            Clip clip = AudioSystem.getClip();
                            clip.open(audioInputStream);
                            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue(VOLUME);
                            clip.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        startCooldown();
                    }
                }

                x += dx;
                y += dy;
                
                hitbox = new Rectangle(x, y, currentImage.getWidth(), currentImage.getHeight());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }

    public Rectangle getHitbox() {
    	return hitbox;
    }
    
    public void increaseScore(int increment){
        score += increment;
    }

    public int getHighscore() {
        return highscore;
    }

    public int getPower() {
        return power;
    }

    public void increasePower(){
        power++;
    }

    private void startCooldown() {
        cooldownActive = true;
        Timer cooldownTimer = new Timer();
        cooldownTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                cooldownActive = false;
                cooldownTimer.cancel();
            }
        }, COOLDOWN);
    }
}