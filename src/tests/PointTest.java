package tests;

import model.Point;
import org.junit.Test;

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
}
