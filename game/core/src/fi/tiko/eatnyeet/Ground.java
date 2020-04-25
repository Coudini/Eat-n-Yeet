package fi.tiko.eatnyeet;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

public class Ground extends GameObject {

    Sound sound;


    public Ground (float width,float height, Body body, GameScreen game) {
        super(width,height,body,game);
        sound = audio.newSound(files.internal("HitWall.mp3"));
    }



    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof FlingableObject) {
            ((FlingableObject) other).flyTime = 0f;
            ((FlingableObject) other).isOnFloor = true;
            game.player.resetCombo();
            game.player.resetScoreHandlers();
            sound.play();
        }
    }
    @Override
    public void endCollision (Contact contact, GameObject other) {
        if (other != null && other instanceof FlingableObject) {
            ((FlingableObject) other).flyTime = 0f;
            ((FlingableObject) other).isOnFloor = false;
        }
    }
}
