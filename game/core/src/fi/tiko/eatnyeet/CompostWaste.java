package fi.tiko.eatnyeet;
import com.badlogic.gdx.graphics.Texture;


/**
 * Compostwaste carries information of fillamount which determines how well crops grows in Field.
 */
public class CompostWaste extends FlingableObject {

    public static Texture texture;

    /**
     * Defaulft constructor, should not need any other. Spawned when character goes to pick up compostWaste.
     * @param x position
     * @param y position
     * @param fill fillamount from compost
     * @param game saved to superclass
     */
    public CompostWaste(float x, float y,float fill, GameScreen game) {
        super(texture, x,y, 1f, 1f, game);
        this.fillAmount = fill;
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.5f);
        body = createBody(x,y,0.7f);
        allowPlayerCollision();
    }


    /**
     * Called on every iteration, calls methods and does if checking needed for the class.
     */
    @Override
    public void update() {
        super.update();
        flyTimeUpdate();
    }
}
