package fi.tiko.eatnyeet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

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

    Animation<TextureRegion> run;


    //public static Texture carrotEaten;
    //public static Texture tomatoEaten;

    public Customer(MainGame game) {
        super(customerTexture, 15f,5f,1.5f,1.5f,game);

        run = Util.createTextureAnimation(8,1, customerRun);
        Util.flip(run);
        body = createBody(16f,4f,0.45f);
        body.setGravityScale(0);


        Filter filter = new Filter();
        filter.categoryBits = OTHER_BITS;
        filter.maskBits = OTHER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    @Override
    public void update() {
        super.update();
        currentAnimation = run;
        move();
        updateObjectToCarry();

        // stop move for 2s before throwing
        /*
        if (lifeTime - timeWhenPickedUp > randomThrowTime - 1f && isCarryingFlingable) {
            stopMove(1.5f);
        }

         */
        if (lifeTime - timeWhenPickedUp > randomThrowTime && isCarryingFlingable) {
            if (objectToCarry instanceof Carrot) {

                objectToCarry.setTexture(((Carrot) objectToCarry).carrotEaten);
                objectToCarry.setSize(0.3f,0.3f);

            }
            else if (objectToCarry instanceof Tomato) {
                objectToCarry.setTexture(((Tomato) objectToCarry).tomatoEaten);
                objectToCarry.setSize(0.3f,0.3f);
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
            System.out.println("customer collision added");
        }
    }
    private void move () {

        if (allowMove) {
            if (!pickedUpFood) {
                moveTowardsPoint(fieldPoint);
            } else {
                moveTowardsPoint(moveAwayPoint);
            }
        }
        // stopmove ends here
        else if (lifeTime - timeWhenStopped > waitTime) {
            System.out.println("gay");
            allowMove = true;
        }


    }
    public void killYourSelf () {
        game.toBeDeleted.add(this);
    }
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

            //body.applyForce(force, body.getWorldCenter(), true);
            body.applyForceToCenter(force,true);

    }
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof Field) {
            System.out.println("field");
            pickedUpFood = true;
            isCarryingFlingable = true;

            callAfterPhysicsStep(() ->{
                objectToCarry = ((Field) other).giveRandomFood();
                objectToCarry.ignorePlayerCollision();
                objectToCarry.isBeingCarried = true;
                objectToCarry.body.setGravityScale(0.3f);
                timeWhenPickedUp = lifeTime;
                //stopMove(2f);
                return null;
            });
        }
    }
    private void stopMove(float timeSeconds) {
        timeWhenStopped = lifeTime;
        allowMove = false;
        waitTime = timeSeconds;
        body.setLinearVelocity(0f,0f);
    }
    private void updateObjectToCarry () {

        if (isCarryingFlingable) {
            float xModif = -0.37f;
            float yModif = 0.045f;

            /*if (isRight) {
                xModif = 0.37f;
            }

             */

            // set body location to player body location -+ x and y modifiers
            objectToCarry.body.setTransform(this.body.getPosition().x + xModif,this.body.getPosition().y + yModif,0f);
        }
    }
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
    }

}
