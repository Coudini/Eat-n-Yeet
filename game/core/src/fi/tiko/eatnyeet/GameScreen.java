package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.I18NBundle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.Callable;
import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

/**
 * Game screen, aka the screen where use plays the game.
 */
public class GameScreen implements Screen {
    SpriteBatch batch;
    MainGame mainGame;

    private Box2DDebugRenderer debugRenderer;
    protected boolean isPaused = false;
    public GameWorld gameWorld;
    public World world;
    public ArrayList<GameObject> gameObjects;
    public ArrayList<GameObject> customers;
    public ArrayList<GraphicObject> graphicObjects;
    public ArrayList <Callable<Void>> functionsToBeCalled;
    public ArrayList<Button> buttons;
    private boolean gameOver = false;
    Character player;

    Healthbar healthbar;

    Compost compost;

    ForceMeter meter;
    Cloud cloud;
    Sun sun;
    PauseButton pauseButton;

    BitmapFont score;
    BitmapFont combo;
    BitmapFont health;

    InputMultiplexer multiplexer;
    InputAdapter gameUiInputs;

    // Alternate for ArrayList
    HashSet<GameObject> toBeDeleted;

    String langScore;
    String langCombo;

    public boolean sounds;
    Music song;

    /**
     * Constructor, calls and creates all the default values for the game.
     * @param batch saved for rendering
     * @param mainGame saved to be able access other classes and information
     */
    public GameScreen (SpriteBatch batch, MainGame mainGame) {
        this.batch = batch;
        this.mainGame = mainGame;
        I18NBundle lang = I18NBundle.createBundle(Gdx.files.internal("lang"), mainGame.locale);
        Sun.sunNoDisco = new Texture("sun.png");
        Sun.sunDisco = new Texture("sun_disco.png");
        ForceMeter.texture = new Texture("force_arrow.png");
        Cloud.texture1 = new Texture("cloud1.png");
        Cloud.texture2 = new Texture("cloud2.png");
        Cloud.texture3 = new Texture("cloud3.png");
        Melon.texture1 = new Texture("melon.png");
        Melon.texture2 = new Texture("melond.png");
        Melon.melonNoDisco = new Texture("melNew.png");
        Melon.melonDisco =  new Texture("MelonParty.png");
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
        Customer.customerRun3 = new Texture("c_run3.png");
        Carrot.carrotEaten = new Texture("carrothalf.png");
        Tomato.tomatoEaten = new Texture("tomatohalf.png");
        Melon.melonEaten = new Texture("melonEaten.png");
        Rat.run = new Texture("prison_rat.png");
        Compost.empty = new Texture("compost_empty.png");
        Compost.fill1 = new Texture("compost_stage1.png");
        Compost.fill2 = new Texture("compost_stage2.png");
        Compost.fill3 = new Texture("compost_stage3.png");
        Compost.fill4 = new Texture("compost_stage4.png");
        Field.empty = new Texture("field_empty.png");
        PauseButton.pauseButtonTexture = new Texture("pause.png");
        Heart.texture = new Texture("heart.png");
        song = audio.newMusic(files.internal("music3.mp3"));
        song.setVolume(0.4f);
        song.setLooping(true);
        sounds = mainGame.useSounds;
        if (sounds) {
            song.play();
        }


        gameObjects = new ArrayList<>();
        customers = new ArrayList<>();
        graphicObjects = new ArrayList<>();
        functionsToBeCalled = new ArrayList<>();
        buttons = new ArrayList<>();
        // gameworld  must be created before spawning anything else
        gameWorld = new GameWorld(this);

        spawnDefaultObjects();


        score = mainGame.generateFont(48,1);
        combo =  mainGame.generateFont(48,1);
        health =  mainGame.generateFont(48,1);

        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(player.characterInput);
        createGameUiInputs();
        multiplexer.addProcessor(gameUiInputs);
        Gdx.input.setInputProcessor(multiplexer);


        toBeDeleted = new HashSet<>();
        debugRenderer = new Box2DDebugRenderer();

        //localisation for fonts

        langScore = lang.get("score");
        langCombo = lang.get("combo");
    }


    @Override
    public void show() {

    }

