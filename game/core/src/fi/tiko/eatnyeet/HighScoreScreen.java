package fi.tiko.eatnyeet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * MainClass is just for demo purposes in this project.
 */
public class HighScoreScreen implements HighScoreListener, Screen {
	private Stage stage;
	private Skin skin;

	//localization
	Locale locale = Locale.getDefault();
	I18NBundle lang = I18NBundle.createBundle(Gdx.files.internal("lang"), locale);
	String langName;
	String langUpdateName;
	String langBackToMainMenu;
	String langHighscores;

	//protected String updateNameMessage = "Update name";
	//protected String backToMainMenuMessage = "Back to main menu";

	private Table content;

	public static Texture startScreenBackGround;
	SpriteBatch batch;
	MainGame mainGame;


    public HighScoreScreen (SpriteBatch batch, MainGame mainGame) {
		this.batch = batch;
		this.mainGame = mainGame;
		langName = lang.get("name");
		langUpdateName = lang.get("updatename");
		langBackToMainMenu = lang.get("backtomainmenu");
		langHighscores = lang.get("highscores");
        HighScoreServer.readConfig("highscore.config");
        HighScoreServer.setVerbose(true);
        HighScoreServer.fetchHighScores(this);

		startScreenBackGround = new Texture("tilebk.png");

        otherSetup();
    }
	@Override
	public void show() {

	}

	@Override
	public void hide() {

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
	public void receiveHighScore(List<HighScoreEntry> highScores) {
		Gdx.app.log("HighScoreScreen", "Received new high scores successfully");
		updateScores(highScores);
	}

	@Override
	public void receiveSendReply(Net.HttpResponse httpResponse) {
		Gdx.app.log("HighScoreScreen", "Received response from server: "
				+ httpResponse.getStatus().getStatusCode());
		HighScoreServer.fetchHighScores(this);
	}

	@Override
	public void failedToRetrieveHighScores(Throwable t) {
		Gdx.app.error("HighScoreScreen",
				"Something went wrong while getting high scores", t);
	}

	@Override
	public void failedToSendHighScore(Throwable t) {
		Gdx.app.error("HighScoreScreen",
				"Something went wrong while sending a high scoreField entry", t);
	}

	private void otherSetup() {
		skin = new Skin();
		skin = new Skin (Gdx.files.internal("uiskin.json"));
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		content = new Table();
		createTable();
		stage.addActor(content);
	}

	private ArrayList<Label> scoreLabels;

	private void updateScores(List<HighScoreEntry> scores) {
		int i = 0;
		for (HighScoreEntry e : scores) {
			String entry = e.getScore() + " - " + e.getName();
			scoreLabels.get(i).setText(entry);
			i++;
		}
	}

	private TextField nameField;
	private TextField scoreField;

	private void createTable() {
		content.setFillParent(true);
		content.add(new Label(langHighscores, skin)).colspan(2);

		scoreLabels = new ArrayList<>();


		for (int n = 0; n < 10; n++) {
			content.row();
			Label l = new Label("", skin);
			content.add(l).colspan(2);
			scoreLabels.add(l);
		}

		/*TextButton fetch = new TextButton("Fetch highscores", skin);
		fetch.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				fetchHighScores();
			}
		});


		 */


		TextButton newHighScore = new TextButton(langUpdateName, skin);
		newHighScore.addListener(new ClickListener() {
			 @Override
			 public void clicked(InputEvent event, float x, float y) {

				createNewScore();
			 }
		});
		TextButton backToMainMenu = new TextButton(langBackToMainMenu, skin);

		backToMainMenu.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {

				mainGame.startScreen = new StartScreen(mainGame.batch,mainGame);
				mainGame.setScreen(mainGame.startScreen);
			}
		});

   		content.row();
		//content.add(fetch).colspan(2);
		content.row();
		content.add(new Label(langName, skin));
		//content.add(new Label("Score:", skin));
		content.row();


		nameField = new TextField(mainGame.playerName, skin);
		//scoreField = new TextField("", skin);

		content.add(nameField);
		//content.add(scoreField);

		content.add(newHighScore);
		content.row();
		content.add(backToMainMenu).pad(10,100,0,0);
	}

	private void fetchHighScores() {
		HighScoreServer.fetchHighScores(this);
	}

	private void createNewScore() {
		String name = mainGame.playerName;
		try {
			 name = nameField.getText();
			 mainGame.playerName = name;
		} catch (Exception e) {
			System.out.println("No text");
		}

		// TODO frm player
		int score = mainGame.highestScore;
		//int score = Integer.parseInt(scoreField.getText());
		HighScoreEntry scoreEntry = new HighScoreEntry(name, score);
		HighScoreServer.sendNewHighScore(scoreEntry, this);
		fetchHighScores();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
		batch.draw(startScreenBackGround,0f,0f);
		batch.end();
		stage.draw();
	}

	@Override
	public void dispose () {
		skin.dispose();
	}
}