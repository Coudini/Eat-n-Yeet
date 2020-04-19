package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Healthbar extends GraphicObject {

    public static float x = 2.5f;
    public ArrayList<GraphicObject> hearts;


    public Healthbar (MainGame mainGame, int lifes) {
        super(mainGame);
        hearts = new ArrayList<>();
        for (int i = 0; i < lifes; i++) {
            //create hearts
            hearts.add(new Heart(x, mainGame));
            x = x + 0.3f;
        }
    }

    public void reduce() {
        hearts.remove(hearts.size()-1);
    }

    @Override
    public void render (Batch batch) {
        for (GraphicObject obj : hearts) {
            obj.render(batch);
        }
    }

    @Override
    public void update () {
        super.update();
    }
}
