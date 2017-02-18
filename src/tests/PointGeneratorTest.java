package tests;

import model.PointGenerator;
import org.junit.Test;

import java.awt.*;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PointGeneratorTest {
    private Set<Point> testPointSet;

    @Test
    public void testGenerateFromPointBase() {
        testPointSet = PointGenerator.generateFromPoint(new Point(0, 0));
        assertEquals(1, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
    }

    @Test
    public void testGenerateFromPointNeighbours() {
        testPointSet = PointGenerator.generateFromPoint(new Point(1, 0));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(1, 0)));

        testPointSet = PointGenerator.generateFromPoint(new Point(-1, 0));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(-1, 0)));

        testPointSet = PointGenerator.generateFromPoint(new Point(0, 1));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(0, 1)));

        testPointSet = PointGenerator.generateFromPoint(new Point(0, -1));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(0, -1)));

        testPointSet = PointGenerator.generateFromPoint(new Point(1, 1));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(1, 1)));


        testPointSet = PointGenerator.generateFromPoint(new Point(1, -1));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(1, -1)));


        testPointSet = PointGenerator.generateFromPoint(new Point(-1, 1));
        assertEquals(2, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(1, 0)));
    }

    @Test
    public void testGenerateFromPointFar() {
        testPointSet = PointGenerator.generateFromPoint(new Point(2, 5));
        assertEquals(8, testPointSet.size());
        assertTrue(testPointSet.contains(new Point(0, 0)));
        assertTrue(testPointSet.contains(new Point(0, -1)));
        assertTrue(testPointSet.contains(new Point(1, -1)));
        assertTrue(testPointSet.contains(new Point(1, -2)));
        assertTrue(testPointSet.contains(new Point(1, -3)));
        assertTrue(testPointSet.contains(new Point(1, -4)));
        assertTrue(testPointSet.contains(new Point(2, -4)));
        assertTrue(testPointSet.contains(new Point(2, -5)));
    }
}
