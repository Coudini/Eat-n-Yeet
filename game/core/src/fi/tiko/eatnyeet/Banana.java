package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Banana extends FlingableObject implements Food {
    public static Texture texture;

    private boolean spawnComplete = false;

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.9f, 0.9f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.2f);
        body = createBody(posX,posY,0.45f);
        float randY = MathUtils.random(2f,4f);
        float randX = MathUtils.random(-3f,3f);
        body.setLinearVelocity(randX,randY);
        body.setGravityScale(0.2f);
        body.setUserData(this);
        allowPlayerCollision();

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    @Override
    public void update() {
        super.update();

        if (!spawnComplete) {
            if (isBeingCarried || isOnFloor) {
                spawnComplete = true;
                resetGraityScale();
            }
        }
    }

}
