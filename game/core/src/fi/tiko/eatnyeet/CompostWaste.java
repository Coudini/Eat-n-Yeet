package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;

public class CompostWaste extends GameObject {

    private static Texture waste = new Texture("temp_compost_stuff.png");

    public CompostWaste(float x, float y, MainGame game) {
        super(waste, x,y, 1f, 1f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(x,y,0.3f);
    }

    @Override
    public void update() {
        trackPlayer();
        move();
    }

    private void trackPlayer() {

    }

    public void move () {
        if (Gdx.input.justTouched()) {

            int realX = Gdx.input.getX();
            int realY = Gdx.input.getY();
            Vector3 touchPos = new Vector3(realX, realY, 0);
            game.camera.unproject(touchPos);

            float speedX = (touchPos.x - body.getPosition().x) / 5;
            float speedY = (touchPos.y - body.getPosition().y) / 5;

            body.applyLinearImpulse(new Vector2(speedX,speedY),body.getWorldCenter(),true);
            body.applyAngularImpulse(Math.signum(speedX)*-0.01f,true);
        }
    }
}
