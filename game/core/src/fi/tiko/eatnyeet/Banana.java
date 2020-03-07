package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Banana extends GameObject implements Flingable {
    private final static Texture texture = new Texture("banana.png");

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 1f, 1f, game);
        setDensity(0.1f);
        setFriction(0.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.5f);
        body.setUserData("flingable");
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));

    }

    @Override
    public void render(Batch batch) {

        // call render as last
        super.render(batch);
    }

    public void update () {
        if (body == null) {
            texture.dispose();
        }
    }
}
