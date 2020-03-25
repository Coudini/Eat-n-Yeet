package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class FlingableObject extends GameObject {

    // flying related variables
    protected boolean isBeingCarried = false;
    protected boolean isOnFloor = false;
    protected float flyTime = 0f;

    // fillvalue related variables
    protected float fillAmount = 1f;

    public FlingableObject (Texture texture, float x, float y, float width, float height, MainGame game) {
        super(texture, x, y, width, height, game);
    }


    @Override
    public void update() {
        super.update();
        flyTimeUpdate();
    }

    public void setFillAmount (float amount) {
        fillAmount = amount;
    }
    public float getFillAmount() {
        return  fillAmount;
    }
    public void ignorePlayerCollision() {
        Filter filter = new Filter();
        filter.categoryBits = FLINGABLE_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    public void allowPlayerCollision(){
        Filter filter = new Filter();
        filter.categoryBits = FLINGABLE_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    public void flyTimeUpdate() {
        if (!isBeingCarried && !isOnFloor) {
            flyTime += Gdx.graphics.getDeltaTime();
        }
    }
    public void resetGraityScale() {
        this.body.setGravityScale(1f);
    }

}
