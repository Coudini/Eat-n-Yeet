package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class PlayButton extends Button {
    public static Texture playButtonTexture;

    /**
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public PlayButton (MainGame mainGame) {
        super(playButtonTexture,playButtonTexture.getWidth(),playButtonTexture.getHeight() ,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 300f - getHeight() / 2f);
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
            mainGame.gameScreen = new GameScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.gameScreen);
            mainGame.startScreen.dispose();
            isClicked = false;
        }
    }
}
