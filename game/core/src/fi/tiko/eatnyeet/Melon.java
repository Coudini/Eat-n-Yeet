package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class Melon extends FlingableObject implements Food {
    public static Texture texture1;
    public static Texture texture2;
    public static Texture melonNoDisco;
    public static Texture melonDisco;
    Animation<TextureRegion> melon;
    Animation<TextureRegion> discoMelon;

    public boolean eaten;
    public static Texture melonEaten;


    public Melon(float posX, float posY, GameScreen game) {
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
    public Melon(float posX, float posY, float radius, GameScreen game) {
        super(texture1, posX, posY, 0.6f, 0.6f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.4f);
        body = createBody(posX,posY,radius);
        allowPlayerCollision();
        melon = Util.createTextureAnimation(1,2, melonNoDisco);
        discoMelon = Util.createTextureAnimation(3,3,melonDisco);
        eaten = false;
        
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    @Override
    public void update() {
        super.update();

        if (getTexture().equals(melonEaten)) {
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
            currentAnimation = discoMelon;
            setTexture(texture2);
        } else {
            currentAnimation = melon;
            setTexture(texture1);
        }
    }

}
