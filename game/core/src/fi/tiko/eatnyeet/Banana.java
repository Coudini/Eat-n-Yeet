package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Banana extends GameObject implements Flingable {
    private static Texture texture = new Texture("banana.png");

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.3f, 0.3f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.15f);
        body.setUserData("flingable");
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));

    }

    @Override
    public void render(Batch batch) {

        // call render as last
        super.render(batch);
    }
}
