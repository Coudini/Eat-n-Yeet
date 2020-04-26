package fi.tiko.eatnyeet;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;

import java.util.ArrayList;

import static com.badlogic.gdx.Gdx.audio;
import static com.badlogic.gdx.Gdx.files;

/**
 * Field for the crops to grow, only one should be created
 */
public class Field extends GameObject {

    public static Texture empty;

    static float fillLevel;
    float maxFill = 9f;
    float timeWhenPreviousCrop;

    protected GameObject [] fieldObjectArray;

    private float [] fieldPosX;
    private float [] fieldPosY;

    Sound sound;

    /**
     * Default constructor with default values, values are generated from tilemap for easy scaling.
     * @param width size from tilemap
     * @param height size from tilemap
     * @param body body for collision detection
     * @param game needed for superclass
     */
    public Field(float width, float height, Body body , GameScreen game) {
        super(empty, width,height, body, game);
        timeWhenPreviousCrop = 0f;
        fieldObjectArray = new GameObject[9];
        fieldPosX = new float [9];
        fieldPosY = new float[9];
        defineFielPos();
        fillLevel = maxFill;
        growRandomFood(maxFill);

        sound = audio.newSound(files.internal("score.mp3"));
    }

    /**
     * Called on every iteration, calls method and does if checking for the object.
     */
    @Override
    public void update () {
        super.update();
        updateFieldObjects();
    }

    /**
     * Class spesific render because fieldobjects needs special attention.
     * @param batch
     */
    @Override
    public void render(Batch batch) {
        super.render(batch);
        renderFieldObjects(batch);
    }

    /**
     * Updates crops from the field
     */
    public void updateFieldObjects() {
        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {

            } else {
                fieldObjectArray[i].update();
            }
        }
    }

    /**
     * Renders crops from the field
     * @param batch
     */
    public void renderFieldObjects(Batch batch) {
        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {

            } else {
                fieldObjectArray[i].render(batch);
            }

        }
    }

    /**
     * Called in constructor to create positions for field objects, basially calculates the field to become 3 x 3 slot area
     */
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
     * Throws object based on what object got removed from field
     */
    public FlingableObject giveRandomFood() {

            fillLevel -= 1f;
            float fieldPosY = body.getPosition().y + 0.4f;
            float fieldPosX = body.getPosition().x;

            ArrayList<Integer> listOfUsedIndexes = checkUsedIndexOfObjectArray();
            int index = listOfUsedIndexes.get(MathUtils.random(0,listOfUsedIndexes.size()-1));

            if (fieldObjectArray[index] instanceof Melon) {
                Melon temp = new Melon(fieldPosX, fieldPosY, game);
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

    /**
     * When 2 bodies collides, onCollision is automatically called. Overide onCollision to do class spesific tasks
     * @param contact
     * @param other can be used to check what class it is colliding with
     */
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
                // could add message that informs user about field is full
            } else {
                fillLevel += ((CompostWaste) other).getFillAmount();
                if (game.player.characterCombo == 0) {
                    game.player.characterScore += 1;
                } else {
                    game.player.characterScore += (int) ((CompostWaste) other).flyTime * game.player.characterCombo;
                }
                game.player.characterCombo += 1;
                growRandomFood(((CompostWaste)other).getFillAmount());
            }
            if (game.sounds) {
                sound.play(0.2f);
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
                int tempN = MathUtils.random(1,3);

                if (tempN == 1) {
                    Melon temp = new Melon(fieldPosX[index], fieldPosY[index],0.3f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
                else if (tempN == 2) {
                    Tomato temp = new Tomato(fieldPosX[index], fieldPosY[index],0.3f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
                else if (tempN == 3) {
                    Carrot temp = new Carrot(fieldPosX[index], fieldPosY[index],0.3f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectArray[index] = temp;
                }
            }

            return null;
        });
    }


    /**
     * Checks free spots from field, and stores them to array which is used to randomize the crop spawn
     * @return arraylist of free spots
     */
    private ArrayList<Integer> checkFreeIndexOfObjectArray() {
        ArrayList<Integer> temp = new ArrayList<>();

        for (int i = 0; i < fieldObjectArray.length; i++) {
            if (fieldObjectArray[i] == null) {
                temp.add(i);
            }
        }
        return temp;
    }

    /**
     * Checks which slots are in use from field
     * @return arraylist of thos slots
     */
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
