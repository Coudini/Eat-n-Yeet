package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class RetryButton extends Button {
    public static Texture retryButtonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public RetryButton (MainGame mainGame) {
        super(retryButtonTexture,retryButtonTexture.getWidth(),retryButtonTexture.getHeight(),mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 370f - getHeight() / 2f);
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
            isClicked = false;
            mainGame.gameScreen = new GameScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.gameScreen);
        }
    }
}
