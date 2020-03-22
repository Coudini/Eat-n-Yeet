package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Field extends GameObject {

    // temp
    public static Texture empty;
    public static Texture fill1;
    public static Texture fill2;
    public static Texture fill3;
    public static Texture fill4;
    float fillLevel;
    float maxFill = 10f;

    public Field(float width, float height, Body body , MainGame game) {
        super(fill4, width,height, body, game);
        fillLevel = 0f;
    }

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
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof CompostWaste) {
            game.toBeDeleted.add(other);
            fillLevel += ((CompostWaste) other).getFillAmount();

            // if character carries the object to field this will reset character object reference and booleans
            if (other.isBeingCarried) {
                callAfterPhysicsStep(() -> {
                    other.isBeingCarried = false;
                    game.player.resetObjectToCarry();
                    return null;
                });
            }
            if (fillLevel >= maxFill) {
                fillLevel = maxFill;
                System.out.println("Field already full!!");
            }

            System.out.println("Field fillevel = " + fillLevel);
        }



    }
}
