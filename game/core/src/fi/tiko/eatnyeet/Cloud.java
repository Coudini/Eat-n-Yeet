package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class Cloud extends GraphicObject {

    private final float WINDOW_WIDTH = 16f;
    private final float WINDOW_HEIGHT = 9f;
    public static Texture texture1;
    public static Texture texture2;
    public static Texture texture3;
    //public static float width;
    //public static float height;
    public static float speed;

    public Cloud(Texture texture, MainGame game) {
        super(texture, game);
        setX(4f);
        setY(4f);
        System.out.println("Cloud constructor");
    }

    public void update() {
        super.update();
    }
    public void move() {

    }
    public void randomizeSpeed() {

    }
    public void randomizeHeight() {

    }
}
