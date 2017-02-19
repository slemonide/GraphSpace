package model;

public class Point extends java.awt.Point {
    public Point(int x, int y) {
        super(x, y);
    }

    // EFFECTS: produce true if this point is origin
    public boolean isOrigin() {
        return (x == 0 && y == 0);
    }

    // EFFECTS: produce a point that partitions this rectangle in half
    public Point half() {
        return new Point(x / 2, y / 2);
    }
}
