package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class PlayButton extends Button {
    public static Texture playButtonTexture;

    public PlayButton (MainGame mainGame) {
        super(playButtonTexture,playButtonTexture.getWidth(),playButtonTexture.getHeight() ,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 300f - getHeight() / 2f);
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
            mainGame.gameScreen = new GameScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.gameScreen);
            mainGame.startScreen.dispose();
            isClicked = false;
        }
    }
}
