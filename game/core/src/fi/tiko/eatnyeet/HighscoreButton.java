package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class HighscoreButton extends Button {
    public static Texture highscoreButtonTexture;

    /**
     * Constructor with default values
     * @param mainGame saved to superclass
     */
    public HighscoreButton (MainGame mainGame) {
        super(highscoreButtonTexture,highscoreButtonTexture.getWidth() * 0.8f,highscoreButtonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 520f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    /**
     * Updates object, calls methods and does if handling.
     */
    @Override
    public void update () {
        super.update();

        if (isClicked) {
            mainGame.highScoreScreen = new HighScoreScreen(mainGame.batch, mainGame);
            mainGame.setScreen(mainGame.highScoreScreen);
            isClicked = false;
        }
    }
}
