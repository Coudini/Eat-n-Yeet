package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
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
    public GameScreen gameScreen;
    public StartScreen startScreen;

    protected float stateTime;
    protected float lifeTime = 0f;

    //froceMeter
    public GraphicObject(Texture texture,float width, float height, GameScreen game) {
        super(texture);
        this.gameScreen = game;
        this.setSize(width,height);
        this.setOriginCenter();
    }

    public GraphicObject(Texture texture,float width, float height, StartScreen screen) {
        super(texture);
        this.startScreen = screen;
        this.setSize(width,height);
        this.setOriginCenter();
    }

    //for clouds
    public GraphicObject(Texture texture, GameScreen game) {
        super(texture);
        this.gameScreen = game;
        this.setOriginCenter();
    }

    //sun
    public GraphicObject(GameScreen game) {
        super();
        this.gameScreen = game;
        this.setOriginCenter();
    }

    public void update () {
        float delta = Gdx.graphics.getDeltaTime();
        lifeTime += delta;
    }

    public void render(Batch batch) {

        if (getTexture() == null && currentAnimation == null ) {
            return;
        }
        stateTime += Gdx.graphics.getDeltaTime();

        if (currentAnimation != null) {
            currentFrameTexture = currentAnimation.getKeyFrame(stateTime, true);
            this.setRegion(currentFrameTexture);
        }

        draw(batch);

    }


}
