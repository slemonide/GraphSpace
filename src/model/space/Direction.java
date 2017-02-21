package model.space;

import java.util.HashSet;
import java.util.Set;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    // EFFECTS: produce the direction opposite to this one
    public Direction opposite() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            default:
                return LEFT;
        }
    }

    // EFFECTS: produce directions that are perpendicular to the given direction
    public Set<Direction> normal() {
        Set<Direction> normals = new HashSet<>();

        if (this == UP || this == DOWN) {
            normals.add(LEFT);
            normals.add(RIGHT);
        } else if (this == LEFT || this == RIGHT) {
            normals.add(UP);
            normals.add(DOWN);
        }

        return normals;
    }

    // EFFECTS: produce an arbitrary next direction
    // INVARIANT: calling next() this.size() times will make the direction repeat
    // INVARIANT: the same direction does not come after itself
    public Direction next() {
        switch (this) {
            case UP:
                return DOWN;
            case DOWN:
                return LEFT;
            case LEFT:
                return RIGHT;
            default:
                return UP;
        }
    }

    // EFFECTS: returns a new point that is positioned in this
    // direction relative to this point
    public Point shiftPoint(Point p) {
        int x = p.x;
        int y = p.y;

        switch (this) {
            case UP:
                return new Point(x, y - 1);
            case DOWN:
                return new Point(x, y + 1);
            case LEFT:
                return new Point(x - 1, y);
            default:
                return new Point(x + 1, y);
        }
    }
}

/*
    public void move(Direction dir) {
        switch (dir) {
            case UP:
                break;
            case DOWN:
                break;
            case LEFT:
                break;
            default:
        }
    }
 */