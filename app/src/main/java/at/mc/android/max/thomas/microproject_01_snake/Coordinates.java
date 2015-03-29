package at.mc.android.max.thomas.microproject_01_snake;

/**
 * Created by thomasstaltner on 29/03/15.
 */
public class Coordinates {
    private int coorX;
    private int coorY;

    public Coordinates(int coorX, int coorY){
        this.coorX = coorX;
        this.coorY = coorY;
    }

    public int getCoorX(){
        return this.coorX;
    }

    public int getCoorY(){
        return this.coorY;
    }

    public void setCoorX(int coorX){
        this.coorX = coorX;
    }

    public void setCoorY(int coorY){
        this.coorY = coorY;
    }
}
