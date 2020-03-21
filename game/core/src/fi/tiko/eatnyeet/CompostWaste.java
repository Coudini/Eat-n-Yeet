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

public class CompostWaste extends GameObject {

    private static Texture waste = new Texture("temp_compost_stuff.png");
    protected boolean isTouchingPlayer = false;
    protected boolean isJustThrown = false;
    protected int frameCount = 0;

    public static final short DEFAULT_BITS = 0x0001;
    public static final short PLAYER_BITS = 0x0002;
    public static final short COMPOST_BITS = 0x0004;
    public static final short FOOD_BITS = 0x0008;

    public CompostWaste(float x, float y, MainGame game) {
        super(waste, x,y, 1f, 1f, game);
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
        trackPlayer();
        move();
    }

    private void trackPlayer() {

    }
    @Override
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {

        if (other != null && other.body.getUserData().equals("wall") ) {
            System.out.println("wall collision");
        }

        if (other != null && other instanceof Character) {
            System.out.println("collided with character");

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

            int realX = Gdx.input.getX();
            int realY = Gdx.input.getY();
            Vector3 touchPos = new Vector3(realX, realY, 0);
            game.camera.unproject(touchPos);

            float speedX = (touchPos.x - body.getPosition().x) / 3;
            float speedY = (touchPos.y - body.getPosition().y) / 3;

            System.out.println(speedY);

            body.applyLinearImpulse(new Vector2(speedX,speedY),body.getWorldCenter(),true);
            body.applyAngularImpulse(Math.signum(speedX)*-0.01f,true);

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
            float tempX = game.player.body.getPosition().x;
            float tempY = game.player.body.getPosition().y + 1f;
            float angle = body.getAngle();
            body.setTransform(tempX,tempY,angle);
        }

        // counts 60 frames before allowing to touch waste again
        if (isJustThrown) {
            frameCount++;
            if (frameCount > 60) {
                frameCount = 0;
                isJustThrown = false;
            }
        }
    }
}
