package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.g2d.Batch;
import java.util.ArrayList;

public class Healthbar extends GraphicObject {

    public float x = 2.5f;
    public ArrayList<GraphicObject> hearts;


    /**
     * Constructor
     * @param mainGame saved to superclass
     * @param lifes amount of life textures
     */
    public Healthbar (MainGame mainGame, int lifes) {
        super(mainGame);
        hearts = new ArrayList<>();
        for (int i = 0; i < lifes; i++) {
            //create hearts
            hearts.add(new Heart(x, mainGame));
            x = x + 0.3f;
        }
    }

    /**
     * Reduces health amount aka one texture off
     */
    public void reduce() {
        hearts.remove(hearts.size()-1);
    }

    /**
     * Renders all health textures
     * @param batch
     */
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
