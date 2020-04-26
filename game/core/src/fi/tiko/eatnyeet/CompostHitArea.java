package fi.tiko.eatnyeet;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;


public class CompostHitArea extends GameObject {

    public Sound sound;

    /**
     * Constructor, values should be generated from tilemap. Tho it can be created manually, but loses easy replacement ability.
     * @param width size
     * @param height size
     * @param body body for supesclass to save, collision detection.
     * @param game saved to superclass
     */
    public CompostHitArea(float width, float height, Body body , GameScreen game) {
        super(width,height, body, game);

        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
        sound = audio.newSound(files.internal("score.mp3"));
    }

    /**
     * Checks if flingablle object has touched this object, if is do set tasks.
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof Food && other instanceof FlingableObject) {
            game.toBeDeleted.add(other);
            // if character carries the object to compost this will reset character object reference and booleans
            if (((FlingableObject) other).isBeingCarried) {
                callAfterPhysicsStep(() -> {
                    ((FlingableObject) other).isBeingCarried = false;
                    game.player.resetObjectToCarry();
                    return null;
                });
            }

            if (game.compost.fillLevel >= game.compost.maxFill) {
                // field full, could add feature to notify user about it
            } else {
                game.compost.fillLevel += ((FlingableObject) other).getFillAmount();
                if (game.player.characterCombo == 0) {
                    game.player.characterScore += 1;
                } else {
                    game.player.characterScore += (int) ((FlingableObject) other).flyTime * game.player.characterCombo;
                }
                game.player.characterCombo += 1;
            }
            if (game.sounds) {
                sound.play(0.2f);
            }
        }

    }
}
