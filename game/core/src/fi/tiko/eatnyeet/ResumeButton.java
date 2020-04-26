package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ResumeButton extends Button {
    public static Texture buttonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public ResumeButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 1.2f,buttonTexture.getHeight() * 1.2f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2 - buttonTexture.getWidth()  * 1.2f / 2, 350f);
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
            mainGame.gameScreen.resume();
            if (mainGame.gameScreen.sounds) {
                mainGame.gameScreen.song.play();
            }
            isClicked = false;
            mainGame.setScreen(mainGame.gameScreen);
            // add gamescreen input prosessor again in use
            Gdx.input.setInputProcessor(mainGame.gameScreen.multiplexer);
        }
    }
}
