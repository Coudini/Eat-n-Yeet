package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;


public class Button extends GraphicObject {

    protected float xStart;
    protected float xEnd;
    protected float yStart;
    protected float yEnd;
    protected boolean isClicked = false;

    public Button (Texture texture,float width, float height, StartScreen screen) {
        super(texture,width,height,screen);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }
    public Button (Texture texture,float width, float height, GameScreen screen) {
        super(texture,width,height,screen);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }

    @Override
    public void update () {
        super.update();

    }


    public void clickListenerFontCamera () {

        Gdx.input.setInputProcessor(new InputAdapter() {

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                if (startScreen != null) {
                    startScreen.game.fontCamera.unproject(realMousePos);
                } else {
                    gameScreen.game.fontCamera.unproject(realMousePos);
                }
                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;

                if (mousePosX >= xStart && mousePosX <= xEnd && mousePosY >= yStart && mousePosY <= yEnd) {
                    setScale(1.2f);
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                if (startScreen != null) {
                    startScreen.game.fontCamera.unproject(realMousePos);
                } else {
                    gameScreen.game.fontCamera.unproject(realMousePos);
                }
                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;
                if (mousePosX >= xStart && mousePosX <= xEnd && mousePosY >= yStart && mousePosY <= yEnd) {
                    setScale(1f);
                    clicked();
                } else {
                    setScale(1f);
                }
                return false;
            }
        });
    }

    /**
     * Not working probably because libgdx cannot handle 2 differentn inputprosessors with this method??
     * TODO fix this
     */
    public void clickListenerGameCamera () {


        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                System.out.println("touchdown");
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                if (startScreen != null) {

                    startScreen.game.camera.unproject(realMousePos);
                } else {

                    gameScreen.game.camera.unproject(realMousePos);
                }
                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;

                if (mousePosX >= xStart && mousePosX <= xEnd && mousePosY >= yStart && mousePosY <= yEnd) {
                    setScale(1.2f);
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                System.out.println("touchup");
                Vector3 realMousePos = new Vector3(screenX, screenY, 0);
                if (startScreen != null) {
                    startScreen.game.camera.unproject(realMousePos);
                } else {
                    gameScreen.game.camera.unproject(realMousePos);
                }

                float mousePosY = realMousePos.y;
                float mousePosX = realMousePos.x;
                if (mousePosX >= xStart && mousePosX <= xEnd && mousePosY >= yStart && mousePosY <= yEnd) {
                    setScale(1f);
                    clicked();
                } else {
                    setScale(1f);
                }
                return false;
            }
        });
    }
    public void clicked () {
        isClicked = true;
    }
}
