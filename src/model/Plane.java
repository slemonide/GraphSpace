package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * A representation of a discrete Euclidean plane filled with nodes
 */
public class Plane extends HashMap<Point, Node> {

    // REQUIRES: height, width > 0;
    // EFFECTS: constructs a rectangular projection of given graph on this plane,
    // as seen from observerNode, observer is placed at the center of the projection
    // if height and/or width are odd, place the observer slightly to the bottom and/or right
    public Plane(int height, int width, Node observerNode) {
        Queue<Point> pointsToProcess = new LinkedList<>();

        int xShift = width / 2;
        int yShift = height / 2;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pointsToProcess.add(new Point(x - xShift, y - yShift));
            }
        }

        for (Point point : pointsToProcess) {
            this.put(point, observerNode.getNodeAt(point));
        }
    }
}


/*

    // REQUIRES: height, width > 0;
    // EFFECTS: produce a rectangular projection of this graph on a regular Euclidean plane,
    // as seen from this node
    public Map<Point, Node> getMap(int height, int width) {
        return null;
    }
 */