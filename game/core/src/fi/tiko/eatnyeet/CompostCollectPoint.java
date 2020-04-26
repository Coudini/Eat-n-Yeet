package fi.tiko.eatnyeet;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Area where player needs to walk to be able to interact with Compost
 */
public class CompostCollectPoint extends GameObject {

    /**
     * Constructor, values should be generated from tilemap. Tho it can be created manually, but loses easy replacement ability.
     * @param width size
     * @param height size
     * @param body body for supesclass to save, collision detection.
     * @param game saved to superclass
     */
    public CompostCollectPoint(float width, float height, Body body , GameScreen game) {
        super(width,height, body, game);

        Filter filter = new Filter();
        filter.categoryBits = DEFAULT_BITS;
        filter.maskBits = PLAYER_BITS | DEFAULT_BITS | FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }

    }

    /**
     * Checks if chacater has touched this object, if is do set tasks.
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof Character && game.compost.fillLevel > 0f) {


            if (((Character) other).isCarryingFlingable == false) {
                callAfterPhysicsStep(() -> {
                    float posX = game.player.body.getPosition().x;
                    float posY = game.player.body.getPosition().y + 1f;
                    float fill = game.compost.fillLevel;

                    if (game.compost.fillLevel >= game.compost.maxFill) {
                        fill = game.compost.maxFill;
                    }

                    CompostWaste temp = new CompostWaste(posX,posY,fill, game);
                    game.gameObjects.add(temp);
                    game.compost.fillLevel = 0f;
                    return null;
                });
            }
            if (game.sounds){
                game.player.pick.play(0.4f);
            }

        }
    }
}
