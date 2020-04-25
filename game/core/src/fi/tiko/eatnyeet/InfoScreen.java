package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;


public class InfoScreen implements Screen {
    SpriteBatch batch;
    MainGame mainGame;
    public static Texture startScreenBackGround;

    ArrayList<Button> buttons;
    ArrayList<GraphicObject> logos;

    String langBack;
    String langTiko;
    String langTamk;


    public InfoScreen (SpriteBatch batch, MainGame mainGame) {
        this.batch = batch;
        this.mainGame = mainGame;
        I18NBundle lang = I18NBundle.createBundle(Gdx.files.internal("lang"), mainGame.locale);
        startScreenBackGround = new Texture("menu_background.png");


        langBack = lang.get("back");
        langTamk = lang.get("tamk");
        langTiko = lang.get("tiko");

        BackToMainMenuButton.backButtonTexture = new Texture(langBack);

        buttons = new ArrayList<>();
        buttons.add(new BackToMainMenuButton(mainGame));

        logos = new ArrayList<>();
        GraphicObject oras = new GraphicObject(new Texture("oras.png"),mainGame);
        oras.setPosition(mainGame.FONT_CAM_WIDTH / 2 - oras.getWidth() / 2,mainGame.FONT_CAM_HEIGHT - 250f);
        oras.setScale(0.7f);
        logos.add(oras);

        GraphicObject tamk = new GraphicObject(new Texture(langTamk),mainGame);
        tamk.setPosition(mainGame.FONT_CAM_WIDTH / 2 - tamk.getWidth() / 2,mainGame.FONT_CAM_HEIGHT - 500f);
        tamk.setScale(0.7f);
        logos.add(tamk);

        GraphicObject tiko = new GraphicObject(new Texture(langTiko),mainGame);
        tiko.setPosition(mainGame.FONT_CAM_WIDTH / 2 - tiko.getWidth() / 2,mainGame.FONT_CAM_HEIGHT - 700f);
        tiko.setScale(0.7f);
        logos.add(tiko);




        // all startscreen inputs are handled in here
        createInputProcessor();


    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(mainGame.fontCamera.combined);
        updateObjects();
        batch.begin();
        batch.draw(startScreenBackGround,0f,0f);
        renderObjects(batch);
        batch.end();
    }


    /**
     * inputs are handled here
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
    public void updateObjects() {
        for (Button obj: buttons) {
            obj.update();
        }
        for (GraphicObject obj: logos) {
            obj.update();
        }
    }
    public void renderObjects(SpriteBatch batch) {
        for (Button obj: buttons) {
            obj.render(batch);
        }
        for (GraphicObject obj: logos) {
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
        System.out.println("Info dispose complete");
    }
}
