package fi.tiko.eatnyeet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.concurrent.Callable;

/**
 * This file will start when opening the game, by default it will set screen to be MainMenuScreen
 * */
public class MainGame extends Game  {
	public Locale locale = Locale.getDefault();

	SpriteBatch batch;

	GameScreen gameScreen;
	StartScreen startScreen;
	GameOverScreen gameOverScreen;
	HighScoreScreen highScoreScreen;

	protected String playerName = "Player";
	protected int highestScore = 0;
	protected boolean useSounds = true;

	protected final float FONT_CAM_WIDTH = 1280f;
	protected final float FONT_CAM_HEIGHT = 720f;
	protected final float GAME_CAM_WIDTH = 16f;
	protected final float GAME_CAM_HEIGHT = 9f;

	protected OrthographicCamera camera;
	protected OrthographicCamera fontCamera;

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GAME_CAM_WIDTH, GAME_CAM_HEIGHT);

		fontCamera = new OrthographicCamera();
		fontCamera.setToOrtho(false, FONT_CAM_WIDTH, FONT_CAM_HEIGHT);

		startScreen = new StartScreen(batch,this);


		//probably useless
		String tmp = locale.getLanguage();
		if (!(tmp.equals("en") || tmp.equals("fi"))) {
			locale = new Locale("fi", "FI");
		}

		setScreen(startScreen);


	}

	public  void changeLocale() {
		String tmp = locale.getLanguage();

		if (tmp.equals("en")) {
			locale = new Locale("fi","FI");
		}
		else if (tmp.equals("fi")) {
			locale = new Locale("en","US");

		}
	}
	public BitmapFont generateFont(int size, int borderWidth) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = borderWidth;

	    BitmapFont temp = generator.generateFont(parameter);
	    return temp;
    }
	@Override
	public void render () {
		super.render();
	}


	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
	@Override
	public void dispose () {
		batch.dispose();

	}
}
