package model;

import java.util.Collections;
import java.util.LinkedList;

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
    // if in doubt, go counterclockwise
    public Path(Point point) {
        // TODO: finish
    }
}
