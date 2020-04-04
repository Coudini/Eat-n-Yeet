package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Carrot extends FlingableObject implements Food {

    public static Texture texture1;
    public static Texture texture2;

    public static Texture carrotNoDisco;
    public static Texture carrotDisco;

    Animation<TextureRegion> carrot;
    Animation<TextureRegion> discoCarrot;

    public boolean eaten;



    public Carrot(float posX, float posY, MainGame game) {
        super(texture1, posX, posY, 0.3f, 0.3f, game);
        setSize(0.6f,0.6f);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.4f);
        body = createBody(posX,posY,0.3f);
        allowPlayerCollision();
        eaten = false;

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }
    public Carrot(float posX, float posY,float radius, MainGame game) {
        super(texture1, posX, posY, 0.6f, 0.6f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.4f);
        body = createBody(posX,posY,radius);
        allowPlayerCollision();
        carrot = Util.createTextureAnimation(4,1, carrotNoDisco);
        discoCarrot = Util.createTextureAnimation(4,1,carrotDisco);
        eaten = false;

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }

    @Override
    public void update() {
        super.update();

        if (getTexture().equals(Customer.carrotEaten)) {
            eaten = true;
        }
        if (!eaten) {
            checkDisco();
        }
        if (isOnFloor && body.getGravityScale() < 1f) {
            resetGravityScale();
        }
    }

    public void checkDisco(){
        if (game.player.characterCombo > 1) {
            currentAnimation = discoCarrot;
            setTexture(texture2);
        } else {
            currentAnimation = carrot;
            setTexture(texture1);
        }
    }
}
