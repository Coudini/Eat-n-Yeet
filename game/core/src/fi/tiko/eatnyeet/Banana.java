package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Banana extends GameObject implements Flingable {
    private static Texture texture = new Texture("banana.png");

    // used for detect if object can pass through other object
    public static final short DEFAULT_BITS = 0x0001;
    public static final short PLAYER_BITS = 0x0002;
    public static final short COMPOST_BITS = 0x0004;
    public static final short FOOD_BITS = 0x0008;

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.5f, 0.5f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.15f);

        Filter filter = new Filter();
        filter.categoryBits = FOOD_BITS;
        filter.maskBits = DEFAULT_BITS | FOOD_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    public void update () {
        move();
    }
    public void move () {
        if (Gdx.input.justTouched()) {

            int realX = Gdx.input.getX();
            int realY = Gdx.input.getY();
            Vector3 touchPos = new Vector3(realX, realY, 0);
            game.camera.unproject(touchPos);

            float speedX = (touchPos.x - body.getPosition().x) / 5;
            float speedY = (touchPos.y - body.getPosition().y) / 5;

            // gives speed to banana based on click position, TODO fling support
            body.applyLinearImpulse(new Vector2(speedX,speedY),body.getWorldCenter(),true);
            body.applyAngularImpulse(Math.signum(speedX)*-0.01f,true);
        }
    }

    public float getFillAmount() {
        return 1f;
    }
}
