package tests.world;

import model.space.Point;
import model.world.World;
import org.junit.Before;
import org.junit.Test;
import tests.space.NodeTest;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class WorldTest extends NodeTest {
    private final int HEIGHT = 100;
    private final int WIDTH = 50;
    private World testWorld;
    private World testMultiWorld;

    @Before
    public void runBefore() {
        testWorld = new World();
        testMultiWorld = new World(WIDTH, HEIGHT);
    }

    @Test
    public void testConstructor() {
        assertNotNull(testWorld.readObject());
    }

    @Test
    public void testConstructorBig() {
        for (int x = - WIDTH; x < WIDTH; x++) {
            for (int y = - HEIGHT; y < HEIGHT; y++) {
                assertNotNull(testMultiWorld.getNodeAt(new Point(x, y)));
            }
        }
    }

    @Test
    public void testConstructorBigConnectivity() {
        assertEquals(testMultiWorld, testMultiWorld.getNodeAt(new Point(WIDTH, 0)));
        assertEquals(testMultiWorld, testMultiWorld.getNodeAt(new Point(0, HEIGHT)));

        assertEquals(testMultiWorld, testMultiWorld.getNodeAt(new Point(WIDTH, HEIGHT)));
        assertEquals(testMultiWorld, testMultiWorld.getNodeAt(new Point(-WIDTH, -HEIGHT)));
    }
}
