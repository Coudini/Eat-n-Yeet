package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class PauseButton extends Button {
    public static Texture pauseButtonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public PauseButton (MainGame mainGame) {
        super(pauseButtonTexture,0.4f,0.4f,mainGame);
        setPosition(15f, 8f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }

    /**
     * Default update for buttons, methods that all buttons need to call will be added here. Calling superclass update is a must.
     */
    @Override
    public void update () {
        super.update();

        if (isClicked) {
            mainGame.gameScreen.pause();
            if (mainGame.gameScreen.sounds) {
                mainGame.gameScreen.song.pause();
            }
            isClicked = false;
            PauseScreen temp = new PauseScreen(mainGame.batch,mainGame);
            mainGame.setScreen(temp);
        }
    }
}
