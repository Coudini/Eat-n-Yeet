package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.FixtureDef;

import java.util.concurrent.Callable;

public class GameObject extends Sprite {
    Sound soundEffect;
    Animation<TextureRegion> currentAnimation;
    TextureRegion currentFrameTexture;


    // used for animation, this must be refreshed with deltatime before calling draw method
    protected float stateTime;
    public Body body;
    private float density = 0.3f;
    private float restitution = 0f;
    private float friction = 0f;

    public MainGame game;

    protected float lifeTime = 0f;

    protected static final short DEFAULT_BITS = 0x0001;
    protected static final short PLAYER_BITS = 0x0002;
    protected static final short FLINGABLE_BITS = 0x0004;

    public GameObject(Texture texture, float x, float y, float width, float height, MainGame game) {
        super(texture);
        this.game = game;
        this.setSize(width,height);
        this.setCenter(x,y);
        this.setOriginCenter();
    }
    public GameObject(Texture texture,float width, float height, Body body, MainGame game) {
        super(texture);
        this.game = game;
        this.body = body;
        this.body.setUserData(this);
        this.setSize(width,height);
        this.setCenter(body.getPosition().x,body.getPosition().y);
        this.setOriginCenter();
    }
    public GameObject( float x, float y, float width, float height, MainGame game) {
        this.game = game;
        this.setSize(width,height);
        this.setCenter(x,y);
        this.setOriginCenter();
    }
    public GameObject(float width, float height, Body body, MainGame game) {
        this.game = game;
        this.body = body;
        this.body.setUserData(this);
    }


    //forcemeter gameobject
    public GameObject(Texture texture, MainGame game) {
        super(texture);
        this.game = game;
        //temporAL
        this.setSize(5f,5f);
        this.setOriginCenter();
    }


   /* // This can be used if sprite only has one texture animation, otherwise use the version that returns Animation<TextureRegion>
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

    */

    // Use this to create many different animations for same object
    public Animation<TextureRegion> createTextureAnimation(int cols, int rows, Texture texture) {
        Animation<TextureRegion> temp;

        // Calculate the tile width from the sheet
        int tileWidth = texture.getWidth() / cols;

        // Calculate the tile height from the sheet
        int tileHeight = texture.getHeight() / rows;

        // Create 2D array from the texture (REGIONS of a TEXTURE).
        TextureRegion[][] tmp = TextureRegion.split(texture, tileWidth, tileHeight);

        // Transform the 2D array to 1D
        TextureRegion[] allFrames = toTextureArray( tmp, cols, rows );

        temp = new Animation(6 / 60f, allFrames);
        return  temp;
    }

    // TODO not in use yet
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

        if (this.body != null){
            this.setCenter(body.getPosition().x,body.getPosition().y);
            this.setRotation(body.getTransform().getRotation() * MathUtils.radiansToDegrees);
        }


            draw(batch);

    }

    public Body createBody(float x, float y, float radius) {
        Body tempBody = game.world.createBody(getDefinitionOfBody(x, y));

        tempBody.createFixture(getFixtureDefinition(radius));
        tempBody.setUserData(this);


        return tempBody;
    }
    protected BodyDef getDefinitionOfBody(float x, float y) {
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
        playerFixtureDef.density = density;

        // How bouncy object? Very bouncy [0,1]
        playerFixtureDef.restitution = restitution;

        // How slipper object? [0,1]
        playerFixtureDef.friction = friction;

        // Create circle shape.
        CircleShape circleshape = new CircleShape();
        circleshape.setRadius(radius);

        // Add the shape to the fixture
        playerFixtureDef.shape = circleshape;

        return playerFixtureDef;
    }
    public void flip(Animation<TextureRegion> animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(true, false);
        }
    }

    /**
     * Default on collision, its empty since by default you do not need to do anything.
     * Overwrite this method on subclass to define what object should do when colliding with other object
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
    public void onCollision(Contact contact, GameObject other) {

    }

    /**
     * Default end collision, its empty since by default you do not need to do anything.
     * Overwrite this method on subclass to define what object should do when colliding ends with other object
     * @param contact
     * @param other can be used to check what class it was colliding with
     */
    public void endCollision (Contact contact, GameObject other) {

    }

    public void callAfterPhysicsStep (Callable<Void> toBeCalled) {
        game.functionsToBeCalled.add(toBeCalled);
    }


    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

}
