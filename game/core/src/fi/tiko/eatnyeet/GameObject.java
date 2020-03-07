package fi.tiko.eatnyeet;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class GameObject extends Sprite {
    Sound soundEffect;
    Animation<TextureRegion> textureAnimation;
    TextureRegion currentFrameTexture;
    protected float stateTime;
    public Body body;

    private MainGame game;


    public GameObject(Texture texture, float x, float y, float width, float height, MainGame game) {
        super(texture);
        this.game = game;
        this.setSize(width,height);
        this.setCenter(x,y);
        this.setOriginCenter();
    }

    public void createTextureAnimation(int cols, int rows) {

        // Calculate the tile width from the sheet
        int tileWidth = this.getTexture().getWidth() / cols;

        // Calculate the tile height from the sheet
        int tileHeight = this.getTexture().getHeight() / rows;

        // Create 2D array from the texture (REGIONS of a TEXTURE).
        TextureRegion[][] tmp = TextureRegion.split(this.getTexture(), tileWidth, tileHeight);

        // Transform the 2D array to 1D
        TextureRegion[] allFrames = toTextureArray( tmp, cols, rows );

        textureAnimation = new Animation(6 / 60f, allFrames);

        currentFrameTexture = textureAnimation.getKeyFrame(stateTime, true);
    }

    public static TextureRegion[] toTextureArray( TextureRegion [][]tr, int cols, int rows ) {
        TextureRegion [] frames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                frames[index++] = tr[i][j];
            }
        }
        return frames;
    }

    public void update() {

    }
    public void render(Batch batch) {
        if (getTexture() == null) {
            return;
        }
        batch.draw(getTexture(),
                body.getPosition().x - 1f,
                body.getPosition().y - 1f,
                1f,                   // originX
                1f,                   // originY
                1f * 2,               // width
                1f * 2,               // height
                0.6f,                          // scaleX
                0.6f,                          // scaleY
                body.getTransform().getRotation() * MathUtils.radiansToDegrees,
                0,                             // Start drawing from x = 0
                0,                             // Start drawing from y = 0
                getTexture().getWidth(),       // End drawing x
                getTexture().getHeight(),      // End drawing y
                false,                         // flipX
                false);
    }

    public Body createBody(float x, float y, float radius) {
        Body tempBody = game.world.createBody(getDefinitionOfBody(x, y));
        tempBody.createFixture(getFixtureDefinition(radius));
        return tempBody;
    }
    private BodyDef getDefinitionOfBody(float x, float y) {
        // Body Definition
        BodyDef myBodyDef = new BodyDef();

        // It's a body that moves
        myBodyDef.type = BodyDef.BodyType.DynamicBody;

        // Initial position is centered up
        // This position is the CENTER of the shape!
        myBodyDef.position.set(x, y);

        return myBodyDef;
    }
    private FixtureDef getFixtureDefinition(float radius) {
        FixtureDef playerFixtureDef = new FixtureDef();

        // Mass per square meter (kg^m2)
        playerFixtureDef.density = 0.3f;

        // How bouncy object? Very bouncy [0,1]
        playerFixtureDef.restitution = 0.1f;

        // How slipper object? [0,1]
        playerFixtureDef.friction = 0.1f;

        // Create circle shape.
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(radius);

        // Add the shape to the fixture
        playerFixtureDef.shape = circleshape;

        return playerFixtureDef;
    }

}
