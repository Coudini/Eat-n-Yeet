package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class QuitToMainMenuButton extends Button {

    public static Texture quitButtonTexture;

    public QuitToMainMenuButton (MainGame mainGame) {
        super(quitButtonTexture,quitButtonTexture.getWidth(),quitButtonTexture.getHeight(),mainGame);
        setPosition(mainGame.FONT_CAM_WIDTH / 2f - getWidth() / 2f, mainGame.FONT_CAM_HEIGHT  - 500f - getHeight() / 2f);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }


    @Override
    public void update () {
        super.update();

        if (isClicked) {
            // check if there is gamescreen created, if is stop the music
            try {
                if (mainGame.gameScreen.song.isPlaying()) {
                    mainGame.gameScreen.song.stop();
                }
            } catch (Exception e) {
                System.out.println("There is no gamescreen created");
            }

            isClicked = false;
            mainGame.startScreen = new StartScreen(mainGame.batch,mainGame);
            mainGame.setScreen(mainGame.startScreen);
        }
    }
}