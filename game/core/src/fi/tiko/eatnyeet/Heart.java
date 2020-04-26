package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;


/**
 * this is icon that is spawned to healthbar
 */
public class Heart extends GraphicObject {

    public static Texture texture;
    public static float width = 0.25f;
    public static float height = 0.25f;
    public static float y = 7.8f;

    /**
     * Constructor
     * @param X position
     * @param game saved to superclass
     */
    public Heart (float X, MainGame game) {
        super(texture, width, height, game);
        setPosition(X, y);
    }
}
