package at.mc.android.max.thomas.microproject_01_snake;

/**
 * Created by thomasstaltner on 26/03/15.
 */
public class SnakeList {
    /** Pointer to the first and the last element of the list */
    private SnakeNode head, tail;
    private int elements;

    // ########################### --Constructor--
    /** Constructor initializes list with a standard size. */
    public SnakeList() {
        this.head = null;
        this.tail = null;
        this.elements = 0;
    }

    /**
     * Copy constructor initializes list with another list. This constructor
     * must COPY all elements of the other list. The elements of the other list
     * must NOT be changed! Initializes the double linked list instance
     **/
//    public SnakeList(SnakeList list) {
//        SnakeNode p = list.head;
//
//        while (p != null) {
//            this.pushBack(p.getValue());
//            p = p.getNext();
//        }
//    }

    // ########################### --Methods--
    /**
     * Deinitializes the object; think about it and comment what to do here.
     */
    public void finalize() {
        this.head = null;
        this.tail = null;
        this.elements = 0;
        // The object is still alive, to deinitialize the object may finalize2()
        // is better
    }

    public static SnakeList finalize2() {
        return null;
    }

    /** Clears all elements from the linked list */
    public void clear() {
        this.head = null;
        this.tail = null;
        this.elements = 0;
    }

    /** Adds an element at the front of the linked list */
    public void pushFront(int valX, int valY) {
        SnakeNode p = new SnakeNode(valX, valY);
        p.setNext(head);
        if (head == null) {
            tail = p;
        } else {
            head.setPrev(p);
        }
        head = p;
        this.elements++;
    }

    /**
     * Adds all elements from another list at the front of this linked list.
     * other list MUST NOT be the same like this list.
     */
//    public void pushFront(SnakeList other) {
//        if (this != other) {
//            SnakeNode p = other.tail;
//
//            while (p != null) {
//                this.pushFront(p.getValue());
//                p = p.getPrev();
//            }
//        }
//    }

    /** Adds an element at the back of the linked list. */
    public void pushBack(int valX, int valY) {
        SnakeNode p = new SnakeNode(valX, valY);
        // check empty list
        if (head == null) {
            head = p;
            p.setPrev(null);
        } else {
            tail.setNext(p);
            p.setPrev(tail);
            ;
        }
        tail = p;
        elements++;
    }

//    /**
//     * Adds all elements from another list at the end of this linked list. other
//     * list MUST NOT be the same like this list.
//     */
//    public void pushBack(SnakeList other) {
//        if (this != other) {
//            SnakeNode p = other.head;
//
//            while (p != null) {
//                this.pushBack(p.getValue());
//                p = p.getNext();
//            }
//        }
//    }

//    /** Clones this SnakeList instance and returns an exact COPY. */
//    public SnakeList clone() {
//        SnakeList ret = new SnakeList();
//        SnakeNode p = this.head;
//
//        while (p != null) {
//            ret.pushBack(p.getValue());
//            p = p.getNext();
//        }
//
//        return ret;
//
//    }

    /**
     * Returns true if the other list is equal to this one, false otherwise. *
     * The contents of the two lists must not be changed!
     */
    public boolean equals(SnakeList other) {
        if (this.elements != other.elements) {
            return false;
        } else {
            SnakeNode p = this.head;
            SnakeNode q = other.head;
            while (p != null) {
                if (p.getValueX() != q.getValueX() || p.getValueY() != q.getValueY()) {
                    return false;
                }
                p = p.getNext();
                q = q.getNext();
            }
        }
        return true;
    }

    /**
     * Removes and returns the front element Integer.MIN_VALUE if empty
     */
    public int[] popFront() {
        // check empty list
        int[] ret = new int[2];

        if (head != null) {
            ret[0] = head.getValueX();
            ret[1] = head.getValueY();
            head = head.getNext();
            if (head != null)
                head.setPrev(null);
            elements--;
            return ret;
        } else {
            this.tail = null;
            ret[0] = Integer.MIN_VALUE;
            ret[1] = Integer.MIN_VALUE;
            return ret;
        }
    }

    /**
     * Returns the front element of the list Returns Integer.MIN_VALUE if empty
     */
    public int[] peekFront() {
        // check empty list
        int[] ret = new int[2];

        if (head != null) {
            ret[0] = head.getValueX();
            ret[1] = head.getValueY();
            return ret;
        } else {
            ret[0] = Integer.MIN_VALUE;
            ret[1] = Integer.MIN_VALUE;
            return ret;
        }
    }

    /**
     * Removes and returns the element from the back of the linked list. Returns
     * Integer.MIN_VALUE if empty
     */
    public int[] popBack() {
        // check empty list
        int[] ret = new int[2];

        if (this.tail != null) {
            ret[0] = tail.getValueX();
            ret[1] = tail.getValueY();
            tail = tail.getPrev();
            if (tail != null)
                tail.setNext(null);
            elements--;
            return ret;
        } else {
            this.head = null;
            ret[0] = Integer.MIN_VALUE;
            ret[1] = Integer.MIN_VALUE;
            return ret;
        }
    }

    /**
     * Returns the element at the back of the list without removing it. Returns
     * Integer.MIN_VALUE if empty
     */
    public int[] peekBack() {
        // check empty list
        int[] ret = new int[2];
        if (head != null) {
            ret[0] = tail.getValueX();
            ret[1] = tail.getValueY();
            return ret;
        } else {
            ret[0] = Integer.MIN_VALUE;
            ret[1] = Integer.MIN_VALUE;
            return ret;

        }
    }

    /** Returns the number of elements in the double linked list */
    public int elements() {
        return this.elements;
    }

    /** Returns true if the element val exists in the list, false otherwise. */
    public boolean search(int val) {
        SnakeNode p = this.head;
        while (p != null) {
            if (p.getValueX() == val || p.getValueY() == val) {
                return true;
            }
            p = p.getNext();
        }
        return false;
    }

    /**
     * Reverses the order of all elements in the list. “He who is first, shall
     * be last!”
     */
    public void reverse() {
        SnakeNode temp = null;
        SnakeNode current = this.head;

        // swap next and prev for all nodes of
        // double linked list
        while (current != null) {
            temp = current.getPrev();
            current.setPrev(current.getNext());
            current.setNext(temp);
            current = current.getPrev(); // because they have changed
            // current = current.prev; // because they have changed
        }

        // before changing head, check for the case of empty list
        // or list with only one node.
        if (temp != null) {
            temp = this.tail;
            this.tail = this.head;
            this.head = temp;
        }
    }

    // ########################### --Useful Infos--
    public boolean isHeadNull() {
        return this.head == null;
    }

    public boolean isTailNull() {
        return this.tail == null;
    }
}
