package fi.tiko.eatnyeet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;

public class Customer extends GameObject {

    private float speed = 30f;
    public Vector2 fieldPoint = new Vector2(14f, 2f);
    public Vector2 moveAwayPoint = new Vector2(1f,3.3f);
    public boolean pickedUpFood = false;
    public boolean spawnComplete = false;
    public static Texture customerTexture;
    public Customer(MainGame game) {
        super(customerTexture, 15f,5f,1f,1f,game);

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
        move();

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

        if (!pickedUpFood) {
            moveTowardsPoint(fieldPoint);
        } else {
            moveTowardsPoint(moveAwayPoint);
        }

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

        }



    }

}
