package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class StartScreen implements Screen {
    SpriteBatch batch;
    MainGame game;
    // fontcamera should be fine for startscreen for both text and image usage
    public static Texture startScreenBackGround;


    GraphicObject playButton;


    public StartScreen (SpriteBatch batch, MainGame game) {
        this.batch = batch;
        this.game = game;
        startScreenBackGround = new Texture("game_background2.png");
        PlayButton.playButtonTexture = new Texture("play.png");


        playButton = new PlayButton(this);



    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(game.fontCamera.combined);
        playButton.update();
        batch.begin();
        batch.draw(startScreenBackGround,0f,0f);
        playButton.render(batch);
        batch.end();
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
        playButton.getTexture().dispose();
        System.out.println("StartScreen dispose complete");
    }
}
