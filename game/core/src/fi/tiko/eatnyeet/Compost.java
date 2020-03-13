package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Compost extends GameObject {
    private static Texture texture = new Texture("compost_stage4.png");
    float fillLevel;

    public Compost(float width, float height,Body body , MainGame game) {
        super(texture, width,height, body, game);

    }

    @Override
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {
        if (other != null && other instanceof Flingable) {
            game.toBeDeleted.add(other);
        }
    }
}
