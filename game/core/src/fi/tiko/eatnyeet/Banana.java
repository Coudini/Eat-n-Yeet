package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Banana extends GameObject implements Flingable, Food {
    private static Texture texture = new Texture("banana.png");

    protected boolean isTouchingPlayer = false;
    protected boolean isJustThrown = false;
    protected int frameCount = 0;

    // used for detect if object can pass through other object


    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.5f, 0.5f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.15f);

        Filter filter = new Filter();
        filter.categoryBits = FOOD_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    public void update () {
        move();
    }

    @Override
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {

        if (other != null && other instanceof Character) {
            if (!isJustThrown) {
                isTouchingPlayer = true;

                // when colliding mask waste to ignore player collision
                Filter filter = new Filter();
                filter.categoryBits = FOOD_BITS;
                filter.maskBits = DEFAULT_BITS;
                for (Fixture fix: body.getFixtureList()) {
                    fix.setFilterData(filter);
                }
            }
        }
    }

    public void move () {

        if (Gdx.input.justTouched() && isTouchingPlayer) {

            fling();

            isJustThrown = true;
            isTouchingPlayer = false;

            // when thrown mask it to recognize player character again
            Filter filter = new Filter();
            filter.categoryBits = FOOD_BITS;
            filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
            for (Fixture fix: body.getFixtureList()) {
                fix.setFilterData(filter);
            }

        } else if (isTouchingPlayer && !isJustThrown) {
            trackPlayer();
        }

        // counts 20 frames before allowing to touch waste again
        if (isJustThrown) {
            frameCount++;
            if (frameCount > 20) {
                frameCount = 0;
                isJustThrown = false;
            }
        }
    }

    public float getFillAmount() {
        return 1f;
    }

}
