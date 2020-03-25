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
    protected float timeWhenThrown;
    protected boolean isJustThrown = false;

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

    /**
     * Ignores collision with character class
     * This should be called in subclass when creating object to define object as FLINGABLE_BITS
     * You can also use allowPlayerCollision() method to do same
     */
    public void ignorePlayerCollision() {
        Filter filter = new Filter();
        filter.categoryBits = FLINGABLE_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    /**
     * allows collision with character class
     * This should be called in subclass when creating object to define object as FLINGABLE_BITS
     * You can also use ignorePlayerCollision() method to do same
     */
    public void allowPlayerCollision(){
        Filter filter = new Filter();
        filter.categoryBits = FLINGABLE_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    /***
     * If object is not being carried or not touching floor we can assume its flying
     * updates the time spent in air for highscore purposes
     * Also resets player collision if character just throwed it after set delay
     */
    public void flyTimeUpdate() {
        if (!isBeingCarried && !isOnFloor) {
            flyTime += Gdx.graphics.getDeltaTime();

            if (isJustThrown && timeWhenThrown > 0f && lifeTime - timeWhenThrown > 0.5f) {
                allowPlayerCollision();
                timeWhenThrown = 0f;
                isJustThrown = false;
            }
        }
    }
    public void resetGraityScale() {
        this.body.setGravityScale(1f);
    }

}