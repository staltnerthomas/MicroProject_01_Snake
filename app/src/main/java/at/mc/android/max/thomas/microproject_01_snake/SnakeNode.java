package at.mc.android.max.thomas.microproject_01_snake;

/**
 * Created by thomasstaltner on 26/03/15.
 */
public class SnakeNode {
    /** Ref to the next elem in the list, or null if it is the last */
    private SnakeNode next;

    /** Ref to the prev elem in the list, or null if it is the first */
    private SnakeNode prev;

    /** Holds the actual data */
    private int valX;
    private int valY;

    // ###################### --Constructors
    public SnakeNode() {
        this(0, 0, null, null);
    }

    public SnakeNode(int valX, int valY) {
        this(valX, valY, null, null);
    }

    public SnakeNode(int valX, int valY, SnakeNode next, SnakeNode prev) {
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

    public SnakeNode getNext() {
        return next;
    }

    public SnakeNode getPrev() {
        return prev;
    }

    // ###################### --Setter
    public void setValueX(int valX) {
        this.valX = valX;
    }

    public void setValueY(int valX) {
        this.valY = valX;
    }

    public void setNext(SnakeNode next) {
        this.next = next;
    }

    public void setPrev(SnakeNode prev) {
        this.prev = prev;
    }
}
