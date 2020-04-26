package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

/***
 * Button superclass for many buttons in Eat n Yeet procect. Button also extends graphicBbject.
 */
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


    /**
     * Constuctor that passes information to its supeclass graphicObject.
     * Also sets values to x and y starting and ending positions for program to be able to determine if buttons are clicked.
     * @param texture
     * @param width
     * @param height
     * @param mainGame
     */
    public Button (Texture texture,float width, float height, MainGame mainGame) {
        super(texture,width,height,mainGame);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();
    }

    /**
     * Default update for button, methods that all buttons need to call will be added here. Calling superclass update is a must.
     */
    @Override
    public void update () {
        super.update();
    }

    /**
     * set button status to clicked, which is used to check if actions is needed.
     */
    public void clicked () {
        isClicked = true;
    }


}
