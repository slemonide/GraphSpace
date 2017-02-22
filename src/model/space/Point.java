package model.space;

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

    // REQUIRES: this and otherPoint are on the same horizontal or vertical line
    // this and otherPoint are distinct
    // EFFECTS: produce the direction from this point to the otherPoint
    public Direction getDirectionTo(Point otherPoint) {
        /*
         *  -----> dx
         * |
         * | dy
         * |
         * v
         */

        int dx = otherPoint.x - this.x;
        int dy = otherPoint.y - this.y;

        if (dx > 0) {
            return Direction.RIGHT;
        } else if (dx < 0) {
            return Direction.LEFT;
        } else if (dy > 0) {
            return Direction.DOWN;
        } else if (dy < 0) {
            return Direction.UP;
        } else {
            return null; // TODO: add exceptions
        }
    }

    // EFFECTS: returns the difference between this point and a given point
    public Point minus(Point otherPoint) {
        return new Point(this.x - otherPoint.x, this.y - otherPoint.y);
    }

    // EFFECTS: produce the taxicab distance between this point and a given point
    public int taxicabDistance(Point point) {
        return Math.abs(point.x - this.x) + Math.abs(point.y - this.y);
    }
}
