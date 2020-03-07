package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Character extends GameObject {
    private final float DEAD_ZONE = 5f;
    private final float speed = 300f;

    private final static Texture texture = new Texture("chad.png");

    public Character(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 1f, 1f, game);
        body = createBody(posX,posY,0.5f);
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));

    }

    @Override
    public void update() {
        move();
    }


    @Override
    public void render(Batch batch) {
        super.render(batch);
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
        } else {
            // mobile section
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
