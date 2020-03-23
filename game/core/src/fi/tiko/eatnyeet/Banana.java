package fi.tiko.eatnyeet;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;



public class Banana extends GameObject implements Flingable, Food {
    public static Texture texture;

    private float fillAmount = 1f;
    private boolean spawnComplete = false;

    public Banana(float posX, float posY, MainGame game) {
        super(texture, posX, posY, 0.9f, 0.9f, game);
        setDensity(0.8f);
        setFriction(4.5f);
        setRestitution(0.2f);
        body = createBody(posX,posY,0.45f);
        float randY = MathUtils.random(2f,4f);
        float randX = MathUtils.random(-3f,3f);
        body.setLinearVelocity(randX,randY);
        body.setGravityScale(0.2f);
        allowPlayerCollision();

        //soundEffect = Gdx.audio.newSound(Gdx.files.internal("pew.mp3"));
    }


    @Override
    public void update() {
        super.update();
        flyTimeUpdate();

        if (!spawnComplete) {
            if (isBeingCarried || isOnFloor) {
                spawnComplete = true;
                resetGraityScale();
            }
        }
    }

    public void ignorePlayerCollision() {
        Filter filter = new Filter();
        filter.categoryBits = FOOD_BITS;
        filter.maskBits = DEFAULT_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }
    public void allowPlayerCollision(){
        Filter filter = new Filter();
        filter.categoryBits = FOOD_BITS;
        filter.maskBits = DEFAULT_BITS | PLAYER_BITS;
        for (Fixture fix: body.getFixtureList()) {
            fix.setFilterData(filter);
        }
    }

    public void setFillAmount (float fillAmount) {
        this.fillAmount = fillAmount;
    }
    public float getFillAmount() {
        return 1f;
    }

}
