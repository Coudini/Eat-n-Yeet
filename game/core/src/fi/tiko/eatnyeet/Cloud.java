package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Cloud extends GraphicObject {

    private final float WINDOW_WIDTH = 16f;
    private final float WINDOW_HEIGHT = 9f;

    public static Texture texture1;
    public static Texture texture2;
    public static Texture texture3;
    public float speed;

    public Cloud(Texture texture, GameScreen game) {
        super(texture, game);
        setSize(this.getWidth()/50, this.getHeight()/50);
        setX(-2f);
        setX(MathUtils.random(-2f, WINDOW_WIDTH));
        setY(randomizeHeight());
        speed = randomizeSpeed();
    }

    public void update() {
        super.update();
        move();
    }
    public void move() {
        setX(getX() + speed);
        if (getX() > WINDOW_WIDTH) {
            setX(-2f);
            speed = randomizeSpeed();
            setY(randomizeHeight());
        }
    }
    public float randomizeSpeed() {
        float temp = MathUtils.random(0.005f,0.02f);
        return temp;
    }
    public float randomizeHeight() {
        float temp = MathUtils.random(4f,7.4f);
        return temp;
    }
}
