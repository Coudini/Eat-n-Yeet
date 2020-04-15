package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class TutorialButton extends Button {
    public static Texture tutorialButtonTexture;


    public TutorialButton (StartScreen screen) {
        super(tutorialButtonTexture,tutorialButtonTexture.getWidth(),tutorialButtonTexture.getHeight(),screen);
        setPosition(screen.game.FONT_CAM_WIDTH / 2f - getWidth() / 2f, screen.game.FONT_CAM_HEIGHT - 240f - getHeight() / 2f);
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
            System.out.println("tutorial clicked");
            isClicked = false;
        }
    }
}

