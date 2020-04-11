package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class ForceMeter extends GraphicObject {

    public static Texture texture;
    public static float width = 0.5f;
    public static float height = 0.5f;
    public static float x;
    public static float y;
    public static boolean show;
    public static double rotation;
    public static float angular;

    public ForceMeter(GameScreen game) {
        super(texture, width, height, game);
        setOriginCenter();
        System.out.println("meter made");
        show = false;
    }

    public void update () {
        super.update();

        //add an additional if() for less texture calls
        if (show) {
            setMeter();
            setTexture(texture);
        } else {
            setTexture(null);
        }
    }

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
