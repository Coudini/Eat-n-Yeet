package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.Locale;

public class GameOverScreen implements Screen {
    SpriteBatch batch;
    MainGame mainGame;
    public static Texture startScreenBackGround;

    //localization

    I18NBundle lang = I18NBundle.createBundle(Gdx.files.internal("lang"), MainGame.locale);
    String langGameOver;
    String langScore;
    String langQuit;
    String langRetry;

    //protected String gameOverMessage = "Game over!";
    protected int gameOverMessageLength;
    //protected String scoreMessage = "Your score was ";
    protected int scoreLenght;
    protected int fontSize = 48;
    ArrayList<Button> buttons;

    BitmapFont messageAndScore;



    public GameOverScreen (SpriteBatch batch, MainGame mainGame, int score) {
        this.batch = batch;
        this.mainGame = mainGame;
        startScreenBackGround = new Texture("tilebk.png");

        langGameOver = lang.get("gameover");
        langScore = lang.get("scorewas");
        langQuit = lang.get("quit");
        langRetry = lang.get("retry");

        QuitToMainMenuButton.quitButtonTexture = new Texture(langQuit);
        RetryButton.retryButtonTexture = new Texture(langRetry);

        buttons = new ArrayList<>();

        buttons.add(new QuitToMainMenuButton(mainGame));
        buttons.add(new RetryButton(mainGame));

        // update score if it was bigger than session highest
        if (score > mainGame.highestScore) {
            String name = "Seppo :D";
            HighScoreEntry scoreEntry = new HighScoreEntry(name, score);
            // this needs to be done before creating inputprocessor for gameover screen, or else game will use highscore sceens input prosessor
            mainGame.highScoreScreen = new HighScoreScreen(mainGame.batch,mainGame);
            HighScoreServer.sendNewHighScore(scoreEntry, mainGame.highScoreScreen);
            mainGame.highestScore = score;
        }


        // all startscreen inputs are handled in here
        createInputProcessor();
        messageAndScore = mainGame.generateFont(fontSize,1);

        gameOverMessageLength = langGameOver.length() * fontSize / 2;
        langScore += score;
        scoreLenght = langScore.length() * fontSize / 2;


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
        messageAndScore.draw(batch,langGameOver,mainGame.FONT_CAM_WIDTH / 2 - gameOverMessageLength / 2,mainGame.FONT_CAM_HEIGHT - 150);
        messageAndScore.draw(batch,langScore,mainGame.FONT_CAM_WIDTH / 2 - scoreLenght / 2,mainGame.FONT_CAM_HEIGHT - 250);
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
