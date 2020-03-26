package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
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

public class Character extends GameObject {
    // Limit for mobile accelerometer
    private final float DEAD_ZONE = 10f;
    private final float speed = 300f;
    protected int characterScore = 0;
    private int previousScore = 0;
    protected int characterCombo = 0;
    private int previousCombo = 0;
    // used to keep track and flipping textures to right direction
    boolean isRight = true;

    //boolean jump = false;
    private float maxStr = 10f;

    public  static Texture run;
    public  static Texture idle;

    Animation<TextureRegion> characterIdle;
    Animation<TextureRegion> characterRun;

    protected FlingableObject objectToCarry;
    protected boolean isCarryingFlingable = false;

    public static boolean startPosSet =false;
    Vector3 touchPosDrag;
    Vector3 endPosDrag;
    // used for detect if object can pass through other object


    // for force-meter
    public static float pX;
    public static boolean carry;
    public static boolean startDrag;
    public static boolean initial;
    public static float meter1x;
    public static float meter1y;
    public static float meter2x;
    public static float meter2y;
    public static float angle;


    public Character(float posX, float posY, MainGame game) {
        super(posX, posY, 1.9f, 1.9f, game);
        characterRun = Util.createTextureAnimation(4,2, run);
        characterIdle = Util.createTextureAnimation(4,1,idle);
        body = createBody(posX,posY,0.95f);
        startDrag = false;
        initial = true;
        allowPlayerCollision();

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    public void update () {
        super.update();
        move();
        flingListener();
        updateObjectToCarry();
        printScoreAndCombo();
        if(isCarryingFlingable) {
            upX();
            upAngle();
        }
        carry = isCarryingFlingable;
    }

    public void upX() {
        pX = getX();
    }
    public void upAngle() {

    }

    public void printScoreAndCombo() {
        if (characterScore != previousScore) {
            System.out.println("Current score = " + characterScore);
            previousScore = characterScore;
        }
        if (characterCombo != previousCombo) {
            System.out.println("Current combo = " + characterCombo);
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
    public void flingListener() {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                if (!startPosSet) {
                    touchPosDrag = new Vector3(screenX, screenY, 0);
                    game.camera.unproject(touchPosDrag);
                    startPosSet = true;

                    startDrag = true;
                    if (startDrag && initial) {
                        meter1x = screenX;
                        meter1y = screenY;
                        //System.out.println("1x:                " + meter1x);
                        //System.out.println("1y:                " + meter1y);
                        startDrag = false;
                        initial = false;
                    }
                }
                meter2x = screenX;
                meter2y = screenY;
                angle = meter2y;
                //System.out.println("2x: " + meter2y);
                //System.out.println("2y: " + meter2y);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPosDrag = new Vector3(screenX, screenY, 0);
                game.camera.unproject(endPosDrag);
                float speedX = 0f;
                float speedY = 0f;


                    try {
                        speedX = (touchPosDrag.x - endPosDrag.x) * 4;
                        speedY = (touchPosDrag.y - endPosDrag.y) * 4;
                    } catch (Exception e) {
                        System.out.println("no drag");
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
                    //if ((speedX < 1.3f && speedX > -1.3f) && (speedY < 1.3f && speedY > -1.3f)) {
                    //    startPosSet = false;
                    //    jump = true;
                    //}


                if (isCarryingFlingable && startPosSet) {
                    throwObjectToCarry(speedX,speedY);

                    initial = true;

                //} else {
                //    if (jump && body.getLinearVelocity().y == 0f) {
                //        body.setLinearVelocity(0f, 5f);
                //        jump = false;
                //    }
                }
                return true;
            }
        });
    }

    public void throwObjectToCarry (float speedX,float speedY) {
        objectToCarry.body.setLinearVelocity(speedX, speedY);
        objectToCarry.body.applyAngularImpulse(Math.signum(speedX) * -0.3f, true);

        startPosSet = false;
        isCarryingFlingable = false;
        allowPlayerCollision();

        objectToCarry.timeWhenThrown = objectToCarry.lifeTime;
        objectToCarry.isBeingCarried = false;
        objectToCarry.isJustThrown = true;
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
}
