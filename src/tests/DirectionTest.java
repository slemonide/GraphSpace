package tests;

import model.Direction;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;

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
}
