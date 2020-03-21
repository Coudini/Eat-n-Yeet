package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CompostWaste extends GameObject implements Flingable {

    private static Texture waste = new Texture("temp_compost_stuff.png");
    protected boolean isTouchingPlayer = true;
    protected boolean isJustThrown = false;
    protected int frameCount = 0;


    protected float fillAmount;

    public CompostWaste(float x, float y,float fill, MainGame game) {
        super(waste, x,y, 1f, 1f, game);
        this.fillAmount = fill;
        setDensity(0.2f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(x,y,0.3f);

        Filter filter = new Filter();
        filter.categoryBits = COMPOST_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {

        if (other != null && other instanceof Character) {
            if (!isJustThrown) {
                isTouchingPlayer = true;

                // when colliding mask waste to ignore player collision
                Filter filter = new Filter();
                filter.categoryBits = COMPOST_BITS;
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
            filter.categoryBits = COMPOST_BITS;
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
        return fillAmount;
    }
}
