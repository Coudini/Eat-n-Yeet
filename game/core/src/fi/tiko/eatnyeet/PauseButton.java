package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class PauseButton extends Button {
    public static Texture pauseButtonTexture;


    public PauseButton (MainGame mainGame) {
        super(pauseButtonTexture,0.4f,0.4f,mainGame);
        setPosition(15f, 8f);
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
            System.out.println("Pause");
            mainGame.gameScreen.pause();
            isClicked = false;
            PauseScreen temp = new PauseScreen(mainGame.batch,mainGame);
            mainGame.setScreen(temp);
        }
    }
}
