package tests;

import model.Direction;
import model.Path;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {
    private Path testPath;

    @Before
    public void runBefore() {
        testPath = new Path();
    }

    @Test
    public void testConstructor() {
        assertTrue(testPath.isEmpty());
        assertEquals(0, testPath.size());
    }

    @Test
    public void testConstructorLong() {
        testPath = new Path(Direction.DOWN, Direction.UP, Direction.DOWN, Direction.LEFT);

        assertFalse(testPath.isEmpty());
        assertEquals(4, testPath.size());
        assertEquals(Direction.DOWN, testPath.remove());
        assertEquals(Direction.UP,   testPath.remove());
        assertEquals(Direction.DOWN, testPath.remove());
        assertEquals(Direction.LEFT, testPath.remove());
    }
}