    /**
     * Main render which updates and renders all the object game has
     * @param delta time from previous frame
     */
    @Override
    public void render(float delta) {

        if (player.healthPoints <= 0) {
            mainGame.gameOverScreen = new GameOverScreen(batch, mainGame, player.characterScore);
            mainGame.setScreen(mainGame.gameOverScreen);
            gameOver = true;
            song.stop();
        }
        if (!gameOver) {
            batch.setProjectionMatrix(mainGame.camera.combined);
            Gdx.gl.glClearColor(0, 0.5f, 1, 0.5f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            gameWorld.render(mainGame.camera);
            if (!isPaused) {
                moveCamera();

                gameWorld.doPhysicsStep(Gdx.graphics.getDeltaTime());
                spawnCustomers();
                callCallables();
                updateObjects();
                //debugRenderer.render(world, game.camera.combined);
                deleteDeletables();
            }
            batch.begin();
            renderObjects();
            batch.end();

            // different render for fonts
            batch.setProjectionMatrix(mainGame.fontCamera.combined);
            batch.begin();
            score.draw(batch, langScore + player.getScore(), 200, 700);
            combo.draw(batch, langCombo + player.getCombo(), 700, 700);
            batch.end();
        }
        if (gameOver) {
            dispose();
        }

    }

    /**
     * updates camera position
     */
    public void moveCamera() {
        mainGame.camera.update();
    }

    /**
     * Spawns all the objects to the game that is needed immediately on launch
     */
    public void spawnDefaultObjects() {
        this.player = new Character(mainGame.GAME_CAM_WIDTH / 2, 2f, this);
        gameObjects.add(player);

        gameObjects.add(new Rat(1f,2f,this));

        //sun
        this.sun = new Sun(mainGame);
        graphicObjects.add(sun);
        this.pauseButton = new PauseButton(mainGame);
        buttons.add(pauseButton);

        //clouds
        for (int i = 0; i < 3; i++) {
            if (i==0) {
                this.cloud = new Cloud(Cloud.texture1, mainGame);
                graphicObjects.add(cloud);
            }
            if (i==1) {
                this.cloud = new Cloud(Cloud.texture2, mainGame);
                graphicObjects.add(cloud);
            }
            if (i==2) {
                this.cloud = new Cloud(Cloud.texture3, mainGame);
                graphicObjects.add(cloud);
            }
        }

        //healthbar
        healthbar = new Healthbar(mainGame, player.healthPoints);
        graphicObjects.add(healthbar);

        //forcemeter
        this.meter = new ForceMeter(mainGame);
        graphicObjects.add(meter);
    }

    /**
     * Goes throuhg all render lists
     */
    public void renderObjects () {
        for (GraphicObject obj : graphicObjects) {
            obj.render(batch);
        }
        for(GameObject obj : customers) {
            obj.render(batch);
        }
        for (GameObject obj: gameObjects) {
            obj.render(batch);
        }
        for (Button btn : buttons) {
            btn.render(batch);
        }


    }

    /**
     * Goes through all update lists
     */
    public void updateObjects () {
        for (GraphicObject obj : graphicObjects) {
            obj.update();
        }
        for (GameObject obj : customers) {
            obj.update();
        }
        for (GameObject obj: gameObjects) {
            obj.update();
        }

        for (Button btn : buttons) {
            btn.update();
        }

    }

    /**
     * Calls the methods or does the tasks that were supposed to do after physicsStep
     */
    public void callCallables () {
        for (Callable<Void> callable: functionsToBeCalled) {
            try {
                callable.call();
            } catch (Exception e) {
                System.out.println("Error when calling callables");
            }
        }
        functionsToBeCalled.clear();
    }

    /**
     * Delete objects that has been added to toBeDeleted list
     */
    public void deleteDeletables () {
        for (GameObject obj: toBeDeleted) {
            world.destroyBody(obj.body);
            gameObjects.remove(obj);
            customers.remove(obj);
        }
        toBeDeleted.clear();
    }

    /**
     * Spawns customer after random amount of time uses player lifetime as caluclation value
     * Speed up when player score increases
     */
    float customerSpawnTimer = 0f;
    float minTime = 4f;
    float maxTime = 8f;
    float randTime = MathUtils.random(minTime,maxTime);
    public void spawnCustomers () {
        if (Field.getFillLevel() > 0) {
            if (player.lifeTime - customerSpawnTimer > randTime) {
                customers.add(new Customer(this));
                customerSpawnTimer = player.lifeTime;

                minTime = minTime - (float)player.characterScore * 0.001f;
                maxTime = maxTime - (float)player.characterScore * 0.001f;

                // limit how fast customers can possibly spawn
                if (minTime < 1.8f) {
                    minTime = 1.8f;
                }
                if (maxTime < 3f) {
                    maxTime = 3f;
                }
                randTime = MathUtils.random(minTime, maxTime);

                minTime = 4f;
                maxTime =8f;

            }
        }
    }

    /**
     * Input prosessor created for being able to interact with ui
     */
    public void createGameUiInputs() {
        gameUiInputs = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                mainGame.camera.unproject(realMousePos);

                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;

                for (Button btn : buttons) {
                    if (mousePosX >= btn.getxStart() && mousePosX <= btn.getxEnd() && mousePosY >= btn.getyStart() && mousePosY <= btn.getyEnd()) {
                        btn.setScale(1.2f);
                    }
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                mainGame.camera.unproject(realMousePos);

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
                return true;
            }
        };
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
        song.dispose();
        for (GameObject obj: gameObjects) {
            if (obj.getTexture()!=null) {
                obj.getTexture().dispose();
            }
        }
        gameObjects.clear();
    }
}
