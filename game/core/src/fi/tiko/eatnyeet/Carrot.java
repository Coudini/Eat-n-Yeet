package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Carrot extends FlingableObject implements Food {

    public static Texture texture1;
    public static Texture texture2;

    public static Texture carrotNoDisco;
    public static Texture carrotDisco;

    Animation<TextureRegion> carrot;
    Animation<TextureRegion> discoCarrot;

    public boolean eaten;

    public static Texture carrotEaten;


    /***
     * Constuctor with ability to set position
     * @param posX position
     * @param posY position
     * @param game needed to save to superclass
     */
    public Carrot(float posX, float posY, GameScreen game) {
        super(texture1, posX, posY, 0.3f, 0.3f, game);
        setSize(0.6f,0.6f);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.6f);
        body = createBody(posX,posY,0.3f);
        allowPlayerCollision();
        eaten = false;

    }

    /***
     * Constructor with ability set position and radius
     * @param posX position
     * @param posY position
     * @param radius radius
     * @param game needed to save to superclass
     */
    public Carrot(float posX, float posY,float radius, GameScreen game) {
        super(texture1, posX, posY, 0.6f, 0.6f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.6f);
        body = createBody(posX,posY,radius);
        allowPlayerCollision();
        carrot = Util.createTextureAnimation(4,1, carrotNoDisco);
        discoCarrot = Util.createTextureAnimation(4,1,carrotDisco);
        eaten = false;

    }

    /**
     * Called on every iteration, overides superclass version, but superclass version is called in the function.
     * Used to call methods and do if checking on every frame.
     */
    @Override
    public void update() {
        super.update();

        if (getTexture().equals(carrotEaten)) {
            eaten = true;
        }
        if (!eaten) {
            checkDisco();
        }
        if (isOnFloor && body.getGravityScale() < 1f) {
            resetGravityScale();
        }
    }

    /**
     * Checks if discomode needs to be activated. Sets new textures based on results.
     */
    public void checkDisco(){
        if (game.player.characterCombo > 1) {
            currentAnimation = discoCarrot;
            setTexture(texture2);
        } else {
            currentAnimation = carrot;
            setTexture(texture1);
        }
    }
}
