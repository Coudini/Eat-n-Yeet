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
    private final float DEAD_ZONE = 5f;
    private final float speed = 300f;
    // used to keep track and flipping textures to right direction
    boolean isRight = true;

    private final static Texture run = new Texture("farma_run.png");
    private final static Texture idle = new Texture("farma_idle.png");

    Animation<TextureRegion> characterIdle;
    Animation<TextureRegion> characterRun;

    protected boolean isCarryingFlingable = false;
    protected boolean isJustThrown = false;
    protected float frameCount;

    GameObject objectToCarry;

    boolean startPosSet =false;
    Vector3 touchPosDrag;
    Vector3 endPosDrag;
    // used for detect if object can pass through other object


    public Character(float posX, float posY, MainGame game) {
        super(posX, posY, 1f, 1f, game);
        characterRun = createTextureAnimation(4,2, run);
        characterIdle = createTextureAnimation(4,1,idle);
        body = createBody(posX,posY,0.5f);

        allowPlayerCollision();


        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    public void update () {
        move();
        flingListener();
        updateObjectToCarry();

    }

    public void flingListener() {

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                if (!startPosSet) {
                    touchPosDrag = new Vector3(screenX, screenY, 0);
                    game.camera.unproject(touchPosDrag);
                    startPosSet = true;
                    System.out.println("homo");
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                endPosDrag = new Vector3(screenX, screenY, 0);
                game.camera.unproject(endPosDrag);
                float speedX = 0f;
                float speedY = 0f;

                try {
                    speedX = (touchPosDrag.x - endPosDrag.x) * 5;
                    speedY = (touchPosDrag.y - endPosDrag.y) * 5;
                } catch (Exception e) {
                    System.out.println("no drag");
                    return false;
                }
                System.out.println(speedX + " x");
                System.out.println(speedY + " y");


                if (isCarryingFlingable && startPosSet) {
                    // TODO Worldcenter needs to be changed to actual world center, currently from body
                    //objectToCarry.body.applyLinearImpulse(new Vector2(speedX, speedY), objectToCarry.body.getWorldCenter(), true);
                    objectToCarry.body.setLinearVelocity(speedX, speedY);
                    objectToCarry.body.applyAngularImpulse(Math.signum(speedX) * -0.01f, true);

                    startPosSet = false;
                    isCarryingFlingable = false;
                    isJustThrown = true;

                }
                return true;
            }
        });
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
                body.setLinearVelocity(0f,body.getLinearVelocity().y);
            }
        }

        if (body.getLinearVelocity().x > 0 && !isRight) {
            flip(characterIdle);
            flip(characterRun);
            isRight = true;
        }
        if (body.getLinearVelocity().x < 0 && isRight) {
            flip(characterIdle);
            flip(characterRun);
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
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {
        if (other != null && other instanceof Flingable) {
            if (!isJustThrown && !isCarryingFlingable) {

                objectToCarry = other;

                ignorePlayerCollision();

                isCarryingFlingable = true;
                // when colliding mask waste to ignore player collision
                ignorePlayerCollision();
            }
        }
    }


    private void updateObjectToCarry () {

        if (isCarryingFlingable) {
            float xModif = -0.5f;
            float yModif = 0.8f;
            if (isRight) {
                xModif = 0.5f;
            }
            // set body location to player body location -+ x and y modifiers
            objectToCarry.body.setTransform(this.body.getPosition().x + xModif,this.body.getPosition().y + yModif,0f);
        }

        // after set time allow character to interact with the object again
        if (isJustThrown) {
            frameCount++;
            if (frameCount > 20) {
                allowPlayerCollision();
                frameCount = 0;
                isJustThrown = false;
                isJustThrown = false;
            }
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
        filter.maskBits = DEFAULT_BITS | FOOD_BITS | COMPOST_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

}
