package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class QuitButton extends Button {

    public static Texture quitButtonTexture;

    public QuitButton (MainGame mainGame) {
        super(quitButtonTexture,quitButtonTexture.getWidth() * 0.8f,quitButtonTexture.getHeight() * 0.8f,mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 630f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }
    public QuitButton (MainGame mainGame, float posX, float posY) {
        super(quitButtonTexture,quitButtonTexture.getWidth(),quitButtonTexture.getHeight(),mainGame);
        setPosition(posX, posY);
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
            //mainGame.gameScreen.dispose();
            //mainGame.startScreen.dispose();
            Gdx.app.exit();
        }
    }
}
