package tests;

import model.Direction;
import model.Path;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

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

    @Test
    public void testConstructorPointBase() {
        testPath = new Path(new Point(0, 0));
        assertTrue(testPath.isEmpty());
        assertEquals(0, testPath.size());
    }

    @Test
    public void testConstructorPointNeighbours() {
        testPath = new Path(new Point(0, -1));
        assertEquals(Direction.UP, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(0, 1));
        assertEquals(Direction.DOWN, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(-1, 0));
        assertEquals(Direction.LEFT, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(1, 0));
        assertEquals(Direction.RIGHT, testPath.remove());
        assertTrue(testPath.isEmpty());
    }

    @Test
    public void testGetNodeAtConflict() {
        testPath = new Path(new Point(-1, -1));
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.LEFT, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(-1, 1));
        assertEquals(Direction.LEFT, testPath.remove());
        assertEquals(Direction.DOWN, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(1, -1));
        assertEquals(Direction.DOWN, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(1, 1));
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertTrue(testPath.isEmpty());
    }

    @Test
    public void testGetNodeAtFar() {
        testPath = new Path(new Point(4, 4));
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(4, 1));
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(1, 3));
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertTrue(testPath.isEmpty());

        testPath = new Path(new Point(2, 5));
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertEquals(Direction.RIGHT, testPath.remove());
        assertEquals(Direction.UP, testPath.remove());
        assertTrue(testPath.isEmpty());
    }
}
