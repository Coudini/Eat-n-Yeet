package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;

public class ArrowButton extends Button {

    public ArrowButton (MainGame mainGame, Texture texture, float x, float y) {
        super(texture,texture.getWidth() * 0.7f,texture.getHeight() * 0.7f,mainGame);
        setPosition(x, y);
        xStart = getX();
        xEnd = getX() + getWidth();
        yStart = getY();
        yEnd = getY() + getHeight();

    }

    @Override
    public void update () {
        super.update();

        if (isClicked) {
            System.out.println("arrow click");
            //isClicked = false;
        }
    }

}
