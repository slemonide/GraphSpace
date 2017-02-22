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

    @Test
    public void testMinus() {
        assertEquals(new Point(12, -123), new Point(0, 23).minus(new Point(-12,  23 + 123)));
        assertEquals(new Point(0, 0), new Point(0, 0).minus(new Point(0, 0)));
    }

    @Test
    public void testTaxicabDistance() {
        assertEquals(0, new Point(0, 0).taxicabDistance(new Point(0, 0)));
        assertEquals(0, new Point(110, -20).taxicabDistance(new Point(110, -20)));

        assertEquals(10, new Point(0, 10).taxicabDistance(new Point(0, 0)));
        assertEquals(20, new Point(90, -20).taxicabDistance(new Point(110, -20)));

        assertEquals(200, new Point(100, -100).taxicabDistance(new Point(0, 0)));
        assertEquals(110, new Point(10, -30).taxicabDistance(new Point(110, -20)));
    }
}
