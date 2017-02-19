package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Contains methods for generating lists of points
 */
public class PointGenerator {
    // Suppresses default constructor, ensuring non-instantiability.
    private PointGenerator() {}

    // EFFECTS: returns a set containing all the points between the origin and the point
    // if in doubt, take the node that is closest to the horizontal line
    public static Queue<Point> generateFromPoint(Point endPoint) {
        // template as a traversal of generated graph
        Queue<Point> visited = new LinkedList<>();
        Point current = new Point(0, 0);
        visited.add(current);

        while (!current.equals(endPoint)) {
            if ((lineGoesAboveCurrentNode(current, endPoint)
                    || endIsExactlyAbove(current, endPoint))
                    && !visited.contains(Direction.UP.shiftPoint(current))) {
                current = Direction.UP.shiftPoint(current);
            } else if ((lineGoesBelowCurrentNode(current, endPoint)
                    || endIsExactlyBelow(current, endPoint))
                    && !visited.contains(Direction.DOWN.shiftPoint(current))) {
                current = Direction.DOWN.shiftPoint(current);
            } else if (theEndIsToTheLeft(current, endPoint)) {
                current = Direction.LEFT.shiftPoint(current);
            } else if (theEndIsToTheRight(current, endPoint)) {
                current = Direction.RIGHT.shiftPoint(current);
            }
            visited.add(current);
        }

        return visited;
    }

    private static boolean theEndIsToTheLeft(Point current, Point endPoint) {
        return endPoint.x < current.x;
    }

    private static boolean endIsExactlyBelow(Point current, Point endPoint) {
        return current.x == endPoint.x && endPoint.y > current.y;
    }

    private static boolean endIsExactlyAbove(Point current, Point endPoint) {
        return current.x == endPoint.x && endPoint.y < current.y;
    }

    private static boolean theEndIsToTheRight(Point current, Point endPoint) {
        return endPoint.x > current.x;
    }

    private static boolean lineGoesBelowCurrentNode(Point current, Point endPoint) {
        if (current.x > 0) {
            return endPoint.y * current.x > current.y * endPoint.x;
        } else {
            return endPoint.y * current.x < current.y * endPoint.x;
        }
    }

    private static boolean lineGoesAboveCurrentNode(Point current, Point endPoint) {
        /*
         *          /|
         *        /  |
         *      / |  | y_e
         *    /   |y |
         *  /     |  |
         * -----------
         * |<-x-> |
         * | <-x_e-> |
         */
        if (current.x > 0) {
            return endPoint.y * current.x < current.y * endPoint.x;
        } else {
            return endPoint.y * current.x > current.y * endPoint.x;
        }
    }
}
