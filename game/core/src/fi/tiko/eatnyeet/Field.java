package fi.tiko.eatnyeet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Manifold;

import java.util.ArrayList;

public class Field extends GameObject {

    // temp
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
    float fillLevel;
    float maxFill = 9f;
    float timeWhenPreviousCrop;

    protected ArrayList<GameObject> fieldObjectList;

    private float [] fieldPosX;
    private float [] fieldPosY;


    public Field(float width, float height, Body body , MainGame game) {
        super(empty, width,height, body, game);
        fillLevel = 10f;
        timeWhenPreviousCrop = 0f;
        fieldObjectList = new ArrayList<>();

        fieldPosX = new float [9];
        fieldPosY = new float[9];
        defineFielPos();
        growRandomFood(maxFill);

    }

    @Override
    public void update () {
        super.update();
        updateFieldObjectList();
        float currentPercent = fillLevel / maxFill;

        if (currentPercent > 0.05f) {
            cropCrops();
        }

        if (currentPercent < 0.1) {
            this.setTexture(empty);
        }
        /*
        else if (currentPercent < 0.2) {
            this.setTexture(fill1);
        } else if (currentPercent < 0.3) {
            this.setTexture(fill2);
        } else if (currentPercent < 0.4) {
            this.setTexture(fill3);
        } else if (currentPercent < 0.5) {
            this.setTexture(fill4);
        }else if (currentPercent < 0.6) {
            this.setTexture(fill5);
        }else if (currentPercent < 0.7) {
            this.setTexture(fill6);
        }else if (currentPercent < 0.8) {
            this.setTexture(fill7);
        }else if (currentPercent < 0.9) {
            this.setTexture(fill8);
        } else {
            this.setTexture(fill9);
        }

         */
        //System.out.println("Current% " + currentPercent + ", fill " + fillLevel + ", max " + maxFill);


    }
    @Override
    public void render(Batch batch) {
        super.render(batch);
        renderFieldObjectList(batch);
    }

    public void updateFieldObjectList() {
        for (GameObject obj: fieldObjectList) {
            obj.update();
        }
    }
    public void renderFieldObjectList(Batch batch) {
        for (GameObject obj: fieldObjectList) {
            obj.render(batch);
        }
    }
    public void defineFielPos() {
        float startX;
        float startY = this.getY() - this.getHeight()/3f/2f;

        float [][] fieldPositionsX;
        float [][] fieldPositionsY;
        fieldPositionsX = new float[3][3];
        fieldPositionsY = new float[3][3];

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
    public void cropCrops() {
        
        if (lifeTime - timeWhenPreviousCrop > 5f) {
            timeWhenPreviousCrop = lifeTime;
            fillLevel -= 1f;
            throwBanana();
System.out.println(fillLevel);
            //temporal rat spawn
            if (fillLevel % 6 == 0.0f) {
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
            temp.body.setGravityScale(0.4f);
            game.gameObjects.add(temp);

            return null;
        });
    }

    public void throwBanana() {
        callAfterPhysicsStep(() -> {
            float fieldPosY = body.getPosition().y + 0.4f;
            float fieldPosX = body.getPosition().x;

            float randY = MathUtils.random(4f,8f);
            float randX = MathUtils.random(-2f,-0.5f);

            int tempN = MathUtils.random(1,3);
            if (tempN == 1) {
                Banana temp = new Banana(fieldPosX, fieldPosY, game);
                temp.body.setLinearVelocity(randX,randY);
                temp.body.setGravityScale(0.4f);
                game.gameObjects.add(temp);
            }
            else if (tempN == 2) {
                Tomato temp = new Tomato(fieldPosX, fieldPosY, game);
                temp.body.setLinearVelocity(randX,randY);
                temp.body.setGravityScale(0.4f);
                game.gameObjects.add(temp);
            }
            else if (tempN == 3) {
                Carrot temp = new Carrot(fieldPosX, fieldPosY, game);
                temp.body.setLinearVelocity(randX,randY);
                temp.body.setGravityScale(0.4f);
                game.gameObjects.add(temp);
            }
            //Banana temp = new Banana(fieldPosX, fieldPosY, game);



            return null;
        });
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
            } else {
                fillLevel += ((CompostWaste) other).getFillAmount();
                game.player.characterScore += (int) ((CompostWaste) other).flyTime * game.player.characterCombo;
                game.player.characterCombo += 1;
                //growRandomFood(((CompostWaste)other).getFillAmount());
            }

            //System.out.println("Field fillevel = " + fillLevel);
        }



    }
    protected void growRandomFood (float amount) {

        callAfterPhysicsStep(() -> {

            for (int i = 0; i < amount; i++) {


                int tempN = MathUtils.random(1,3);


                if (tempN == 1) {
                    //System.out.println(fieldPositionsX[i][i] + "X " + fieldPositionsY[i][i] + " Y");
                    Banana temp = new Banana(fieldPosX[i], fieldPosY[i],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectList.add(temp);
                }
                else if (tempN == 2) {
                    //System.out.println(fieldPositionsX[i][i] + "X " + fieldPositionsY[i][i] + " Y");
                    Tomato temp = new Tomato(fieldPosX[i], fieldPosY[i],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectList.add(temp);
                }
                else if (tempN == 3) {
                    //System.out.println(fieldPositionsX[i][i] + "X " + fieldPositionsY[i][i] + " Y");
                    Carrot temp = new Carrot(fieldPosX[i], fieldPosY[i],0.1f, game);
                    temp.ignorePlayerCollision();
                    temp.body.setGravityScale(0f);
                    fieldObjectList.add(temp);
                }
            }

            return null;
        });
    }
    private void addFieldObjectByIndex(int index) {
        if (index == 0) {

        } else if (index == 1) {

        } else if (index == 2) {

        } else if (index == 3) {

        } else if (index == 4) {

        } else if (index == 5) {

        }
    }
}
