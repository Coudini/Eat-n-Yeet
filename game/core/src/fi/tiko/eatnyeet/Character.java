package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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


    public Character(float posX, float posY, MainGame game) {
        super(posX, posY, 1f, 1f, game);
        characterRun = createTextureAnimation(4,2, run);
        characterIdle = createTextureAnimation(4,1,idle);
        body = createBody(posX,posY,0.5f);
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    @Override
    public void render(Batch batch) {
        move();
        stateTime += Gdx.graphics.getDeltaTime();

        // If character is moving use characterRun animation
        if (body.getLinearVelocity().x != 0) {
            currentFrameTexture = characterRun.getKeyFrame(stateTime, true);
        }
        // if not moving use idle animation
        else {
            currentFrameTexture = characterIdle.getKeyFrame(stateTime, true);
        }

        // render current image frame to body position
        batch.draw(currentFrameTexture, body.getPosition().x - 0.5f, body.getPosition().y - 0.5f, getWidth(), getHeight());

    }

    public void move() {
        float accY = Gdx.input.getAccelerometerY();
        float delta =  Gdx.graphics.getDeltaTime();
        float degrees = accY / 10 * 90;

        // keyboard section
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.setLinearVelocity(speed * delta,body.getLinearVelocity().y);
            if (isRight == false) {
                flip(characterIdle);
                flip(characterRun);
                isRight = true;
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.setLinearVelocity(-speed * delta,body.getLinearVelocity().y);
            if (isRight == true) {
                flip(characterIdle);
                flip(characterRun);
                isRight = false;
            }
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
    }
}
