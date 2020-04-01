package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class Carrot extends FlingableObject implements Food {

    public static Texture texture;

    private boolean spawnComplete = false;

    public Carrot(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.9f, 0.9f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.4f);
        body = createBody(posX,posY,0.45f);
        allowPlayerCollision();
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }
    public Carrot(float posX, float posY,float radius, MainGame game) {
        super(texture, posX, posY, 0.6f, 0.6f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.4f);
        body = createBody(posX,posY,radius);
        allowPlayerCollision();
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    @Override
    public void update() {
        super.update();

        if (!spawnComplete) {
            if (isBeingCarried || isOnFloor) {
                spawnComplete = true;
                resetGravityScale();
            }
        }
    }
}
