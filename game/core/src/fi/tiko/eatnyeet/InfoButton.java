package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class InfoButton extends Button {
    public static Texture buttonTexture;


    public InfoButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 0.5f,buttonTexture.getHeight() * 0.5f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH  * 0.1f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 600f - getHeight() / 2f);
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
            InfoScreen temp = new InfoScreen(mainGame.batch,mainGame);
            mainGame.setScreen(temp);
        }
    }

}
