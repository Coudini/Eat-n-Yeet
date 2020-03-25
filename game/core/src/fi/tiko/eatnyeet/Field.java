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
    public static Texture fill5;
    public static Texture fill6;
    public static Texture fill7;
    public static Texture fill8;
    public static Texture fill9;
    float fillLevel;
    float maxFill = 10f;
    float reduceFill;

    public Field(float width, float height, Body body , MainGame game) {
        super(empty, width,height, body, game);
        fillLevel = 5f;
        reduceFill = 0f;
    }

    @Override
    public void update () {
        super.update();

        float currentPercent = fillLevel / maxFill;



        if (currentPercent > 0.05f) {
            cropCrops();
        }

        if (currentPercent < 0.05) {
            this.setTexture(empty);
        } else if (currentPercent < 0.15) {
            this.setTexture(fill1);
        } else if (currentPercent < 0.25) {
            this.setTexture(fill2);
        } else if (currentPercent < 0.35) {
            this.setTexture(fill3);
        } else if (currentPercent < 0.45) {
            this.setTexture(fill4);
        }else if (currentPercent < 0.55) {
            this.setTexture(fill5);
        }else if (currentPercent < 0.65) {
            this.setTexture(fill6);
        }else if (currentPercent < 0.75) {
            this.setTexture(fill7);
        }else if (currentPercent < 0.85) {
            this.setTexture(fill8);
        } else {
            this.setTexture(fill9);
        }
        //System.out.println("Current% " + currentPercent + ", fill " + fillLevel + ", max " + maxFill);


    }

    public void cropCrops() {
        reduceFill += lifeTime;
        if(reduceFill > 5000f) {
            reduceFill = 0f;
            fillLevel -= 1f;
        }
    }
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof CompostWaste && other instanceof FlingableObject) {
            game.toBeDeleted.add(other);


            // if character carries the object to field this will reset character object reference and booleans
            if (((CompostWaste) other).isBeingCarried) {
                callAfterPhysicsStep(() -> {
                    ((CompostWaste) other).isBeingCarried = false;
                    game.player.resetObjectToCarry();
                    return null;
                });
            }
            if (fillLevel >= maxFill) {
                //System.out.println("Field already full!!");
            } else {
                fillLevel += ((CompostWaste) other).getFillAmount();
                game.player.characterScore += (int) ((CompostWaste) other).flyTime * game.player.characterCombo;
                game.player.characterCombo += 1;
            }

            //System.out.println("Field fillevel = " + fillLevel);
        }



    }
}
