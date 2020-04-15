package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class HighscoreButton extends Button {
    public static Texture highscoreButtonTexture;

    public HighscoreButton (StartScreen screen) {
        super(highscoreButtonTexture,highscoreButtonTexture.getWidth(),highscoreButtonTexture.getHeight(),screen);
        setPosition(screen.game.FONT_CAM_WIDTH / 2f - getWidth() / 2f, screen.game.FONT_CAM_HEIGHT  - 370f - getHeight() / 2f);
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
            // highscore screenhere
            System.out.println("highscore");
            isClicked = false;
        }
    }
}
