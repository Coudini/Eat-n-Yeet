package fi.tiko.eatnyeet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;

	// window height and width in meters
	private final float WINDOW_WIDTH = 16f;
	private final float WINDOW_HEIGHT = 9f;

	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private GameWorld gameWorld;
	public World world;
	private ArrayList<GameObject> gameObjects;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

		// gameworld  must be created before spawning anything else
		gameWorld = new GameWorld(this);


		gameObjects = new ArrayList<GameObject>();
		spawnDefaultObjects();

		// includes static world features


		debugRenderer = new Box2DDebugRenderer();

	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameWorld.render(camera);
		moveCamera();
		updateObjects();


		batch.begin();
		renderObjects();
		batch.end();
		debugRenderer.render(world, camera.combined);
		gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
	}


	public void moveCamera() {
		camera.position.x = gameObjects.get(0).body.getPosition().x;

		camera.update();
	}
	public void spawnDefaultObjects() {
		gameObjects.add(new Character(WINDOW_WIDTH / 2, 2f,this));
	}
	public void updateObjects () {
		for (GameObject obj: gameObjects) {
			obj.update();
		}
	}
	public void renderObjects () {
		for (GameObject obj: gameObjects) {
			obj.render(batch);
		}
	}

	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
