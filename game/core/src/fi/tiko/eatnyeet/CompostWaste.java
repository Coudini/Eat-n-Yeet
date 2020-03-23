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

    public static Texture texture;

    protected boolean isTouchingPlayer = true;
    protected boolean isJustThrown = false;
    protected int frameCount = 0;

    protected float fillAmount;

    public CompostWaste(float x, float y,float fill, MainGame game) {
        super(texture, x,y, 1f, 1f, game);
        this.fillAmount = fill;
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.5f);
        body = createBody(x,y,0.3f);

        Filter filter = new Filter();
        filter.categoryBits = COMPOST_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }


    public float getFillAmount() {
        return fillAmount;
    }
}
