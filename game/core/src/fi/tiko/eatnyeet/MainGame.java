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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;

	// window height and width in meters
	private final float WINDOW_WIDTH = 16f;
	private final float WINDOW_HEIGHT = 9f;

	private Box2DDebugRenderer debugRenderer;
	protected OrthographicCamera camera;
	public GameWorld gameWorld;
	public World world;
	public ArrayList<GameObject> gameObjects;
	public ArrayList <Callable<Void>> functionsToBeCalled;
	public Array<Body> bodies;
	Character player;

	// Alternate for ArrayList
	HashSet<GameObject> toBeDeleted;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

		gameObjects = new ArrayList<GameObject>();
		functionsToBeCalled = new ArrayList<Callable<Void>>();
		// gameworld  must be created before spawning anything else
		gameWorld = new GameWorld(this);

		spawnDefaultObjects();
		toBeDeleted = new HashSet<>();
		debugRenderer = new Box2DDebugRenderer();

	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0.5f, 1, 0.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		tempBananaSpawn();
		gameWorld.render(camera);
		moveCamera();

		gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
		callCallables();
		updateObjects();

		batch.begin();
		renderObjects();
		batch.end();

		debugRenderer.render(world, camera.combined);
		deleteDeletables();
	}


	public void moveCamera() {
		//camera.position.x = player.body.getPosition().x;
		camera.update();
	}
	public void spawnDefaultObjects() {
		this.player = new Character(WINDOW_WIDTH / 2, 4f,this);
		gameObjects.add(player);
	}

	public void renderObjects () {
		for (GameObject obj: gameObjects) {
			obj.render(batch);
		}
	}
	public void updateObjects () {
		for (GameObject obj: gameObjects) {
			obj.update();
		}
	}
	public void callCallables () {
		for (Callable<Void> callable: functionsToBeCalled) {
			try {
				callable.call();
			} catch (Exception e) {
				System.out.println("penis");
			}
		}
		functionsToBeCalled.clear();
	}
	public void deleteDeletables () {
		for (GameObject obj: toBeDeleted) {
				world.destroyBody(obj.body);
				gameObjects.remove(obj);
		}
		toBeDeleted.clear();
	}
	public void tempBananaSpawn () {
		boolean spawnBanana = false;
		for (GameObject obj : gameObjects) {
			if (obj instanceof Flingable) {
				spawnBanana = true;
			}
		}
		if (!spawnBanana) {
		Banana temp = new Banana(WINDOW_WIDTH / 2f, WINDOW_HEIGHT -1f, this);
			gameObjects.add(temp);
		}

	}

	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
		for (GameObject obj: gameObjects) {
			if (obj.getTexture()!=null) {
				obj.getTexture().dispose();
			}
		}
		gameObjects.clear();
	}
}
