package fi.tiko.eatnyeet;


import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

public class Ground extends GameObject {


    public Ground (float width,float height, Body body, MainGame game) {
        super(width,height,body,game);
    }



    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof Flingable) {
            other.flyTime = 0f;
            other.isOnFloor = true;
            game.player.resetCombo();
            game.player.resetScoreHandlers();
        }
    }
    @Override
    public void endCollision (Contact contact, GameObject other) {
        if (other != null && other instanceof Food) {
            other.flyTime = 0f;
            other.isOnFloor = false;
        }
    }
}
