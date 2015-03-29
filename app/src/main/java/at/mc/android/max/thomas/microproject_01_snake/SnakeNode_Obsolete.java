package at.mc.android.max.thomas.microproject_01_snake;

/**
 * Created by thomasstaltner on 26/03/15.
 */
public class SnakeNode_Obsolete {
    /** Ref to the next elem in the list, or null if it is the last */
    private SnakeNode_Obsolete next;

    /** Ref to the prev elem in the list, or null if it is the first */
    private SnakeNode_Obsolete prev;

    /** Holds the actual data */
    private int valX;
    private int valY;

    // ###################### --Constructors
    public SnakeNode_Obsolete() {
        this(0, 0, null, null);
    }

    public SnakeNode_Obsolete(int valX, int valY) {
        this(valX, valY, null, null);
    }

    public SnakeNode_Obsolete(int valX, int valY, SnakeNode_Obsolete next, SnakeNode_Obsolete prev) {
        this.valX = valX;
        this.valY = valY;
        this.next = next;
        this.prev = prev;
    }

    // ###################### --Getter
    public int getValueX() {
        return valX;
    }

    public int getValueY() {
        return valY;
    }

    public SnakeNode_Obsolete getNext() {
        return next;
    }

    public SnakeNode_Obsolete getPrev() {
        return prev;
    }

    // ###################### --Setter
    public void setValueX(int valX) {
        this.valX = valX;
    }

    public void setValueY(int valX) {
        this.valY = valX;
    }

    public void setNext(SnakeNode_Obsolete next) {
        this.next = next;
    }

    public void setPrev(SnakeNode_Obsolete prev) {
        this.prev = prev;
    }
}
