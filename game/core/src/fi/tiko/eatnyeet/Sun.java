package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sun extends GraphicObject {

    public static Texture sunNoDisco;
    public static Texture sunDisco;

    Animation<TextureRegion> sun;
    Animation<TextureRegion> discoSun;

    public Sun(MainGame game) {
        super(game);
        sun = Util.createTextureAnimation(12,1, sunNoDisco);
        discoSun = Util.createTextureAnimation(12,1,sunDisco);
        setSize(2f,2f);
        setX(0.5f);
        setY(7f);
    }
    public void update() {
        super.update();
        checkDisco();
    }
    public void checkDisco() {
        // discomode based on combo-points
        if (game.player.characterCombo > 1) {
            currentAnimation = discoSun;
        } else {
            currentAnimation = sun;
        }
    }
}
