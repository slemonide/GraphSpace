package tests.examples.life;

import examples.Life.TimeInstant;
import model.space.Node;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;

public class TimeLineInstantTest {
    private static final int MAX_TICKS = 100;
    private final double TOL = 0.1;
    private final int HEIGHT = 100;
    private final int WIDTH = 100;
    private TimeInstant testTimeInstant;

    @Before
    public void runBefore() {
        testTimeInstant = new TimeInstant(new Node(WIDTH, HEIGHT), new HashSet<Node>());
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testTimeInstant.getPopulation());
    }

    @Test
    public void testRevive() {
        testTimeInstant.revive(new Point(0, 0));
        testTimeInstant.revive(new Point(1, 100));

        assertEquals(2, testTimeInstant.getPopulation());
    }

    @Test
    public void testKill() {
        testTimeInstant.revive(new Point(-23, 123));
        testTimeInstant.revive(new Point(400, 1));
        testTimeInstant.revive(new Point(401, 1));
        testTimeInstant.revive(new Point(0, 0));

        testTimeInstant.kill(new Point(0, 0));
        assertEquals(3, testTimeInstant.getPopulation());
    }

    @Test
    public void testPopulate() {
        testTimeInstant.populate(0);
        assertEquals(0, testTimeInstant.getPopulation());

        testTimeInstant.populate(0.5);
        assertEquals(HEIGHT * WIDTH * 0.5, testTimeInstant.getPopulation(), TOL);

        testTimeInstant.purge();
        testTimeInstant.populate(0.33);
        assertEquals(HEIGHT * WIDTH * 0.33, testTimeInstant.getPopulation(), TOL);

        testTimeInstant.purge();
        assertEquals(0, testTimeInstant.getPopulation());
        testTimeInstant.populate(1);
        assertEquals(HEIGHT * WIDTH, testTimeInstant.getPopulation());
    }

    @Test
    public void testPopulateTwice() {
        testTimeInstant.populate(0.5);
        testTimeInstant.populate(0.5);
        assertEquals(HEIGHT * WIDTH, testTimeInstant.getPopulation());

        testTimeInstant.purge();

        testTimeInstant.populate(0.3);
        testTimeInstant.populate(0.5);
        testTimeInstant.populate(0.2);
        assertEquals(HEIGHT * WIDTH, testTimeInstant.getPopulation());
    }
}
