package fi.tiko.eatnyeet;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;


public class CompostWaste extends FlingableObject {

    public static Texture texture;

    public CompostWaste(float x, float y,float fill, GameScreen game) {
        super(texture, x,y, 1f, 1f, game);
        this.fillAmount = fill;
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.5f);
        body = createBody(x,y,0.3f);
        allowPlayerCollision();
    }

    @Override
    public void update() {
        super.update();
        flyTimeUpdate();
    }
}
