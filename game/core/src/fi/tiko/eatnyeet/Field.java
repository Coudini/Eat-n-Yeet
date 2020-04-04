package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Field extends GameObject {

    public static Texture empty;
    public static Texture fill1;
    public static Texture fill2;
    public static Texture fill3;
    public static Texture fill4;
    public static Texture fill5;
    public static Texture fill6;
    public static Texture fill7;
    public static Texture fill8;
    public static Texture fill9;
    static float fillLevel;
    float maxFill = 9f;
    float timeWhenPreviousCrop;

    protected GameObject [] fieldObjectArray;

    private float [] fieldPosX;
    private float [] fieldPosY;


    public Field(float width, float height, Body body , MainGame game) {
        super(empty, width,height, body, game);
        timeWhenPreviousCrop = 0f;
        fieldObjectArray = new GameObject[9];
        fieldPosX = new float [9];
        fieldPosY = new float[9];
        defineFielPos();
        fillLevel = maxFill;
        growRandomFood(maxFill);

    }

    @Override
    public void update () {
        super.update();
        updateFieldObjects();
        float currentPercent = fillLevel / maxFill;

        if (currentPercent > 0.05f) {
            cropCrops();
        }




    }
    @Override
    public void render(Batch batch) {
        super.render(batch);
        renderFieldObjects(batch);
    }

    public void updateFieldObjects() {
        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {

            } else {
                fieldObjectArray[i].update();
            }
        }
    }
    public void renderFieldObjects(Batch batch) {
        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {

            } else {
                fieldObjectArray[i].render(batch);
            }

        }
    }
    public void defineFielPos() {
        float startX;
        float startY = this.getY() - this.getHeight()/3f/2f;

        float [][] fieldPositionsX = new float[3][3];
        float [][] fieldPositionsY = new float[3][3];

        for (int i = 0; i < fieldPositionsX.length; i++) {
            startX = this.getX() +this.getWidth()/3f/2f;
            startY += this.getHeight() / 3f;
            for (int j = 0; j < fieldPositionsX[i].length; j++) {
                fieldPositionsX[i][j] = startX;
                fieldPositionsY[i][j] = startY;
                startX += this.getWidth() / 3f;
            }
        }
        fieldPosX = Util.toOneDimensonalArray(fieldPositionsX);
        fieldPosY = Util.toOneDimensonalArray(fieldPositionsY);
    }

    /**
     * Placeholder for customer, keeps track of time and throws object every 5 second
     */
    public void cropCrops() {
        
        if (lifeTime - timeWhenPreviousCrop > 5f) {
            timeWhenPreviousCrop = lifeTime;
            //fillLevel -= 1f;
            //throwObject();
            System.out.println(fillLevel);
            //temporal rat spawn
                // purkka maanantain demoa varten:D
                boolean spawnrat = true;
                for (GameObject obj : game.gameObjects) {
                    if (obj instanceof Rat) {
                        spawnrat = false;
                    }
                }
                if (spawnrat) {
                    spawnRat();
                }
        }
    }
    public void spawnRat() {
        System.out.println("RAT");
        callAfterPhysicsStep(() -> {
            float posY = 1f;
            float posX = 2f;


            Rat temp = new Rat(posX, posY, game);
            //temp.body.setLinearVelocity(randX,randY);
            game.gameObjects.add(temp);

            return null;
        });
    }

    /**
     * Throws object based on what object got removed from field
     */
    public FlingableObject giveRandomFood() {

            fillLevel -= 1f;
            float fieldPosY = body.getPosition().y + 0.4f;
            float fieldPosX = body.getPosition().x;

            ArrayList<Integer> listOfUsedIndexes = checkUsedIndexOfObjectArray();
            int index = listOfUsedIndexes.get(MathUtils.random(0,listOfUsedIndexes.size()-1));

            if (fieldObjectArray[index] instanceof Banana) {
                Banana temp = new Banana(fieldPosX, fieldPosY, game);
                game.gameObjects.add(temp);

                game.toBeDeleted.add(fieldObjectArray[index]);
                fieldObjectArray[index] = null;
                return temp;

            }
            else if (fieldObjectArray[index] instanceof Tomato) {
                Tomato temp = new Tomato(fieldPosX, fieldPosY, game);
                game.gameObjects.add(temp);

                game.toBeDeleted.add(fieldObjectArray[index]);
                fieldObjectArray[index] = null;
                return temp;
            }
            else if (fieldObjectArray[index] instanceof Carrot) {
                Carrot temp = new Carrot(fieldPosX, fieldPosY, game);
                game.gameObjects.add(temp);

                game.toBeDeleted.add(fieldObjectArray[index]);
                fieldObjectArray[index] = null;
                return temp;
            }
            return null;
    }
    @Override
    public void onCollision(Contact contact, GameObject other) {

        if (other != null && other instanceof CompostWaste && other instanceof FlingableObject) {
            game.toBeDeleted.add(other);


            // if character carries the object to field this will reset character object reference and booleans
            if (((CompostWaste) other).isBeingCarried) {
                callAfterPhysicsStep(() -> {
                    ((CompostWaste) other).isBeingCarried = false;
                    game.player.resetObjectToCarry();
                    return null;
                });
            }
            if (fillLevel >= maxFill) {
                fillLevel = maxFill;
                System.out.println("field already full");
            } else {
                fillLevel += ((CompostWaste) other).getFillAmount();
                game.player.characterScore += (int) ((CompostWaste) other).flyTime * game.player.characterCombo;
                game.player.characterCombo += 1;
                growRandomFood(((CompostWaste)other).getFillAmount());
            }

        }



    }

    /**
     * Grows random crops to field, checks if slot is empty before doing so
     * @param amount how many crops needs to be grown
     */
    protected void growRandomFood (float amount) {

        callAfterPhysicsStep(() -> {

            for (int i = 0; i < amount; i++) {

                ArrayList<Integer> listOfFreeIndexes = checkFreeIndexOfObjectArray();
                int index = listOfFreeIndexes.get(MathUtils.random(0,listOfFreeIndexes.size()-1));
                int tempN = MathUtils.random(2,3);

                if (tempN == 1) {
                    Banana temp = new Banana(fieldPosX[index], fieldPosY[index],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
                else if (tempN == 2) {
                    Tomato temp = new Tomato(fieldPosX[index], fieldPosY[index],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
                else if (tempN == 3) {
                    Carrot temp = new Carrot(fieldPosX[index], fieldPosY[index],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
            }

            return null;
        });
    }
    private ArrayList<Integer> checkFreeIndexOfObjectArray() {
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {
                temp.add(i);
            }
        }
        return temp;
    }
    private ArrayList<Integer> checkUsedIndexOfObjectArray() {
        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] != null) {
                temp.add(i);
            }
        }
        return temp;
    }

    public static float getFillLevel() {
        return fillLevel;
    }

}
