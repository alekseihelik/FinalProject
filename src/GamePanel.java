import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	private Player player;
	
	public static ArrayList<Bullet> bullets = new ArrayList<>();
	
	public GamePanel() throws IOException {
		player = new Player(this);
		this.addKeyListener(player);
		this.setFocusable(true);
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
	
	            try {
	                Thread.sleep(1); // Small delay to avoid excessive CPU usage
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	   gameLoop.start();
    }
   
   public void paintComponent(Graphics g) {
       super.paintComponent(g);
       
       Graphics2D g2d = (Graphics2D) g;
       
       player.update();
       for(int i = 0; i < bullets.size(); i++) {
    	   bullets.get(i).update();
    	   bullets.get(i).draw(g2d);
       }
       player.draw(g2d);
       
       
       }
}
