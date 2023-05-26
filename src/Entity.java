import java.awt.*;

public class Entity {

    private int health;
    private int score;
    private int bullets;

    public Entity(){
        health = 1;
        score = 1;
        bullets = 1;
    }

    public Entity(int hp, int score, int bullets){
        health = hp;
        this.score = score;
        this.bullets = bullets;
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }

    public int getBullets() {
        return bullets;
    }

}
