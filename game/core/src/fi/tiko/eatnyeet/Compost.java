package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Compost extends GameObject {
    public static Texture empty;
    public static Texture fill1;
    public static Texture fill2;
    public static Texture fill3;
    public static Texture fill4;
    float fillLevel;
    float maxFill = 10f;

    /**
     * "Default constructor" for the class, uses default values since it should be only created only one time in the program.
     * @param width size
     * @param height size
     * @param body saved to superclass for collision detection
     * @param game saved to superclass
     */
    public Compost(float width, float height,Body body , GameScreen game) {
        super(fill4, width,height, body, game);
        fillLevel = 0f;
        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = DEFAULT_BITS | FLINGABLE_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    /**
     * Called on every iteration, calls methods and does if checking needed for the class.
     */
    @Override
    public void update () {
        super.update();
        float currentPercent = fillLevel / maxFill;
        if (currentPercent < 0.05) {
            this.setTexture(empty);
        } else if (currentPercent < 0.25) {
            this.setTexture(fill1);
        } else if (currentPercent < 0.50) {
            this.setTexture(fill2);
        } else if (currentPercent < 0.75) {
            this.setTexture(fill3);
        } else {
            this.setTexture(fill4);
        }
    }

}
