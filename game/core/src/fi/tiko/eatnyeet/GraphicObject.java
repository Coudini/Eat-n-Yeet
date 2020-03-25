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
    public MainGame game;

    protected float stateTime;
    protected float lifeTime = 0f;

    //froceMeter
    public GraphicObject(Texture texture,float width, float height, MainGame game) {
        super(texture);
        this.game = game;
        this.setSize(width,height);
        this.setOriginCenter();
    }

    //for clouds
    public GraphicObject(Texture texture, MainGame game) {
        super(texture);
        this.game = game;
        this.setSize(5f, 5f);
        this.setOriginCenter();
        System.out.println("Graphic Object cloud constructor. size height= " + this.getHeight() + " width = " + this.getWidth());
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
