package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

public class Rat extends GameObject {

    private final float WINDOW_WIDTH = 16f;
    private float speed = 1f;
    private  float FLOOR;

    boolean isRight = true;
    private float timeWhenPickedUp;
    private boolean doneDyingAnimation = false;

    public  static Texture run;
    Animation<TextureRegion> ratRun;

    protected FlingableObject objectToCarry;
    protected boolean isCarryingFlingable = false;

    Sound sound;

    public Rat (float posX, float posY, GameScreen game) {
        super(posX, posY, 1f, 0.5f, game);
        ratRun = Util.createTextureAnimation(6,1, run);

        body = createBody(posX,posY,0.3f);

        allowPlayerCollision();
        //ignorePlayerCollision();
        sound = audio.newSound(files.internal("rat.mp3"));

    }

    public void update () {
        super.update();
        move();
        seek();
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
        // ui healthbar
        game.healthbar.reduce();

        // delete rat
        game.toBeDeleted.add(this);
        game.toBeDeleted.add(objectToCarry);
        game.player.healthPoints--;

        // spawn new after previous dies
        spawnRat();

    }
    public void spawnRat() {
        callAfterPhysicsStep(() -> {
            float posY = 1f;
            float posX = 2f;

            Rat temp = new Rat(posX, posY, game);
            game.gameObjects.add(temp);

            return null;
        });
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

        speed = speed + (float)game.player.characterScore * 0.006f;
        // speed limit to 3f
        if (speed > 3f) {
            speed = 3f;
        }
        if (getX() < WINDOW_WIDTH / 2 && getX() >  1.3f) {
            if (isRight) {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
            }
        } else if(getX() > WINDOW_WIDTH / 2 && getX() < WINDOW_WIDTH - 1.3f) {
            if (isRight) {
                body.setLinearVelocity(speed, body.getLinearVelocity().y);
            } else {
                body.setLinearVelocity(-speed, body.getLinearVelocity().y);
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

        currentAnimation = ratRun;
        speed = 1f;

    }

    public void seek() {
        if (!isCarryingFlingable) {
            float distance = 50f;
            float right = distance;
            float left = distance;
            int i = 0;
            float tempRight = 0f;
            float tempLeft = 0f;
            for (GameObject obj : game.gameObjects) {
                if (obj != null && obj instanceof FlingableObject &&  obj instanceof Food && ((FlingableObject) obj).isOnFloor) {
                    if (obj.getX() > getX() && (obj.getX() - getX()) < distance) {
                        if (i == 0) {
                            right = obj.getX() - getX();
                        } else {
                            tempRight = obj.getX() - getX();
                            if (tempRight < right) {
                                right = tempRight;
                            }
                        }
                    }
                    if (obj.getX() < getX() && (getX() - obj.getX()) < distance) {
                        if (i == 0) {
                            left = getX() - obj.getX();
                        } else {
                            tempLeft = getX() - obj.getX();
                            if (tempLeft < left) {
                                left = tempLeft;
                            }
                        }
                    }
                }
                i++;
            }
            if (left > right) {
                if (!isRight) {
                    body.setLinearVelocity(speed, body.getLinearVelocity().y);
                    if (body.getLinearVelocity().x > 0) {
                        Util.flip(ratRun);
                        isRight = true;
                    }
                } else {
                    body.setLinearVelocity(speed, body.getLinearVelocity().y);
                    if (body.getLinearVelocity().x < 0) {
                        Util.flip(ratRun);
                        isRight = false;
                    }
                }
            }
            else if (left < right) {
                if (isRight) {
                    body.setLinearVelocity(-speed, body.getLinearVelocity().y);
                    if (body.getLinearVelocity().x < 0) {
                        Util.flip(ratRun);
                        isRight = false;
                    }
                } else {
                    body.setLinearVelocity(-speed, body.getLinearVelocity().y);
                    if (body.getLinearVelocity().x > 0) {
                        Util.flip(ratRun);
                        isRight = true;
                    }
                }
            }
        }
    }

    /***
     * Finds collisions with food that also implements flinable object
     * When collision is detected, attach the object to rat
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
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
                if (game.sounds) {
                    sound.play(0.1f);
                }
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



