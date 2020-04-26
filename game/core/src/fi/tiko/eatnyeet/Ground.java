package fi.tiko.eatnyeet;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

public class Ground extends GameObject {

    Sound sound;


    /**
     * Consructor
     * @param width size
     * @param height size
     * @param body body
     * @param game gamescreen whichs needs to be passed to superclass
     */
    public Ground (float width,float height, Body body, GameScreen game) {
        super(width,height,body,game);
        sound = audio.newSound(files.internal("HitWall.mp3"));
    }


    /**
     *  When 2 bodies collides, onCollision is automatically called. Overide onCollision to do class spesific tasks
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof FlingableObject) {
            ((FlingableObject) other).flyTime = 0f;
            ((FlingableObject) other).isOnFloor = true;
            game.player.resetCombo();
            game.player.resetScoreHandlers();
            if (game.sounds) {
                sound.play();
            }
        }
    }

    /**
     *  When 2 bodies collideing ends, endCollision is automatically called. Overide endCollision to do class spesific tasks
     * @param contact
     * @param other can be used to check what class it was colliding with
     */
    @Override
    public void endCollision (Contact contact, GameObject other) {
        if (other != null && other instanceof FlingableObject) {
            ((FlingableObject) other).flyTime = 0f;
            ((FlingableObject) other).isOnFloor = false;
        }
    }
}
