package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;


public class GraphicObject extends Sprite {

    Sound soundEffect;
    Animation<TextureRegion> currentAnimation;
    TextureRegion currentFrameTexture;
    //public GameScreen gameScreen;
    //public StartScreen startScreen;
    public MainGame mainGame;

    protected float stateTime;
    protected float lifeTime = 0f;

    //froceMeter
    /*
    public GraphicObject(Texture texture,float width, float height, MainGame mainGame) {
        super(texture);
        this.mainGame = mainGame;
        this.setSize(width,height);
        this.setOriginCenter();
    }

     */

    public GraphicObject(Texture texture,float width, float height, MainGame mainGame) {
        super(texture);
        this.mainGame = mainGame;
        this.setSize(width,height);
        this.setOriginCenter();
    }

    //for healthbar


    //for clouds
    public GraphicObject(Texture texture, MainGame mainGame) {
        super(texture);
        this.mainGame = mainGame;
        this.setOriginCenter();
    }

    //sun
    public GraphicObject(MainGame mainGame) {
        super();
        this.mainGame = mainGame;
        this.setOriginCenter();
    }

    public void update () {
        float delta = Gdx.graphics.getDeltaTime();
        lifeTime += delta;
        stateTime += Gdx.graphics.getDeltaTime();
    }

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
