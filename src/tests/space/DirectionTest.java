package tests.space;

import model.space.Direction;
import model.space.Point;
import org.junit.Test;

import static model.space.Direction.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class DirectionTest {

    @Test
    public void testNormal() {
        for (Direction direction : Direction.values()) {
            assertEquals(2, direction.normal().size());
        }

        assertTrue(UP.normal().contains(LEFT));
        assertTrue(UP.normal().contains(RIGHT));

        assertTrue(RIGHT.normal().contains(UP));
        assertTrue(RIGHT.normal().contains(DOWN));

        assertTrue(DOWN.normal().contains(LEFT));
        assertTrue(DOWN.normal().contains(RIGHT));

        assertTrue(LEFT.normal().contains(UP));
        assertTrue(LEFT.normal().contains(DOWN));
    }

    @Test
    public void testOpposite() {
        assertEquals(UP, DOWN.opposite());
        assertEquals(DOWN, UP.opposite());
        assertEquals(LEFT, RIGHT.opposite());
        assertEquals(RIGHT, LEFT.opposite());
    }

    @Test
    public void testShiftPoint() {
        Point p = new Point(30, 40);

        assertEquals(new Point(30, 40 + 1), DOWN.shiftPoint(p));
        assertEquals(new Point(30, 40 - 1), UP.shiftPoint(p));
        assertEquals(new Point(30 + 1, 40), RIGHT.shiftPoint(p));
        assertEquals(new Point(30 - 1, 40), LEFT.shiftPoint(p));
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
