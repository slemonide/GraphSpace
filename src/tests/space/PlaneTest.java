package tests.space;

import model.space.Direction;
import model.space.Node;
import model.space.Plane;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaneTest {
    private final int PLANE_SIZE = 20;
    private Node testMultiNode;
    private Plane testPlane;

    @Before
    public void runBefore() {
        testMultiNode = new Node(PLANE_SIZE, PLANE_SIZE);
    }

    @Test
    public void testConstructorBase() {
        testPlane = new Plane(1, 1, testMultiNode);
        assertEquals(1, testPlane.size());
        assertEquals(testMultiNode, testPlane.get(new Point(0,0)));
    }

    @Test
    public void testConstructorTwoByTwo() {
        testPlane = new Plane(2, 2, testMultiNode);
        assertEquals(4, testPlane.size());
        assertEquals(testMultiNode, testPlane.get(new Point(0,0)));
        assertEquals(testMultiNode.getNode(Direction.LEFT),
                testPlane.get(new Point(-1,0)));
        assertEquals(testMultiNode.getNode(Direction.UP),
                testPlane.get(new Point(0,-1)));
        assertEquals(testMultiNode.getNode(Direction.LEFT).getNode(Direction.UP),
                testPlane.get(new Point(-1,-1)));
    }

    @Test
    public void testConstructorThreeByThree() {
        testPlane = new Plane(3, 3, testMultiNode);
        assertEquals(9, testPlane.size());
        assertEquals(testMultiNode, testPlane.get(new Point(0,0)));
        assertEquals(testMultiNode.getNode(Direction.RIGHT),
                testPlane.get(new Point(1,0)));
        assertEquals(testMultiNode.getNode(Direction.LEFT),
                testPlane.get(new Point(-1,0)));
        assertEquals(testMultiNode.getNode(Direction.DOWN),
                testPlane.get(new Point(0,1)));
        assertEquals(testMultiNode.getNode(Direction.UP),
                testPlane.get(new Point(0,-1)));
        assertEquals(testMultiNode.getNode(Direction.RIGHT).getNode(Direction.DOWN),
                testPlane.get(new Point(1,1)));
        assertEquals(testMultiNode.getNode(Direction.RIGHT).getNode(Direction.UP),
                testPlane.get(new Point(1,-1)));
        assertEquals(testMultiNode.getNode(Direction.LEFT).getNode(Direction.DOWN),
                testPlane.get(new Point(-1,1)));
        assertEquals(testMultiNode.getNode(Direction.LEFT).getNode(Direction.UP),
                testPlane.get(new Point(-1,-1)));
    }

    @Test
    public void testConstructorBig() {
        // The projection is larger than the graph itself!
        testPlane = new Plane(100, 100, testMultiNode);
        assertEquals(100 * 100, testPlane.size());
        assertEquals(testMultiNode, testPlane.get(new Point(0,0)));
        assertEquals(testMultiNode, testPlane.get(new Point(PLANE_SIZE, 0)));
        assertEquals(testMultiNode, testPlane.get(new Point(-PLANE_SIZE, 0)));
        assertEquals(testMultiNode, testPlane.get(new Point(2 * PLANE_SIZE, 0)));
        assertEquals(testMultiNode, testPlane.get(new Point(-2 * PLANE_SIZE, 0)));
        assertEquals(testMultiNode, testPlane.get(new Point(0, PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(0, -PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(0, 2 * PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(0, -2 * PLANE_SIZE)));

        assertEquals(testMultiNode, testPlane.get(new Point(PLANE_SIZE, PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(PLANE_SIZE, -PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(-PLANE_SIZE, PLANE_SIZE)));
        assertEquals(testMultiNode, testPlane.get(new Point(-PLANE_SIZE, -PLANE_SIZE)));
    }
}
