package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class UpdateNameButton extends Button {
    public static Texture buttonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame
     */
    public UpdateNameButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 0.8f,buttonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT - 100f - buttonTexture.getHeight() * 0.8f / 2);
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
            mainGame.highScoreScreen.createNewScore();
        }
    }

}
