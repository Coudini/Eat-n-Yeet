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
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.util.concurrent.Callable;

/**
 * Important class for the game, all of the objects that has body or needs to have body extends this class.
 */
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

    public GameScreen game;

    protected float lifeTime = 0f;

    protected static final short DEFAULT_BITS = 0x0001;
    protected static final short PLAYER_BITS = 0x0002;
    protected static final short FLINGABLE_BITS = 0x0004;
    protected static final short OTHER_BITS = 0x0008;

    /**
     * Constuctor that gets all of the value except body as argument
     * @param texture texture
     * @param x position
     * @param y position
     * @param width size
     * @param height size
     * @param game gamescreen so objects can access mainclass
     */
    public GameObject(Texture texture, float x, float y, float width, float height, GameScreen game) {
        super(texture);
        this.game = game;
        this.setSize(width,height);
        this.setCenter(x,y);
        this.setOriginCenter();
    }

    /**
     * Constructor that gets all values exepct poisiton as agrument, position is calculated from body.
     * @param texture texture
     * @param width size
     * @param height size
     * @param body body + position
     * @param game gamescreen so objects can access mainclass
     */
    public GameObject(Texture texture,float width, float height, Body body, GameScreen game) {
        super(texture);
        this.game = game;
        this.body = body;
        this.body.setUserData(this);
        this.setSize(width,height);
        this.setCenter(body.getPosition().x,body.getPosition().y);
        this.setOriginCenter();
    }

    /**
     * Constructor that gets most values as agrument, missing texture and body.
     * @param x position
     * @param y position
     * @param width size
     * @param height size
     * @param game gamescreen so objects can access mainclass
     */
    public GameObject( float x, float y, float width, float height, GameScreen game) {
        this.game = game;
        this.setSize(width,height);
        this.setCenter(x,y);
        this.setOriginCenter();
    }

    /**
     * Constructor that gets size, body and Gamescreen as argument
     * @param width size
     * @param height size
     * @param body body
     * @param game gamescreen so objects can access mainclass
     */
    public GameObject(float width, float height, Body body, GameScreen game) {
        this.game = game;
        this.body = body;
        this.body.setUserData(this);
    }


    /**
     * Called on every iteration, classes that extends gameobject should override this, but also call this so their lifetime and statetime gets updated.
     */
    public void update () {
        float delta = Gdx.graphics.getDeltaTime();
        lifeTime += delta;
        stateTime += Gdx.graphics.getDeltaTime();
    }


    /**
     * Default render which works for most gameobjects, overide if class needs special attention
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

        this.setCenter(body.getPosition().x,body.getPosition().y);
        this.setRotation(body.getTransform().getRotation() * MathUtils.radiansToDegrees);

        draw(batch);

    }

    /**
     * If body is not created before, gameobject can create it for you with x and y and radius value
     * it calls couple other methods to create the body
     * @param x position
     * @param y position
     * @param radius size
     * @return complete body
     */
    public Body createBody(float x, float y, float radius) {
        Body tempBody = game.world.createBody(getDefinitionOfBody(x, y));

        tempBody.createFixture(getFixtureDefinition(radius));
        tempBody.setUserData(this);

        return tempBody;
    }

    /**
     * When creating body call this and pass x and y value to it
     * @param x
     * @param y
     * @return bodyDef
     */
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


    /***
     * When creating bodu, call this and pass radius to this
     * @param radius
     * @return fixtureDef
     */
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
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(1f,1f);


        // Add the shape to the fixture
        playerFixtureDef.shape = circleshape;
        //playerFixtureDef.shape = polygonShape;

        return playerFixtureDef;
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

    /**
     * Saves callable methods or tasks program need to run later on. Purpose is to avoid conflicts with physicsStep so you can use this to make your program do the tasks after it.
     * Good example is deleting bodies from world, that must be done after physicsstep
     * @param toBeCalled Method / code that will run after physic step
     */
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
