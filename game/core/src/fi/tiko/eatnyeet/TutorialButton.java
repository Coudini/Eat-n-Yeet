package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class TutorialButton extends Button {
    public static Texture tutorialButtonTexture;


    public TutorialButton (MainGame mainGame) {
        super(tutorialButtonTexture,tutorialButtonTexture.getWidth() * 0.8f,tutorialButtonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT - 410f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }
    @Override
    public void update () {
        super.update();

        // when clicked play button start game
        if (isClicked) {
                // swap to tutorial screen
            isClicked = false;
            TutorialScreen tutScreen = new TutorialScreen(mainGame.batch,mainGame);
            mainGame.setScreen(tutScreen);
        }
    }
}

