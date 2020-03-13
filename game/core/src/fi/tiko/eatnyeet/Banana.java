package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Banana extends GameObject implements Flingable {
    private static Texture texture = new Texture("banana.png");
    public static final short DEFAULT_BITS = 0x0001;
    public static final short PLAYER_BITS = 0x0002;
    public static final short ENEMY_CATEGORY_BITS = 0x0004;
    public static final short FOOD_BITS = 0x0008;

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.5f, 0.5f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.15f);

        Filter filter = new Filter();
        filter.categoryBits = FOOD_BITS;
        filter.maskBits = DEFAULT_BITS | FOOD_BITS;

        for (Fixture fix: body.getFixtureList()) {

            fix.setFilterData(filter);
        }
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    public float getFillAmount() {
        return 1f;
    }
}
