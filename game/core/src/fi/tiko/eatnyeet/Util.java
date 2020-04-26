package fi.tiko.eatnyeet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Util {
    // Use this to create many different animations for same object

    /**
     * Creates an Animation from an image file by dividing it into columns and rows
     * @param cols
     * @param rows
     * @param texture
     * @return animation created from image file
     */
    public static Animation<TextureRegion> createTextureAnimation(int cols, int rows, Texture texture) {
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

    /**
     * Transforms 2 dimensional array into 1 dimensional array containing frames of an animation
     * @param tr 2 dimensional TextureRegion array
     * @param cols
     * @param rows
     * @return 1 dimensional array
     */
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

    /**
     * Flips the animation around when for example Sprite turns around to indicate it visually
     * @param animation
     */
    public static void flip(Animation<TextureRegion> animation) {
        TextureRegion[] regions = animation.getKeyFrames();
        for(TextureRegion r : regions) {
            r.flip(true, false);
        }
    }

    /**
     * Transforms two dimensonal array to one dimensional, NOTE it only works when columns are same size
     * @param twoDimArr
     * @return one dimensional array
     */
    public static float [] toOneDimensonalArray(float [][] twoDimArr) {
        float temp [] = new float[twoDimArr.length * twoDimArr[0].length];
        int index = 0;
        for (int i = 0; i < twoDimArr.length; i++) {
            for (int j = 0; j < twoDimArr[i].length; j++) {
                temp[index] = twoDimArr[i][j];
                index++;
            }
        }
        return temp;
    }
}
