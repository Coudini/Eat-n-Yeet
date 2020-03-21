package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Compost extends GameObject {
    private static Texture empty = new Texture("compost_empty.png");
    private static Texture fill1 = new Texture("compost_stage1.png");
    private static Texture fill2 = new Texture("compost_stage2.png");
    private static Texture fill3 = new Texture("compost_stage3.png");
    private static Texture fill4 = new Texture("compost_stage4.png");
    float fillLevel;
    float maxFill = 10f;

    public Compost(float width, float height,Body body , MainGame game) {
        super(fill4, width,height, body, game);
        fillLevel = 0f;
    }

    @Override
    public void update () {
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
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {

        if (other != null && other instanceof Food) {
            game.toBeDeleted.add(other);
            fillLevel += ((Food) other).getFillAmount();
            if (fillLevel >= maxFill) {
                fillLevel = maxFill;
                System.out.println("Field already full!!");
            }
            System.out.println("Compost filllevel = " +  fillLevel);
        }

        if (other != null && other instanceof Character && fillLevel > 0f) {
            System.out.println("character detected");

            // cannot add object during physics steps so it will be added later
           callAfterPhysicsStep(() -> {
               float posX = game.player.body.getPosition().x;
               float posY = game.player.body.getPosition().y + 1f;
               float fill = fillLevel;

               if (fillLevel >= maxFill) {
                   fill = maxFill;
               }

               CompostWaste temp = new CompostWaste(posX,posY,fill, game);
               game.gameObjects.add(temp);
               fillLevel = 0f;
               return null;
           });
        }

    }
}
