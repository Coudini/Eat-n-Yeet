package fi.tiko.eatnyeet;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;




public class Button extends GraphicObject {

    protected float xStart;
    protected float xEnd;


    public float getxStart() {
        return xStart;
    }

    public float getxEnd() {
        return xEnd;
    }

    public float getyStart() {
        return yStart;
    }

    public float getyEnd() {
        return yEnd;
    }

    protected float yStart;
    protected float yEnd;

    protected boolean isClicked = false;

   /* public Button (Texture texture,float width, float height, StartScreen screen) {
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

    */
    public Button (Texture texture,float width, float height, MainGame mainGame) {
        super(texture,width,height,mainGame);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }

    @Override
    public void update () {
        super.update();
    }



    public void clicked () {
        isClicked = true;
    }


}
