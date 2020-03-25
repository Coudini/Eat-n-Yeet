package fi.tiko.eatnyeet;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class GameWorld  {

    public TiledMap tiledMap;
    public TiledMapRenderer tiledMapRenderer;
    public float UNIT_SCALE = 100f;
    private MainGame game;
    public GameWorld (MainGame game) {
        this.game = game;
        game.world = new World(new Vector2(0f, -10.8f), true);
        tiledMap = new TmxMapLoader().load("map2.0.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / UNIT_SCALE);


        transformWallsToBodies("wall-rectangles", "wall");
        transformWallsToBodies("compost-rectangles", "compost");
        transformWallsToBodies("field-rectangles", "field");


        game.world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                GameObject userDataA = null;
                GameObject userDataB = null;

                try {
                    userDataA = (GameObject) (contact.getFixtureA().getBody().getUserData());

                } catch (Exception e) {

                }

                try {
                    userDataB = (GameObject) (contact.getFixtureB().getBody().getUserData());

                } catch (Exception e) {

                }


                if (userDataA != null) {
                    userDataA.onCollision(contact,userDataB);
                }

                if (userDataB != null) {
                    userDataB.onCollision(contact,userDataA);
                }

            }

            @Override
            public void endContact(Contact contact) {
                GameObject userDataA = null;
                GameObject userDataB = null;

                try {
                    userDataA = (GameObject) (contact.getFixtureA().getBody().getUserData());

                } catch (Exception e) {

                }

                try {
                    userDataB = (GameObject) (contact.getFixtureB().getBody().getUserData());

                } catch (Exception e) {

                }


                if (userDataA != null) {
                    userDataA.endCollision(contact,userDataB);
                }

                if (userDataB != null) {
                    userDataB.endCollision(contact,userDataA);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {


            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        });

    }


    public void render(OrthographicCamera camera) {
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    public void transformWallsToBodies(String layer, String userData) {
        MapLayer collisionObjectLayer = tiledMap.getLayers().get(layer);

        // Get all object layer rectangles
        MapObjects mapObjects = collisionObjectLayer.getObjects();

        // Cast it to RectangleObjects array
        Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

        // Iterate all the rectangles
        for (RectangleMapObject rectangleObject : rectangleObjects) {
            Rectangle tmp = rectangleObject.getRectangle();

            // SCALE given rectangle down if using world dimensions!
            Rectangle rectangle = scaleRect(tmp, 1 / UNIT_SCALE);

            Body body =  createStaticBody(rectangle, userData);
            if (rectangleObject.getProperties().get("type") != null && rectangleObject.getProperties().get("type").equals("compostType")) {
                game.gameObjects.add(new Compost(rectangle.getWidth(), rectangle.getHeight(), body,game));
            }
            if (rectangleObject.getProperties().get("type") != null && rectangleObject.getProperties().get("type").equals("fieldType")) {
                game.gameObjects.add(new Field(rectangle.getWidth(), rectangle.getHeight(), body,game));
            }
            if (rectangleObject.getProperties().get("type") != null && rectangleObject.getProperties().get("type").equals("groundType")) {
                game.gameObjects.add(new Ground(rectangle.getWidth(), rectangle.getHeight(), body,game));
            }
        }
    }

    public Body createStaticBody(Rectangle rect, String userData) {
        BodyDef myBodyDef = new BodyDef();
        myBodyDef.type = BodyDef.BodyType.StaticBody;

        float x = rect.getX();
        float y = rect.getY();
        float width = rect.getWidth();
        float height = rect.getHeight();

        float centerX = width/2 + x;
        float centerY = height/2 + y;

        myBodyDef.position.set(centerX, centerY);

        Body wall = game.world.createBody(myBodyDef);

        wall.setUserData(userData);
        // Create shape
        PolygonShape groundBox = new PolygonShape();

        // Real width and height is 2 X this!
        groundBox.setAsBox(rect.getWidth() / 2 , height / 2 );

        if ((wall.getUserData().equals("compost")) || (wall.getUserData().equals("field"))) {
            wall.createFixture(groundBox, 0.0f).setSensor(true);
        } else {
            wall.createFixture(groundBox, 0.0f);
        }
        return wall;

    }


    private Rectangle scaleRect(Rectangle r, float scale) {
        Rectangle rectangle = new Rectangle();
        rectangle.x      = r.x * scale;
        rectangle.y      = r.y * scale;
        rectangle.width  = r.width * scale;
        rectangle.height = r.height * scale;
        return rectangle;
    }

    private double accumulator = 0;
    private float TIME_STEP = 1 / 60f;

    public void doPhysicsStep(float deltaTime) {

        float frameTime = deltaTime;

        // If it took ages (over 4 fps, then use 4 fps)
        // Avoid of "spiral of death"
        if(deltaTime > 1 / 4f) {
            frameTime = 1 / 4f;
        }

        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            // It's a fixed time step!
            game.world.step(TIME_STEP, 8, 3);
            accumulator -= TIME_STEP;
        }
    }

}
