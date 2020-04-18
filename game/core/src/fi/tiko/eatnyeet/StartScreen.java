package fi.tiko.eatnyeet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class StartScreen implements Screen {

    SpriteBatch batch;
    MainGame mainGame;
    public static Texture startScreenBackGround;

    ArrayList<Button> buttons;

    public StartScreen (SpriteBatch batch, MainGame mainGame) {
        this.batch = batch;
        this.mainGame = mainGame;
        startScreenBackGround = new Texture("tilebk.png");
        TutorialButton.tutorialButtonTexture = new Texture("tutorial.png");
        PlayButton.playButtonTexture = new Texture("play.png");
        HighscoreButton.highscoreButtonTexture = new Texture("highscore.png");
        QuitButton.quitButtonTexture = new Texture("quit.png");

        buttons = new ArrayList<>();

        buttons.add(new PlayButton(mainGame));
        buttons.add(new TutorialButton(mainGame));
        buttons.add(new HighscoreButton(mainGame));
        buttons.add(new QuitButton(mainGame));


        // all startscreen inputs are handled in here
        createInputProcessor();


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(mainGame.fontCamera.combined);
        updateButtons();
        batch.begin();
        batch.draw(startScreenBackGround,0f,0f);
        renderButtons(batch);
        batch.end();
    }


    /**
     * Startscreen inputs are handled here
     * Scales button up if user holds down it and scales down after not holding it down anymore
     * Sets button value isClicked to true if click was successfully pressed on proper location
     */
    public void createInputProcessor() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                mainGame.fontCamera.unproject(realMousePos);

                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;

                for (Button btn : buttons) {
                    if (mousePosX >= btn.getxStart() && mousePosX <= btn.getxEnd() && mousePosY >= btn.getyStart() && mousePosY <= btn.getyEnd()) {
                        btn.setScale(1.2f);
                    }
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                mainGame.fontCamera.unproject(realMousePos);

                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;

                for (Button btn : buttons) {
                    if (mousePosX >= btn.getxStart() && mousePosX <= btn.getxEnd() && mousePosY >= btn.getyStart() && mousePosY <= btn.getyEnd()) {
                        btn.setScale(1f);
                        btn.clicked();
                    } else {
                        btn.setScale(1f);
                    }
                }
                return false;
            }
        });
    }
    public void updateButtons() {
        for (Button obj: buttons) {
            obj.update();
        }
    }
    public void renderButtons(SpriteBatch batch) {
        for (Button obj: buttons) {
            obj.render(batch);
        }
    }




    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        startScreenBackGround.dispose();
        for (Button btn : buttons) {
            btn.getTexture().dispose();
        }
        System.out.println("StartScreen dispose complete");
    }
}
