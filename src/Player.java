import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player implements KeyListener {
    private int score;
    private int time;
    private int x;
    private int y;

    public Player() {
        score = 0;
        time = 0;
        x = 450;
        y = 600;
    }

    public int getScore() {
        return score;
    }

    public int getTime() {
        return time;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void moveUp() {
        y--;
        System.out.println("W pressed");
    }

    public void moveDown() {
        y++;
        System.out.println("S pressed");
    }

    public void moveLeft() {
        x--;
        System.out.println("A pressed");
    }

    public void moveRight() {
        x++;
        System.out.println("D pressed");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) {
            moveUp();
        } else if (key == KeyEvent.VK_S) {
            moveDown();
        } else if (key == KeyEvent.VK_A) {
            moveLeft();
        } else if (key == KeyEvent.VK_D) {
            moveRight();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Not used
    }
}