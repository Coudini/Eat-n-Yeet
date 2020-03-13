package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;

public class Compost extends GameObject {
    private static Texture texture = new Texture("compost_stage4.png");

    public Compost(float width, float height,Body body , MainGame game) {
        super(texture, width,height, body, game);

    }

    public void test () {

    }
}
