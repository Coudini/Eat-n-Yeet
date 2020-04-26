package fi.tiko.eatnyeet;

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

    /**
     * Constructor, spawns a rat in the game with textures, position, body and sound-effect.
     * @param posX position
     * @param posY position
     * @param game needed to save to superclass
     */
    public Rat (float posX, float posY, GameScreen game) {
        super(posX, posY, 1f, 0.5f, game);
        ratRun = Util.createTextureAnimation(6,1, run);
        body = createBody(posX,posY,0.3f);
        allowPlayerCollision();
        sound = audio.newSound(files.internal("rat.mp3"));
    }

    /**
     * Update is called on every iteration, calls method and does if checking that is needed on each iteration.
     */
    public void update () {
        super.update();
        move();
        seek();
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
        }
    }

    /**
     * Destroys the rat and adjusts health-bar and health-points. Calls a spawn for a new rat
     */
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

    /**
     * Creates a new rat.
     */
    public void spawnRat() {
        callAfterPhysicsStep(() -> {
            float posY = 1f;
            float posX = 2f;
            Rat temp = new Rat(posX, posY, game);
            game.gameObjects.add(temp);
            return null;
        });
    }

    private float tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));

    /**
     * Contains the functions for basic movements for the rat including speed and direction.
     */
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
            body.applyLinearImpulse(new Vector2(temp, 0f), body.getWorldCenter(), true);

        }
        if (body.getLinearVelocity().x > 0 && !isRight) {
            Util.flip(ratRun);
            isRight = true;
            tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));
        }
        if (body.getLinearVelocity().x < 0 && isRight) {
            Util.flip(ratRun);
            isRight = false;
            tempX = MathUtils.random (1.3f,(WINDOW_WIDTH - 1.3f));
        }
        currentAnimation = ratRun;
        speed = 1f;
    }

    /**
     * Adjusts the rats movement and direction based on a nearest object the rat can interact with.
     */
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

    /**
     * Keeps the object that is being carried at rats "mouth"
     */
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

    /**
     * Can be used to ignore collision with everything else than default objects
     */
    public void ignorePlayerCollision() {
        Filter filter = new Filter();
        filter.categoryBits = PLAYER_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    /**
     * Returns default collision settings and makes rat collide with everything except character
     */
    public void allowPlayerCollision(){
        Filter filter = new Filter();
        filter.categoryBits = PLAYER_BITS;
        filter.maskBits = DEFAULT_BITS | FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    /**
     * Lets rat fall through the ground
     */
    public void ignoreGroundCollision() {
        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
}



