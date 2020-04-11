package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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
import java.util.concurrent.Callable;

public class GameScreen implements Screen {
    SpriteBatch batch;
    MainGame game;

    // window height and width in meters
    private final float WINDOW_WIDTH = 16f;
    private final float WINDOW_HEIGHT = 9f;

    private Box2DDebugRenderer debugRenderer;
    protected OrthographicCamera camera;
    protected OrthographicCamera fontCamera;
    protected boolean isPaused = false;
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
    FreeTypeFontGenerator generator;
    BitmapFont score;
    BitmapFont combo;


    // Alternate for ArrayList
    HashSet<GameObject> toBeDeleted;
    public GameScreen (SpriteBatch batch, MainGame game) {
        this.batch = batch;
        this.game = game;

        Sun.sunNoDisco = new Texture("sun.png");
        Sun.sunDisco = new Texture("sun_disco.png");
        ForceMeter.texture = new Texture("arrows.png"); //temp grphx
        Cloud.texture1 = new Texture("cloud1.png");
        Cloud.texture2 = new Texture("cloud2.png");
        Cloud.texture3 = new Texture("cloud3.png");
        Melon.texture1 = new Texture("melon.png");
        Melon.texture2 = new Texture("melond.png");
        Melon.melonNoDisco = new Texture("mel.png");
        Melon.melonDisco =  new Texture("meld.png");
        Carrot.texture1 = new Texture("carrotboi.png");
        Carrot.texture2 = new Texture("carrotdisco.png");
        Carrot.carrotNoDisco = new Texture("carrot.png");
        Carrot.carrotDisco = new Texture("carrot_disco.png");
        Tomato.texture1 = new Texture("tomatoboi_NoShadow.png");
        Tomato.texture2 = new Texture("tomatodisco.png"); //no discotomato yet
        Tomato.tomatoNoDisco = new Texture("tomato.png");
        Tomato.tomatoDisco = new Texture("tomato_disco.png");
        CompostWaste.texture = new Texture("compostcube.png");
        Character.run = new Texture("farma_run.png");
        Character.idle  = new Texture("farma_idle.png");
        Customer.customerTexture = new Texture("customer_boi.png");
        Customer.customerRun = new Texture("c_run.png");
        Customer.customerRun2 = new Texture("c_run2.png");
        Carrot.carrotEaten = new Texture("carrothalf.png");
        Tomato.tomatoEaten = new Texture("tomatohalf.png");
        Melon.melonEaten = new Texture("melonEaten.png");
        //Customer.carrotEaten = new Texture("carrothalf.png");
        //Customer.tomatoEaten = new Texture("tomatohalf.png");
        Rat.run = new Texture("ratboi_run.png");
        Compost.empty = new Texture("compost_empty.png");
        Compost.fill1 = new Texture("compost_stage1.png");
        Compost.fill2 = new Texture("compost_stage2.png");
        Compost.fill3 = new Texture("compost_stage3.png");
        Compost.fill4 = new Texture("compost_stage4.png");
        Field.empty = new Texture("field_empty.png");

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        fontCamera = new OrthographicCamera();
        fontCamera.setToOrtho(false, 1280f, 720f);



        gameObjects = new ArrayList<GameObject>();
        graphicObjects = new ArrayList<GraphicObject>();
        functionsToBeCalled = new ArrayList<Callable<Void>>();
        // gameworld  must be created before spawning anything else
        gameWorld = new GameWorld(this);

        spawnDefaultObjects();

        generator = new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 48;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = 1;
        score = generator.generateFont(parameter);
        combo = generator.generateFont(parameter);


        toBeDeleted = new HashSet<>();
        debugRenderer = new Box2DDebugRenderer();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (!isPaused) {

            batch.setProjectionMatrix(camera.combined);
            Gdx.gl.glClearColor(0, 0.5f, 1, 0.5f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            gameWorld.render(camera);
            moveCamera();

            gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
            spawnCustomers();
            callCallables();
            updateObjects();


            batch.begin();
            renderObjects();
            batch.end();

            // different render for fonts
            batch.setProjectionMatrix(fontCamera.combined);
            batch.begin();
            score.draw(batch, "Score " + player.getScore(), 200, 700);
            combo.draw(batch, "Combo " + player.getCombo(), 700, 700);
            batch.end();
            //debugRenderer.render(world, camera.combined);
            deleteDeletables();
            if (player.getCombo() >= 1) {
                game.setScreen(new TestScreen(batch, game));
                pause();
            }
        }
    }
    public void moveCamera() {
        //camera.position.x = player.body.getPosition().x;
        camera.update();
    }
    public void spawnDefaultObjects() {
        this.player = new Character(WINDOW_WIDTH / 2, 2f, this);
        gameObjects.add(player);



        //sun
        this.sun = new Sun(this);
        graphicObjects.add(sun);

        gameObjects.add(new Customer(this));
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
        //forcemeter
        this.meter = new ForceMeter(this);
        graphicObjects.add(meter);
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
        for (GraphicObject obj : graphicObjects) {
            obj.update();
        }
        for (GameObject obj: gameObjects) {
            obj.update();
        }

    }
    public void callCallables () {
        for (Callable<Void> callable: functionsToBeCalled) {
            try {
                callable.call();
            } catch (Exception e) {
                //ystem.out.println("penis");
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
     * Spawns customer every 5 seconds, uses player lifetime as caluclation value
     */
    float customerSpawnTimer = 0f;
    float randTime = MathUtils.random(4f,8f);
    public void spawnCustomers () {
        if (Field.getFillLevel() > 0) {
            if (player.lifeTime - customerSpawnTimer > randTime) {
                gameObjects.add(new Customer(this));
                customerSpawnTimer = player.lifeTime;
                randTime = MathUtils.random(4f, 8f);
            }
        }
    }

    @Override
    public void resize(int width, int height) {

    }



    @Override
    public void pause () {
        isPaused = true;
    }
    @Override
    public void resume () {
        System.out.println("resume");
        isPaused = false;
    }


    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        world.dispose();
        score.dispose();
        combo.dispose();
        for (GameObject obj: gameObjects) {
            if (obj.getTexture()!=null) {
                obj.getTexture().dispose();
            }
        }
        gameObjects.clear();
    }
}