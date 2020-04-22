package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class LanguageSwapButton extends Button {
    public static Texture buttonTexture;

    public LanguageSwapButton (MainGame mainGame) {
        super(buttonTexture,buttonTexture.getWidth() * 0.8f,buttonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH * 0.9f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 100f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    @Override
    public void update () {
        super.update();

        if (isClicked) {
            // highscore screenhere
            System.out.println("swap language");
            isClicked = false;
        }
    }

}
