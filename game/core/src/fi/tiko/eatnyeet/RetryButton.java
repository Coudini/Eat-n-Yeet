package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class RetryButton extends Button {
    public static Texture retryButtonTexture;

    public RetryButton (MainGame mainGame) {
        super(retryButtonTexture,retryButtonTexture.getWidth(),retryButtonTexture.getHeight(),mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 370f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }
    @Override
    public void update () {
        super.update();

        if (isClicked) {
            isClicked = false;
            mainGame.gameScreen = new GameScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.gameScreen);
        }
    }
}
