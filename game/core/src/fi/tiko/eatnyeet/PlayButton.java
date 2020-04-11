package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class PlayButton extends Button {
    public static Texture playButtonTexture;


    public PlayButton (StartScreen screen) {
        super(playButtonTexture,playButtonTexture.getWidth(),playButtonTexture.getHeight(),screen);
        setPosition(screen.game.FONT_CAM_WIDTH / 2f - getWidth() / 2f, screen.game.FONT_CAM_HEIGHT / 2f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }
    @Override
    public void update () {
        super.update();

        clickListenerFontCamera();
        // when clicked play button start game
        if (isClicked) {
            startScreen.game.gameScreen = new GameScreen(startScreen.batch,startScreen.game);
            startScreen.game.setScreen(startScreen.game.gameScreen);
            startScreen.dispose();
        }
    }
}
