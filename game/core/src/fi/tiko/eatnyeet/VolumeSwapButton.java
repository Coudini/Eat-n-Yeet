package fi.tiko.eatnyeet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;

public class VolumeSwapButton extends Button {
    public static Texture volumeOnTexture;
    public static Texture volumeOffTexture;

    // 0 volume on 1 volume off
    private Texture [] textureArr = {volumeOnTexture,volumeOffTexture};
    private int index = 0;

    public VolumeSwapButton (MainGame mainGame) {
        super(volumeOnTexture,volumeOnTexture.getWidth() * 0.8f,volumeOnTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH * 0.1f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 100f - getHeight() / 2f);
        if (mainGame.useSounds) {
            index = 0;
        } else {
            index = 1;
        }
        setTexture(textureArr[index]);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    @Override
    public void update () {
        super.update();

        if (isClicked) {
            if (index == 0) {
                setTexture(textureArr[1]);
                index = 1;
                mainGame.useSounds = false;
            } else if (index == 1) {
                setTexture(textureArr[0]);
                index = 0;
                mainGame.useSounds = true;
            } else {
                System.out.println("texture swap error");
                // put default just in case
                setTexture(volumeOnTexture);
                mainGame.useSounds = true;
            }
            // highscore screenhere
            System.out.println("volume swap");
            isClicked = false;
        }
    }
}
