package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class BackToMainMenuButton extends Button{
    public static Texture backButtonTexture;

    /***
     * Constructor, creates button with default settings
     * @param mainGame saved to button superclass
     */
    public BackToMainMenuButton (MainGame mainGame) {
        super(backButtonTexture,backButtonTexture.getWidth() ,backButtonTexture.getHeight(),mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH * 0.15f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT - 100f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    /**
     * Constructor with ability to determine spot for button
     * @param mainGame saved to button superclass
     * @param x position
     * @param y position
     */
    public BackToMainMenuButton (MainGame mainGame, float x, float y) {
        super(backButtonTexture,backButtonTexture.getWidth(),backButtonTexture.getHeight(),mainGame);
        setPosition(x,y);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    /***
     * Updates object on every iteration, also calls button superclass update method. Most function calls are made here.
     */
    @Override
    public void update () {
        super.update();

        if (isClicked) {
            isClicked = false;
            mainGame.startScreen = new StartScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.startScreen);
        }
    }
}


