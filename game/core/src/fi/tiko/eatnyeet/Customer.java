package fi.tiko.eatnyeet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

/**
 * Autopiloted customers for the game, that picks up food, eats and throws them away.
 */
public class Customer extends GameObject {

    protected FlingableObject objectToCarry;
    protected boolean isCarryingFlingable = false;
    private float timeWhenPickedUp;
    private float timeWhenStopped;
    private float waitTime;
    private boolean allowMove = true;
    private float randomThrowTime = MathUtils.random(7f,13f);
    private float speed = 30f;
    public Vector2 fieldPoint = new Vector2(14f, 2f);
    public Vector2 moveAwayPoint = new Vector2(-1f,3.3f);
    public boolean pickedUpFood = false;
    public boolean spawnComplete = false;

    public static Texture customerTexture;

    public static Texture customerRun;
    public static Texture customerRun2;
    public static Texture customerRun3;

    Animation<TextureRegion> run;

    Sound field;
    Sound yeet;

    /**
     * Default constructor for spawning customer, since they are autopiloted most of the time default one will do.
     * @param game saved to superclass
     */
    public Customer(GameScreen game) {
        super(customerTexture, 15f,5f,1.5f,1.5f,game);
        int temp = MathUtils.random(0,2);
        if (temp == 1) {
            run = Util.createTextureAnimation(8,1, customerRun);
        }
        else if(temp == 2) {
            run = Util.createTextureAnimation(8, 1, customerRun3);
        } else {
            run = Util.createTextureAnimation(8,1, customerRun2);
        }

        Util.flip(run);
        body = createBody(16f,4f,0.45f);
        body.setGravityScale(0);


        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = OTHER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
        field = audio.newSound(files.internal("field.mp3"));
        yeet = audio.newSound(files.internal("bite.mp3"));
    }

    /**
     * Called on every iteration, calls methods and does if checking needed for the class.
     */
    @Override
    public void update() {
        super.update();
        currentAnimation = run;
        move();
        updateObjectToCarry();

        if (lifeTime - timeWhenPickedUp > randomThrowTime && isCarryingFlingable) {
            if (objectToCarry instanceof Carrot) {

                objectToCarry.setTexture(((Carrot) objectToCarry).carrotEaten);
                objectToCarry.setSize(0.4f,0.4f);

            }
            else if (objectToCarry instanceof Tomato) {
                objectToCarry.setTexture(((Tomato) objectToCarry).tomatoEaten);
                objectToCarry.setSize(0.55f,0.35f);
            }
            else if (objectToCarry instanceof Melon) {
                objectToCarry.setTexture(((Melon) objectToCarry).melonEaten);
                objectToCarry.setSize(0.6f,0.4f);
            }
            throwObjectToCarry();
        }

        if (getX() < 0f) {
            killYourSelf();
        }
        if (lifeTime > 2f && !spawnComplete) {
            Filter filter = new Filter();
            filter.categoryBits = OTHER_BITS;
            filter.maskBits = DEFAULT_BITS;
            for (Fixture fix: body.getFixtureList()) {
                fix.setFilterData(filter);
            }
            spawnComplete =true;
        }
    }

    /**
     * Moves the customer
     */
    private void move () {

        if (allowMove) {
            if (!pickedUpFood) {
                moveTowardsPoint(fieldPoint);
            } else {
                moveTowardsPoint(moveAwayPoint);
            }
        }
        else if (lifeTime - timeWhenStopped > waitTime) {
            allowMove = true;
        }

    }

    /**
     * Deletes customer, called when it needs to be deleted which is usually when it reaches its end destination aka outside of the screen.
     */
    public void killYourSelf () {
        game.toBeDeleted.add(this);
    }

    /**
     * Moves customer toward point
     * @param point carries information where customer needs to walk to
     */
    private void moveTowardsPoint(Vector2 point) {

            float delta = Gdx.graphics.getDeltaTime();
            //Target position in world coordinates

            //target speed
            float speedToUse = speed * delta;

            //direction
            Vector2 direction = new Vector2(point.x - body.getPosition().x, point.y - body.getPosition().y).nor();

            //distance
            float distanceToTravel = direction.nor().len2();
            

            float distancePerTimestep = speedToUse / 60.0f;
            if (distancePerTimestep > distanceToTravel)
                speedToUse *= (distanceToTravel / distancePerTimestep);


            Vector2 desiredVelocity = direction.scl(speedToUse);
            Vector2 changeInVelocity = desiredVelocity.sub(body.getLinearVelocity());

            Vector2 force = new Vector2(changeInVelocity.scl(body.getMass() *60f));

            body.applyForceToCenter(force,true);

    }

    /**
     * When 2 bodies collides, onCollision is automatically called. Overide onCollision to do class spesific tasks.
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof Field) {
            pickedUpFood = true;
            isCarryingFlingable = true;

            callAfterPhysicsStep(() ->{
                objectToCarry = ((Field) other).giveRandomFood();
                objectToCarry.ignorePlayerCollision();
                objectToCarry.isBeingCarried = true;
                objectToCarry.body.setGravityScale(0.3f);
                timeWhenPickedUp = lifeTime;
                return null;
            });
            if (game.sounds) {
                field.play(0.3f);
            }
        }
    }

    /**
     * Stops customer for set amount of time
     * @param timeSeconds
     */
    private void stopMove(float timeSeconds) {
        timeWhenStopped = lifeTime;
        allowMove = false;
        waitTime = timeSeconds;
        body.setLinearVelocity(0f,0f);
    }

    /**
     * When customer is holding object, this keeps the object in their "hand". Call it on every iteration.
     */
    private void updateObjectToCarry () {

        if (isCarryingFlingable) {
            float xModif = -0.37f;
            float yModif = 0.045f;

            // set body location to player body location -+ x and y modifiers
            objectToCarry.body.setTransform(this.body.getPosition().x + xModif,this.body.getPosition().y + yModif,0f);
        }
    }

    /**
     * Throws the object from hand to random direction with random force.
     */
    public void throwObjectToCarry () {
        float speedX = MathUtils.random(3f,-3f);
        float speedY = MathUtils.random(5f,7f);
        objectToCarry.body.setLinearVelocity(speedX, speedY);
        objectToCarry.body.applyAngularImpulse(Math.signum(speedX) * -0.1f, true);

        isCarryingFlingable = false;

        objectToCarry.timeWhenThrown = objectToCarry.lifeTime;
        objectToCarry.isBeingCarried = false;
        objectToCarry.isJustThrown = true;

        objectToCarry = null;
        if (game.sounds) {
            yeet.play(0.3f);
        }
    }

}
