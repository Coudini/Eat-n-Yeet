package fi.tiko.eatnyeet;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
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
	public ArrayList<GraphicObject> graphicObjects;
	public ArrayList <Callable<Void>> functionsToBeCalled;
	public Array<Body> bodies;
	Character player;
	ForceMeter meter;
	Cloud cloud;
	Sun sun;

	// Alternate for ArrayList
	HashSet<GameObject> toBeDeleted;

	@Override
	public void create () {
		Sun.texture1 = new Texture("sunboi_NoDisco.png");
		Sun.texture2 = new Texture("sunboi_YesDisco.png");
		ForceMeter.texture = new Texture("carrot_no_disco.png"); //temp grphx
		Cloud.texture1 = new Texture("cloud1.png");
		Cloud.texture2 = new Texture("cloud2.png");
		Cloud.texture3 = new Texture("cloud3.png");
		Banana.texture = new Texture("banana.png");
		Carrot.texture = new Texture("carrotboi.png");
		Tomato.texture = new Texture("tomatoboi_NoShadow.png");
		CompostWaste.texture = new Texture("compostcube.png");
		Character.run = new Texture("farma_run.png");
		Character.idle  = new Texture("farma_idle.png");
		Rat.run = new Texture("ratboi_run.png");
		Compost.empty = new Texture("compost_empty.png");
		Compost.fill1 = new Texture("compost_stage1.png");
		Compost.fill2 = new Texture("compost_stage2.png");
		Compost.fill3 = new Texture("compost_stage3.png");
		Compost.fill4 = new Texture("compost_stage4.png");
		Field.empty = new Texture("field_empty.png");
		Field.fill1 = new Texture("field_stage1.png");
		Field.fill2 = new Texture("field_stage2.png");
		Field.fill3 = new Texture("field_stage3.png");
		Field.fill4 = new Texture("field_stage4.png");
		Field.fill5 = new Texture("field_stage5.png");
		Field.fill6 = new Texture("field_stage6.png");
		Field.fill7 = new Texture("field_stage7.png");
		Field.fill8 = new Texture("field_stage8.png");
		Field.fill9 = new Texture("field_stage9.png");
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

		gameObjects = new ArrayList<GameObject>();
		graphicObjects = new ArrayList<GraphicObject>();
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

		gameWorld.render(camera);
		moveCamera();

		gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
		callCallables();
		updateObjects();

		batch.begin();
		renderObjects();
		batch.end();

		//debugRenderer.render(world, camera.combined);
		deleteDeletables();
	}


	public void moveCamera() {
		//camera.position.x = player.body.getPosition().x;
		camera.update();
	}
	public void spawnDefaultObjects() {
		this.player = new Character(WINDOW_WIDTH / 2, 2f, this);
		gameObjects.add(player);
		//this.meter = new ForceMeter(this);
		//graphicObjects.add(meter);

		//sun
		this.sun = new Sun(Sun.texture1, this);
		graphicObjects.add(sun);

		//clouds ym grphx
		for (int i = 0; i < 3; i++) {
			System.out.println(i);
			if (i==0) {
				this.cloud = new Cloud(Cloud.texture1, this);
				graphicObjects.add(cloud);
			}
			if (i==1) {
				this.cloud = new Cloud(Cloud.texture2, this);
				graphicObjects.add(cloud);
			}
			if (i==2) {
				this.cloud = new Cloud(Cloud.texture3, this);
				graphicObjects.add(cloud);
			}
		}
	}

	//add grphx rendere here
	public void renderObjects () {
		for (GraphicObject obj : graphicObjects) {
			obj.render(batch);
		}
		for (GameObject obj: gameObjects) {
			obj.render(batch);
		}

	}
	public void updateObjects () {
		for (GameObject obj: gameObjects) {
			obj.update();
		}
		for (GraphicObject obj : graphicObjects) {
			obj.update();
		}
	}
	public void callCallables () {
		for (Callable<Void> callable: functionsToBeCalled) {
			try {
				callable.call();
			} catch (Exception e) {
				//System.out.println("penis");
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

	/**
	 * Not in use,could be refactored to be random nutrient spawner
	 */
	public void tempBananaSpawn () {
		boolean spawnBanana = false;
		for (GameObject obj : gameObjects) {
			if (obj instanceof Food) {
				spawnBanana = true;
			}
		}
		if (!spawnBanana) {
			//temporally to simulate random X on food-drops
			float tempX = MathUtils.random(3f,13f);
			System.out.println();
			Banana temp = new Banana(tempX, WINDOW_HEIGHT -1f, this);
			//Banana temp = new Banana(WINDOW_WIDTH / 2f, WINDOW_HEIGHT -1f, this);
			gameObjects.add(temp);
		}

	}
	@Override
	public void pause () {

	}
	@Override
    public void resume () {

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
