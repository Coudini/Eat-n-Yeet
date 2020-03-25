package fi.tiko.eatnyeet;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.Image;



public class ForceMeter extends GraphicObject {

    public static Texture texture;
    public static boolean charge;
    public float rotation;
    public static float width = 5f;
    public static float height = 5f;




    //<Texture texture, float x, float y, float width, float height, MainGame game
    public ForceMeter(MainGame game) {
        super(texture, width, height, game);
        setOriginCenter();
        //setRotation(180);
        System.out.println("meter made");
        charge = false;
    }



    public void update () {
        super.update();

        if (Character.startPosSet) {
            charge = true;
        } else {
            charge = false;
            if (getRegionWidth() > 0) {
                setRegionWidth(0);
            }
        }
        if (charge) {
            setRotation(Character.angle / 2);
            int temp = (int) Character.meter2x;
            if (temp < 0) {
                temp = temp + (temp * 2);
            }
            System.out.println(temp);
            setRegionWidth(temp);
            move();
        }
    }
    public void move() {
        setX((Character.pX) - getWidth() / 2);
    }

}
