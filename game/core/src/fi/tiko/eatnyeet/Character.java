package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

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

    // used for detect if object can pass through other object
    public static final short DEFAULT_BITS = 0x0001;
    public static final short PLAYER_BITS = 0x0002;
    public static final short ENEMY_BITS = 0x0004;
    public static final short FOOD_BITS = 0x0008;

    public Character(float posX, float posY, MainGame game) {
        super(posX, posY, 1f, 1f, game);
        characterRun = createTextureAnimation(4,2, run);
        characterIdle = createTextureAnimation(4,1,idle);
        body = createBody(posX,posY,0.5f);


        Filter filter = new Filter();
        filter.categoryBits = PLAYER_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    public void update () {
        move();
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
                body.setLinearVelocity(0f,0f);
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
}
