package fi.tiko.eatnyeet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class PauseButton extends Button {
    public static Texture pauseButtonTexture;


    public PauseButton (GameScreen screen) {
        super(pauseButtonTexture,0.4f,0.4f,screen);
        setPosition(15f, 8f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }
    @Override
    public void update () {
        super.update();

        clickListenerGameCamera();
        // when clicked play button start game
        if (isClicked) {
            System.out.println("Pause");
        }
    }
}
