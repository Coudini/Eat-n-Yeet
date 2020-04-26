package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Cloud extends GraphicObject {

    public static Texture texture1;
    public static Texture texture2;
    public static Texture texture3;
    public float speed;

    /**
     * Basically default constructor for cloud, uses default values
     * @param texture texture for cloud
     * @param mainGame needed for superclass
     */
    public Cloud(Texture texture, MainGame mainGame) {
        super(texture, mainGame);
        setSize(this.getWidth()/50, this.getHeight()/50);
        setX(-2f);
        setX(MathUtils.random(-2f, mainGame.GAME_CAM_WIDTH));
        setY(randomizeHeight());
        speed = randomizeSpeed();
    }

    /**
     * Called on every iteration, calls methods and does if checking for the class
     */
    @Override
    public void update() {
        super.update();
        move();
    }

    /**
     * Moves the cloud with default values
     */
    public void move() {
        setX(getX() + speed);
        if (getX() > mainGame.GAME_CAM_WIDTH) {
            setX(-2f);
            speed = randomizeSpeed();
            setY(randomizeHeight());
        }
    }

    /**
     * Randomizes speed for cloud
     * @return the speed that is calculated
     */
    public float randomizeSpeed() {
        float temp = MathUtils.random(0.005f,0.02f);
        return temp;
    }

    /**
     * Randomizes cloud size
     * @return the size value
     */
    public float randomizeHeight() {
        float temp = MathUtils.random(4f,7.4f);
        return temp;
    }
}
