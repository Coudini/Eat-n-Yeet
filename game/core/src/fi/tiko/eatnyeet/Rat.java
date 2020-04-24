package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Rat extends GameObject {

    private final float WINDOW_WIDTH = 16f;
    private  float FLOOR;

    boolean isRight = true;
    private float timeWhenPickedUp;
    private boolean doneDyingAnimation = false;

    public  static Texture run;
    Animation<TextureRegion> ratRun;

    protected FlingableObject objectToCarry;
    protected boolean isCarryingFlingable = false;

    public Rat (float posX, float posY, GameScreen game) {
        super(posX, posY, 1f, 0.5f, game);
        ratRun = Util.createTextureAnimation(6,1, run);

        body = createBody(posX,posY,0.3f);

        //body.setGravityScale(-80f);

        allowPlayerCollision();
        //ignorePlayerCollision();

    }

    public void update () {
        super.update();
        move();
        //flingListener();
        updateObjectToCarry();
        if(isCarryingFlingable) {

            // start dying by falling trough floor
            if (lifeTime - timeWhenPickedUp > 1.5f && !doneDyingAnimation) {
                doneDyingAnimation = true;
                ignoreGroundCollision();
                objectToCarry.ignoreGroundCollision();
            }
            if (lifeTime - timeWhenPickedUp > 2f) {
                killYourSelf();
            }
            //run etc..
        }
    }

    public void killYourSelf() {

        // add healthbar--; here
        game.healthbar.reduce();

        game.toBeDeleted.add(this);
        game.toBeDeleted.add(objectToCarry);
        game.player.healthPoints--;

    }
    public void throwObjectToCarry (float speedX,float speedY) {
        objectToCarry.body.setLinearVelocity(speedX, speedY);
        objectToCarry.body.applyAngularImpulse(Math.signum(speedX) * -0.3f, true);

        isCarryingFlingable = false;
        allowPlayerCollision();

        objectToCarry.timeWhenThrown = objectToCarry.lifeTime;
        objectToCarry.isBeingCarried = false;
        objectToCarry.isJustThrown = true;
    }

    private float tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));
    public void move() {
        //System.out.println("*tips fedora");

        //add moving towards veggies here

            //temporal movement
        if (getX() < WINDOW_WIDTH / 2 && getX() >  1.3f) {
            if (isRight) {
                body.setLinearVelocity(1f, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-1f, body.getLinearVelocity().y);
            }
        } else if(getX() > WINDOW_WIDTH / 2 && getX() < WINDOW_WIDTH - 1.3f) {
            if (isRight) {
                body.setLinearVelocity(1f, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-1f, body.getLinearVelocity().y);
            }
        } else {
            float temp = MathUtils.random(-1f,1f);
            //body.setLinearVelocity(temp, 0f);
            body.applyLinearImpulse(new Vector2(temp, 0f), body.getWorldCenter(), true);

        }
        if (body.getLinearVelocity().x > 0 && !isRight) {
            //Util.flip(ratIdle);
            Util.flip(ratRun);
            isRight = true;
            tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));
        }
        if (body.getLinearVelocity().x < 0 && isRight) {
            //Util.flip(ratIdle);
            Util.flip(ratRun);
            isRight = false;
            tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));
        }

        //for random jittery jumps, can't get gravity working. rat falls too slow
        //if (getX() > tempX - 0.3f && getX() < tempX + 0.3f) {
        //    body.applyLinearImpulse(new Vector2(0f,1f), body.getWorldCenter(), true);
        //}

        currentAnimation = ratRun;

        /**
        // If character is moving use characterRun animation
        if (body.getLinearVelocity().x != 0) {
            currentAnimation = characterRun;
        }
        // if not moving use idle animation
        else {
            currentAnimation = characterIdle;

        }
         */
    }
    @Override
    public void onCollision(Contact contact, GameObject other) {
        if (other != null && other instanceof FlingableObject &&  other instanceof Food) {
            if (!isCarryingFlingable) {

                objectToCarry = (FlingableObject) other;
                objectToCarry.isBeingCarried = true;
                isCarryingFlingable = true;

                timeWhenPickedUp = lifeTime;
                // when colliding mask waste to ignore player collision
                ignorePlayerCollision();
                objectToCarry.ignorePlayerCollision();
            }
        }
    }

    private void updateObjectToCarry () {

        if (isCarryingFlingable) {
            float xModif = -0.4f;
            float yModif = -0.1f;
            if (isRight) {
                xModif = 0.4f;
            }

            // set body location to player body location -+ x and y modifiers
            objectToCarry.body.setTransform(this.body.getPosition().x + xModif,this.body.getPosition().y + yModif,0f);
        }
    }
    public void ignorePlayerCollision() {
        Filter filter = new Filter();
        filter.categoryBits = PLAYER_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    public void allowPlayerCollision(){
        Filter filter = new Filter();
        filter.categoryBits = PLAYER_BITS;
        filter.maskBits = DEFAULT_BITS | FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    public void ignoreGroundCollision() {
        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    protected void resetObjectToCarry() {
        isCarryingFlingable = false;
        objectToCarry = null;
        allowPlayerCollision();
    }
}



