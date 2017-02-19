package tests.space;

import model.space.Direction;
import model.space.Point;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PointTest {
    @Test
    public void testIsOrigin() {
        Point origin = new Point(0, 0);
        assertTrue(origin.isOrigin());

        Point notOrigin = new Point(12, -123);
        assertFalse(notOrigin.isOrigin());

        assertFalse(new Point(0, 1).isOrigin());
        assertFalse(new Point(0, -1).isOrigin());
        assertFalse(new Point(1, 0).isOrigin());
        assertFalse(new Point(-1, 0).isOrigin());
        assertFalse(new Point(1, 1).isOrigin());
        assertFalse(new Point(-1, 1).isOrigin());
        assertFalse(new Point(1, -1).isOrigin());
        assertFalse(new Point(-1, -1).isOrigin());
    }

    @Test
    public void testHalf() {
        assertTrue(new Point(0, 0).half().equals(new Point(0, 0)));
        assertTrue(new Point(1, -1).half().equals(new Point(1/2, -1/2)));
        assertTrue(new Point(123, -9438).half().equals(new Point(123/2, -9438/2)));
    }

    @Test
    public void testGetDirectionToBase() {
        assertEquals(Direction.RIGHT, new Point(0, 0).getDirectionTo(new Point(1, 0)));
        assertEquals(Direction.LEFT, new Point(0, 0).getDirectionTo(new Point(-1, 0)));
        assertEquals(Direction.UP, new Point(0, 0).getDirectionTo(new Point(0, -1)));
        assertEquals(Direction.DOWN, new Point(0, 0).getDirectionTo(new Point(0, 1)));
    }
    @Test
    public void testGetDirectionToFar() {
        assertEquals(Direction.RIGHT, new Point(0, 0).getDirectionTo(new Point(1023, 0)));
        assertEquals(Direction.LEFT, new Point(0, 0).getDirectionTo(new Point(-3201, 0)));
        assertEquals(Direction.UP, new Point(0, 0).getDirectionTo(new Point(0, -543)));
        assertEquals(Direction.DOWN, new Point(0, 0).getDirectionTo(new Point(0, 9234)));
    }
}
