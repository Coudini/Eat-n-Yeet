package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class Sun extends GraphicObject {

    public static Texture texture1;
    public static Texture texture2;

    public Sun(Texture texture, MainGame game) {
        super(texture, game);
        setSize(2f,2f);
        setX(0.5f);
        setY(7f);
    }
    public void update() {
        super.update();
    }
}
