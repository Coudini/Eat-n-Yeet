package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.ArrayList;

public class TutorialScreen implements Screen {
    SpriteBatch batch;
    MainGame mainGame;
    public static Texture startScreenBackGround;
    public static Texture arrowLeftTexture;
    public static Texture arrowRightTexture;
    private ArrayList<Texture> tutorialImages;
    private static Texture tut1;
    private static Texture tut2;
    private int index = 0;
    private int tutImgPrintX;
    private int tutImgPrintY;
    ArrayList<Button> buttons;
    Button arrowLeft;
    Button arrowRight;
    String langBack;
    String langTutorial1;
    String langTutorial2;

    /**
     * Constructor, shows tutorial images to user. The images shown are chosen by localization and language chosen by the user
     * @param batch saved for rendering
     * @param mainGame saved to be able access other classes and information
     */
    public TutorialScreen (SpriteBatch batch, MainGame mainGame) {
        this.batch = batch;
        this.mainGame = mainGame;
        I18NBundle lang = I18NBundle.createBundle(Gdx.files.internal("lang"), mainGame.locale);
        startScreenBackGround = new Texture("menu_background.png");
        arrowLeftTexture = new Texture("arrow_left.png");
        arrowRightTexture = new Texture("arrow_right.png");
        langBack = lang.get("back");
        langTutorial1 = lang.get("tutorial1");
        langTutorial2 = lang.get("tutorial2");
        tut1 = new Texture(langTutorial1);
        tut2 = new Texture(langTutorial2);
        // refresh these values if adding more tutorial images that are not same size as tut1!
        tutImgPrintX = (int)mainGame.FONT_CAM_WIDTH / 2 - tut1.getWidth() / 2;
        tutImgPrintY = (int)mainGame.FONT_CAM_HEIGHT / 2 - tut1.getHeight() / 2;
        tutorialImages = new ArrayList<>();
        tutorialImages.add(tut1);
        tutorialImages.add(tut2);
        BackToMainMenuButton.backButtonTexture = new Texture(langBack);
        buttons = new ArrayList<>();
        float posX = mainGame.FONT_CAM_WIDTH / 2f - BackToMainMenuButton.backButtonTexture.getWidth() / 2;
        float poxY = 50f;
        buttons.add(new BackToMainMenuButton(mainGame,posX,poxY));
        posX = 50f;
        arrowLeft = new ArrowButton(mainGame,arrowLeftTexture, posX,poxY);
        posX = mainGame.FONT_CAM_WIDTH - 50f - arrowRightTexture.getWidth() * 0.7f;
        arrowRight = new ArrowButton(mainGame,arrowRightTexture,posX,poxY);
        buttons.add(arrowRight);
        buttons.add(arrowLeft);
        // all startscreen inputs are handled in here
        createInputProcessor();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(mainGame.fontCamera.combined);
        updateButtons();
        arrowClickChecker();
        batch.begin();
        batch.draw(startScreenBackGround,0f,0f);
        batch.draw(tutorialImages.get(index),tutImgPrintX,tutImgPrintY);
        renderButtons(batch);
        batch.end();
    }

    /**
     * Sets left and right arrows to clicked and changes the value of index
     */
    private void arrowClickChecker() {
        if (arrowRight.isClicked) {
            arrowRight.isClicked = false;
            if (index < tutorialImages.size()-1) {
                index++;
            }
        } else if (arrowLeft.isClicked) {
            arrowLeft.isClicked = false;
            if (index > 0) {
                index--;
            }
        }

    }

    /**
     * Startscreen inputs are handled here
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

    /**
     * Updates all buttons
     */
    public void updateButtons() {
        for (Button obj: buttons) {
            obj.update();
        }
    }

    /**
     * Renders all buttons
     * @param batch saved for rendering
     */
    public void renderButtons(SpriteBatch batch) {
        for (Button obj: buttons) {
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
    }
}
