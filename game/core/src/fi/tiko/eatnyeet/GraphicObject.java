package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Superclass for objects that does not need body, aka visual objects.
 */
public class GraphicObject extends Sprite {

    Sound soundEffect;
    Animation<TextureRegion> currentAnimation;
    TextureRegion currentFrameTexture;
    public MainGame mainGame;

    protected float stateTime;
    protected float lifeTime = 0f;


    /**
     * Constructor
     * @param texture texture
     * @param width size
     * @param height size
     * @param mainGame saved to be able to access screens
     */
    public GraphicObject(Texture texture,float width, float height, MainGame mainGame) {
        super(texture);
        this.mainGame = mainGame;
        this.setSize(width,height);
        this.setOriginCenter();
    }

    /**
     * Constructor
     * @param texture texture
     * @param mainGame saved to be able to access screens
     */
    public GraphicObject(Texture texture, MainGame mainGame) {
        super(texture);
        this.mainGame = mainGame;
        this.setOriginCenter();
    }

    /**
     * Constructor
     * @param mainGame saved to be able to access screens
     */
    public GraphicObject(MainGame mainGame) {
        super();
        this.mainGame = mainGame;
        this.setOriginCenter();
    }

    /**
     * Updates life time and statetime, this should be called even if you overide this
     */
    public void update () {
        float delta = Gdx.graphics.getDeltaTime();
        lifeTime += delta;
        stateTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * default render for graphic objects, usually you can use this but some classes may require overide if they need special treatment aka rendering 2 or more textures
     * @param batch
     */
    public void render(Batch batch) {

        if (getTexture() == null && currentAnimation == null ) {
            return;
        }

        if (currentAnimation != null) {
            currentFrameTexture = currentAnimation.getKeyFrame(stateTime, true);
            this.setRegion(currentFrameTexture);
        }

        draw(batch);
    }

}
