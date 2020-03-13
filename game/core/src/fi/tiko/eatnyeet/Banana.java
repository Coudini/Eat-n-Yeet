package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

public class Banana extends GameObject implements Flingable {
    private static Texture texture = new Texture("banana.png");

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.5f, 0.5f, game);
        setDensity(0.7f);
        setFriction(1.5f);
        setRestitution(0.5f);
        body = createBody(posX,posY,0.15f);
        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));

    }

    @Override
    public void onCollision(Contact contact, Manifold oldManifold, GameObject other) {

        System.out.println("homo");
        if (other != null && other instanceof Compost) {
            System.out.println("penis");
            game.removalBodies.add(body);
            game.gameObjects.remove(this);



            // näillä voi viitata toisen luokan metodeihin yms muuttujiin
            //Compost comp = (Compost) other;
            //comp.test();
        }

    }
}
