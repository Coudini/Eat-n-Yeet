package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

public class Character extends GameObject {
    // Limit for mobile accelerometer
    private final float DEAD_ZONE = 10f;
    private final float speed = 300f;
    protected int characterScore =0;
    private int previousScore = 0;
    protected int characterCombo = 0;
    private int previousCombo = 0;
    protected int healthPoints = 3;
    // used to keep track and flipping textures to right direction
    boolean isRight = true;

    //boolean jump = false;
    private float maxStr = 10f;

    public static Texture run;
    public static Texture idle;

    Animation<TextureRegion> characterIdle;
    Animation<TextureRegion> characterRun;

    protected FlingableObject objectToCarry;
    boolean isCarryingFlingable = false;

    public  boolean startPosSet =false;
    Vector3 touchPosDrag;
    Vector3 endPosDrag;
    // used for detect if object can pass through other object


    // for force-meter
    public boolean meterStart = false;
    public double angle;
    public float angleX;
    public float angleY;
    public float meterX;
    public float meterY;
    public float meter1x;
    public float meter1y;
    public float meter2x;
    public float meter2y;
    public float meter3x;
    public float meter3y;
    public InputAdapter characterInput;

    public Sound pick;
    public Sound yeet;

    public Character(float posX, float posY, GameScreen game) {
        super(posX, posY, 1.9f, 1.9f, game);
        characterRun = Util.createTextureAnimation(4,2, run);
        characterIdle = Util.createTextureAnimation(4,1,idle);
        body = createBody(posX,posY,0.95f);
        allowPlayerCollision();

        pick = audio.newSound(files.internal("PickUp.mp3"));
        yeet = audio.newSound(files.internal("Yeet1.mp3"));
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));


        characterInput = new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                if (!startPosSet) {
                    touchPosDrag = new Vector3(screenX, screenY, 0);
                    // :D
                    game.mainGame.camera.unproject(touchPosDrag);
                    startPosSet = true;

                }

                //ForceMeter
                if (!meterStart) {
                    //starting coordinates
                    meter1x = screenX;
                    meter1y = screenY;
                    meterStart = true;
                    ForceMeter.show(true);
                }
                //updating coordinates
                meter2x = screenX;
                meter2y = screenY;
                //distance and angles between coordinates
                if (meter1x > meter2x) {
                    meter3x = (meter1x - meter2x) * 0.8f;
                    meterX = (getX() + 0.66f) - (meter3x / 150f);
                } else {
                    meter3x = (meter2x - meter1x) * 0.8f;
                    meterX = (getX() + 0.66f) + (meter3x / 150f);
                }
                if (meter1y > meter2y) {
                    meter3y = (meter1y - meter2y) * 0.8f;
                    meterY = getY() + (meter3y / 150f);
                } else {
                    meter3y = (meter2y - meter1y) * 0.8f;
                    meterY = getY() - (meter3y / 150f);
                }
                angleX = meter2x - meter1x;
                angleY = meter2y - meter1y;
                angle = Math.atan2((double)angleY, (double)angleX);
                if (angle < 0) {
                    angle = Math.abs(angle);
                } else {
                    angle = 2*Math.PI - angle;
                }
                angle = Math.toDegrees(angle);
                setXYA(meterX, meterY, angle);

                return false;
            }
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPosDrag = new Vector3(screenX, screenY, 0);
                game.mainGame.camera.unproject(endPosDrag);
                float speedX = 0f;
                float speedY = 0f;
                try {
                    speedX = (touchPosDrag.x - endPosDrag.x) * 4;
                    speedY = (touchPosDrag.y - endPosDrag.y) * 4;
                } catch (Exception e) {
                    return false;
                }
                if (speedX > maxStr) {
                    speedX = maxStr;
                }
                if (speedX < (0f - maxStr)) {
                    speedX = 0f - maxStr;
                }
                if (speedY > maxStr) {
                    speedY = maxStr;
                }
                if (speedY < (0f - maxStr)) {
                    speedY = 0f - maxStr;
                }

                if (isCarryingFlingable && startPosSet) {
                    throwObjectToCarry(speedX,speedY);
                    if (game.sounds) {
                        yeet.play();
                    }
                }
                meterStart = false;
                ForceMeter.show(false);
                return false;
            }

        };
    }


    public void update () {
        super.update();
        move();
        //flingListener();
        updateObjectToCarry();
        updateScoreAndCombo();
    }


    public void setXYA(float X, float Y, double A){
        ForceMeter.setXY(X, Y);
        ForceMeter.setRotate(A);
    }
    public void updateScoreAndCombo() {
        if (characterScore != previousScore) {
            //System.out.println("Current score = " + characterScore);
            previousScore = characterScore;
        }
        if (characterCombo != previousCombo) {
            //System.out.println("Current combo = " + characterCombo);
            previousCombo = characterCombo;
        }
    }
    public void resetScoreHandlers() {
        previousCombo = 0;
        previousScore = 0;
    }
    public void resetCombo() {
        characterCombo = 0;
    }



    public void throwObjectToCarry (float speedX,float speedY) {
        objectToCarry.body.setLinearVelocity(speedX, speedY);
        objectToCarry.body.applyAngularImpulse(Math.signum(speedX) * -0.1f, true);

        startPosSet = false;
        isCarryingFlingable = false;
        allowPlayerCollision();

        objectToCarry.timeWhenThrown = objectToCarry.lifeTime;
        objectToCarry.isBeingCarried = false;
        objectToCarry.isJustThrown = true;
        if (objectToCarry.body.getGravityScale() < 1f) {
            objectToCarry.resetGravityScale();
        }
    }

    public void move() {

        float accY = Gdx.input.getAccelerometerY();
        float delta =  Gdx.graphics.getDeltaTime();
        float degrees = accY / 10 * 90;

        // keyboard section
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(speed * delta,body.getLinearVelocity().y);
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(-speed * delta,body.getLinearVelocity().y);
        }

        // mobile section
        else {
            if ( degrees > DEAD_ZONE) {
                body.setLinearVelocity(speed * delta,body.getLinearVelocity().y);
            } else if (degrees < -DEAD_ZONE) {
                body.setLinearVelocity(-speed * delta,body.getLinearVelocity().y);
            } else {
                // reduce the x velocity step by step
                float xVel = body.getLinearVelocity().x;
                if (xVel > 0.4f || xVel < -0.4f) {
                    body.setLinearVelocity(xVel * 0.7f,body.getLinearVelocity().y);
                } else {
                    body.setLinearVelocity(0f,body.getLinearVelocity().y);
                }
            }
        }

        if (body.getLinearVelocity().x > 0 && !isRight) {
            Util.flip(characterIdle);
            Util.flip(characterRun);
            isRight = true;
        }
        if (body.getLinearVelocity().x < 0 && isRight) {
            Util.flip(characterIdle);
            Util.flip(characterRun);
            isRight = false;
        }

        // If character is moving use characterRun animation
        if (body.getLinearVelocity().x != 0) {
            currentAnimation = characterRun;
        }
        // if not moving use idle animation
        else {
            currentAnimation = characterIdle;

        }
     }

    @Override
    public void onCollision(Contact contact, GameObject other) {
        if (other != null && other instanceof FlingableObject) {
            if (!isCarryingFlingable) {

                objectToCarry = (FlingableObject) other;
                objectToCarry.isBeingCarried = true;
                isCarryingFlingable = true;
                // when colliding mask waste to ignore player collision
                ignorePlayerCollision();
                objectToCarry.ignorePlayerCollision();
                if (game.sounds) {
                    pick.play();
                }
            }
        }
    }


    private void updateObjectToCarry () {

        if (isCarryingFlingable) {
            float xModif = -0.37f;
            float yModif = 0.045f;
            if (isRight) {
                xModif = 0.37f;
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

    /**
     * called when manually carrying objects to compost
     */
    protected void resetObjectToCarry() {
       isCarryingFlingable = false;
       objectToCarry = null;
       allowPlayerCollision();
    }
    public int getScore () {
        return characterScore;
    }
    public int getCombo() {
        return characterCombo;
    }
}
