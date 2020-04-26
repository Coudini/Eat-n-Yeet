package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class TutorialButton extends Button {
    public static Texture tutorialButtonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame
     */
    public TutorialButton (MainGame mainGame) {
        super(tutorialButtonTexture,tutorialButtonTexture.getWidth() * 0.8f,tutorialButtonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT - 410f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    /**
     * Default update for buttons, methods that all buttons need to call will be added here. Calling superclass update is a must.
     * Sets the screen to tutorial screen
     */
    @Override
    public void update () {
        super.update();
        if (isClicked) {
            isClicked = false;
            TutorialScreen tutScreen = new TutorialScreen(mainGame.batch,mainGame);
            mainGame.setScreen(tutScreen);
        }
    }
}

