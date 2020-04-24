package fi.tiko.eatnyeet;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class CompostCollectPoint extends GameObject {
    public CompostCollectPoint(float width, float height, Body body , GameScreen game) {
        super(width,height, body, game);

        Filter filter = new Filter();
        filter.categoryBits = DEFAULT_BITS;
        filter.maskBits = PLAYER_BITS | DEFAULT_BITS | FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }

    }
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
            game.player.pick.play();
        }
    }
}
