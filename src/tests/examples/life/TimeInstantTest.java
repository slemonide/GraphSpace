package tests.examples.life;

import examples.Life.TimeInstant;
import model.space.Direction;
import model.space.Node;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static examples.Life.State.ALIVE;
import static examples.Life.State.DEAD;
import static model.space.Direction.*;
import static org.junit.Assert.assertEquals;

public class TimeInstantTest {
    private static final int MAX_TICKS = 100;
    private final double TOL = 0.1;
    private final int HEIGHT = 100;
    private final int WIDTH = 100;
    private final Point ORIGIN = new Point(0,0);
    private final Point DISTANT_POINT = new Point(HEIGHT / 2 + 32, WIDTH / 3 - 50);
    private TimeInstant testTimeInstant;

    @Before
    public void runBefore() {
        testTimeInstant = new TimeInstant(new Node(WIDTH, HEIGHT), new HashSet<Node>());
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testTimeInstant.getPopulation());

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Point selectedPoint = new Point(x, y);

                assertEquals(DEAD, testTimeInstant.readState(selectedPoint));
            }
        }
    }

    @Test
    public void testRevive() {
        testTimeInstant.revive(new Point(0, 0));
        testTimeInstant.revive(new Point(1, 100));

        assertEquals(ALIVE, testTimeInstant.readState(new Point(0, 0)));
        assertEquals(ALIVE, testTimeInstant.readState(new Point(1, 100)));
        assertEquals(2, testTimeInstant.getPopulation());
    }

    @Test
    public void testKill() {
        testTimeInstant.revive(new Point(-23, 123));
        testTimeInstant.revive(new Point(400, 1));
        testTimeInstant.revive(new Point(401, 1));
        testTimeInstant.revive(new Point(0, 0));

        assertEquals(ALIVE, testTimeInstant.readState(new Point(0, 0)));
        testTimeInstant.kill(new Point(0, 0));
        assertEquals(DEAD, testTimeInstant.readState(new Point(0, 0)));
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

    @Test
    public void testMove() {
        testTimeInstant.revive(ORIGIN);
        testTimeInstant.revive(DISTANT_POINT);
        assertEquals(ALIVE, testTimeInstant.readState(ORIGIN));
        assertEquals(ALIVE, testTimeInstant.readState(DISTANT_POINT));

        for (Direction direction : Direction.values()) {
            assertEquals(DEAD, testTimeInstant.readState(direction.shiftPoint(ORIGIN)));
            assertEquals(DEAD, testTimeInstant.readState(direction.shiftPoint(DISTANT_POINT)));
        }

        for (Direction direction : Direction.values()) {
            testTimeInstant.move(direction);
            assertEquals(DEAD, testTimeInstant.readState(ORIGIN));
            assertEquals(DEAD, testTimeInstant.readState(DISTANT_POINT));
            testTimeInstant.move(direction.opposite());
            assertEquals(ALIVE, testTimeInstant.readState(ORIGIN));
            assertEquals(ALIVE, testTimeInstant.readState(DISTANT_POINT));
        }
    }

    @Test
    public void testMoveFar() {
        testTimeInstant.revive(ORIGIN);
        testTimeInstant.revive(DISTANT_POINT);

        testTimeInstant.move(UP);
        testTimeInstant.move(UP);
        testTimeInstant.move(UP);
        testTimeInstant.move(UP);
        testTimeInstant.move(UP); // 5 up

        testTimeInstant.move(RIGHT);
        testTimeInstant.move(RIGHT); // 2 right

        testTimeInstant.move(DOWN);
        testTimeInstant.move(DOWN);
        testTimeInstant.move(DOWN);
        testTimeInstant.move(DOWN);
        testTimeInstant.move(DOWN); // 5 down

        testTimeInstant.move(LEFT);
        testTimeInstant.move(LEFT); // 2 left

        assertEquals(ALIVE, testTimeInstant.readState(ORIGIN));
        assertEquals(ALIVE, testTimeInstant.readState(DISTANT_POINT));
    }
}
