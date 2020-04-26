package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

/**
 * Visual for force dragged to shoop object
 */
public class ForceMeter extends GraphicObject {

    public static Texture texture;
    public static float width = 0.5f;
    public static float height = 0.3f;
    public static float x;
    public static float y;
    public static boolean show;
    public static double rotation;
    public static float angular;

    /**
     * Default constructor
     * @param mainGame passed to superclass
     */
    public ForceMeter(MainGame mainGame) {
        super(texture, width, height, mainGame);
        setOriginCenter();
        show = false;
    }

    /**
     * Called on every iteration, calls methods and does if handling
     */
    @Override
    public void update () {
        super.update();
        if (show) {
            setMeter();
            setTexture(texture);
        } else {
            setTexture(null);
        }
    }

    /**
     * Set meter values, those are set beforehand.
     */
    public void setMeter() {
        setR();
        setA();
        setX(x);
        setY(y);

    }

    public static void setXY(float X, float Y) {
        x = X;
        y = Y;
    }

    public static void show(boolean b) {
        show = b;
    }

    public static void setRotate(double d) {
        rotation = d;
    }
    public void setR() {
        setRotation((float)rotation);
    }

    public void setA() {
        rotate(angular);
    }
}
