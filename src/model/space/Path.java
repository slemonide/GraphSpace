package model.space;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

/*
 * A sequence of steps that lead from one node in the graph to another
 */
public class Path extends LinkedList<Direction> {

    // EFFECTS: Constructs an empty path
    public Path() {}

    // EFFECTS: Constructs a path with directions provided
    // First argument is the first turn, last argument is the last turn
    public Path(Direction... directions) {
        Collections.addAll(this, directions);
    }

    // EFFECTS: constructs a path from given vector
    // if in doubt, take the node that is closest to the horizontal line
    public Path(Point point) {
        Queue<Point> pointPath = PointGenerator.generateFromPoint(point);

        Point previousPoint = pointPath.remove();
        for (Point currentPoint : pointPath) {
            this.add(previousPoint.getDirectionTo(currentPoint));
            previousPoint = currentPoint;
        }
    }
}
