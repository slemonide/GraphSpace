package tests;

import model.Direction;
import model.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class DirectionTest {
    @Test
    public void testOpposite() {
        assertEquals(Direction.UP, Direction.DOWN.opposite());
        assertEquals(Direction.DOWN, Direction.UP.opposite());
        assertEquals(Direction.LEFT, Direction.RIGHT.opposite());
        assertEquals(Direction.RIGHT, Direction.LEFT.opposite());
    }

    @Test
    public void testShiftPoint() {
        Point p = new Point(30, 40);

        assertEquals(new Point(30, 40 + 1), Direction.DOWN.shiftPoint(p));
        assertEquals(new Point(30, 40 - 1), Direction.UP.shiftPoint(p));
        assertEquals(new Point(30 + 1, 40), Direction.RIGHT.shiftPoint(p));
        assertEquals(new Point(30 - 1, 40), Direction.LEFT.shiftPoint(p));
    }

    @Test
    public void testNextNoIdentity() {
        for (Direction direction : Direction.values()) {
            assertNotEquals(direction, direction.next());
        }
    }

    @Test
    public void testNextRepetition() {
        for (Direction direction : Direction.values()) {
            Direction initialDirection = direction;
            for (int i = 0; i < Direction.values().length; i++) {
                direction = direction.next();
            }
            assertEquals(initialDirection, direction);
        }
    }
}
