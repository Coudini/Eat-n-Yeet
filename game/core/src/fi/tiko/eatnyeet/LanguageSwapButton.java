package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class LanguageSwapButton extends Button {
    public static Texture buttonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public LanguageSwapButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 0.8f,buttonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH * 0.9f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 100f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    /**
     * Default update for buttons, methods that all buttons need to call will be added here. Calling superclass update is a must.
     * Changes languages between Finnish and English.
     */
    @Override
    public void update () {
        super.update();
        if (isClicked) {
            mainGame.changeLocale();
            StartScreen temp = new StartScreen(mainGame.batch, mainGame);
            mainGame.setScreen(temp);
            isClicked = false;
        }
    }

}
