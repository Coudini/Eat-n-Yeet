package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sun extends GraphicObject {
    public static Texture sunNoDisco;
    public static Texture sunDisco;
    Animation<TextureRegion> sun;
    Animation<TextureRegion> discoSun;

    /**
     * Constructor for Sprite, creates 2 animations.
     * @param game
     */
    public Sun(MainGame game) {
        super(game);
        sun = Util.createTextureAnimation(12,1, sunNoDisco);
        discoSun = Util.createTextureAnimation(12,1,sunDisco);
        setSize(2f,2f);
        setX(0.5f);
        setY(7f);
    }

    /**
     * Called on every iteration, overides superclass version, but superclass version is called in the function.
     * Used to call methods and do if checking on every frame.
     */
    public void update() {
        super.update();
        checkDisco();
    }

    /**
     * Checks if discomode needs to be activated. Sets new textures based on results.
     */
    public void checkDisco() {
        // discomode based on combo-points
        if (mainGame.gameScreen.player.characterCombo > 2) {
            currentAnimation = discoSun;
        } else {
            currentAnimation = sun;
        }
    }
}
