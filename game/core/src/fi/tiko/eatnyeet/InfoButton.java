package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class InfoButton extends Button {
    public static Texture buttonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public InfoButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 0.5f,buttonTexture.getHeight() * 0.5f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH  * 0.1f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 600f - getHeight() / 2f);
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
            InfoScreen temp = new InfoScreen(mainGame.batch,mainGame);
            mainGame.setScreen(temp);
        }
    }
}
