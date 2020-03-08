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

public class MainGame extends ApplicationAdapter {
	SpriteBatch batch;

	// window height and width in meters
	private final float WINDOW_WIDTH = 16f;
	private final float WINDOW_HEIGHT = 9f;

	private Box2DDebugRenderer debugRenderer;
	private OrthographicCamera camera;
	private GameWorld gameWorld;
	public World world;
	public ArrayList<GameObject> gameObjects;
	//public Array<GameObject> toBeDeleted;
	public Array<Body> bodies;

	
	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

		// gameworld  must be created before spawning anything else
		gameWorld = new GameWorld(this);


		gameObjects = new ArrayList<GameObject>();
		//toBeDeleted = new Array<GameObject>();
		bodies = new Array<Body>();
		spawnDefaultObjects();

		debugRenderer = new Box2DDebugRenderer();

	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		world.getBodies(bodies);

		checkGestures();
		gameWorld.render(camera);
		moveCamera();

		batch.begin();
		renderObjects();
		batch.end();

		debugRenderer.render(world, camera.combined);
		gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
		deleteToBeDeleted();
	}


	public void moveCamera() {
		camera.position.x = gameObjects.get(0).body.getPosition().x;

		camera.update();
	}
	public void spawnDefaultObjects() {
		gameObjects.add(new Character(WINDOW_WIDTH / 2, 1f,this));
	}

	public void renderObjects () {
		for (GameObject obj: gameObjects) {
			obj.render(batch);

			/**
			try {
				if (obj.body.getUserData().equals("dead")) {
					toBeDeleted.add(obj);
				}
			} catch (Exception e) {
				System.out.println("Error adding object to be deleted, possible cause is missing userdata or typo in userdata");
			}
			 */

		}
	}
	public void deleteToBeDeleted () {

		Array<Body> removalBodies = new Array<Body>();
		ArrayList<GameObject> toBeDeleted = new ArrayList<>();

		for (Body body : bodies) {
			if (body.getUserData() != null) {
				if (body.getUserData().equals("dead")) {
					removalBodies.add(body);
					System.out.println("remove");
				}
			}
		}
		for (GameObject obj: gameObjects) {
			if (obj.body.getUserData().equals("dead")) {
				toBeDeleted.add(obj);
			}
		}
		for (Body body : removalBodies) {
			world.destroyBody(body);
		}
		gameObjects.removeAll(toBeDeleted);
		toBeDeleted.clear();



		//for (GameObject obj:toBeDeleted) {
		//		world.destroyBody(obj.body);
		//	gameObjects.removeValue(obj,true);
		//}


	}
	public void checkGestures () {
		if (Gdx.input.justTouched()) {

			int realX = Gdx.input.getX();
			int realY = Gdx.input.getY();
			Vector3 touchPos = new Vector3(realX, realY, 0);
			camera.unproject(touchPos);

			float speedX = (touchPos.x - gameObjects.get(0).body.getPosition().x) / 5;
			float speedY = (touchPos.y - gameObjects.get(0).body.getPosition().y) / 5;

			// adds banana to shoot during test scenario to object list TODO not needed in final version
			gameObjects.add(new Banana(gameObjects.get(0).body.getPosition().x,gameObjects.get(0).body.getPosition().y + 0.2f,this));

			// gives speed to banana based on click position, TODO fling support
			gameObjects.get(gameObjects.size()-1).body.applyLinearImpulse(new Vector2(speedX,speedY),gameObjects.get(gameObjects.size()-1).body.getWorldCenter(),true);
		}
	}


	@Override
	public void dispose () {
		batch.dispose();
		world.dispose();
	}
}
