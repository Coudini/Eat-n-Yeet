package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class ArrowButton extends Button {

    /***
     * Constructor for creating button
     * @param mainGame saved to button superclass
     * @param texture texture that arrow uses
     * @param x position
     * @param y position
     */
    public ArrowButton (MainGame mainGame, Texture texture, float x, float y) {
        super(texture,texture.getWidth() * 0.7f,texture.getHeight() * 0.7f,mainGame);
        setPosition(x, y);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }
}
