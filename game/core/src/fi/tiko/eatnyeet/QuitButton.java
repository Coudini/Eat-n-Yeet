package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class QuitButton extends Button {

    public static Texture quitButtonTexture;

    public QuitButton (StartScreen screen) {
        super(quitButtonTexture,quitButtonTexture.getWidth(),quitButtonTexture.getHeight(),screen);
        setPosition(screen.game.FONT_CAM_WIDTH / 2f - getWidth() / 2f, screen.game.FONT_CAM_HEIGHT  - 500f - getHeight() / 2f);
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
            System.out.println("quit");
            isClicked = false;
            startScreen.dispose();
            Gdx.app.exit();
        }
    }
}
