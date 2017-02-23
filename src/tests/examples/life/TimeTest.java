package tests.examples.life;

import examples.Life.Time;
import examples.Life.TimeInstant;
import model.space.Direction;
import model.space.Node;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static model.space.Direction.*;
import static org.junit.Assert.*;

public class TimeTest {
    private int WIDTH = 100;
    private int HEIGHT = 100;
    private int MAX_TICKS = 500;
    private Time timeLine;

    private final Point ORIGIN = new Point(0,0);
    private final Point DISTANT_POINT = new Point(HEIGHT / 2 + 32, WIDTH / 3 - 50);

    @Before
    public void runBefore() {
        timeLine = new Time(new TimeInstant(new Node(WIDTH, HEIGHT), new HashSet<Node>()));
    }

    @Test
    public void testConstructor() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Point selectedPoint = new Point(x, y);

                assertFalse(timeLine.isAlive(selectedPoint));
            }
        }
    }

    @Test
    public void testRevive() {
        timeLine.getCurrentTimeInstant().revive(new Point(0, 0));
        timeLine.getCurrentTimeInstant().revive(new Point(1, 100));

        assertTrue(timeLine.isAlive(new Point(0, 0)));
        assertTrue(timeLine.isAlive(new Point(1, 100)));
        assertEquals(2, timeLine.getCurrentTimeInstant().getPopulation());
    }

    @Test
    public void testKill() {
        timeLine.getCurrentTimeInstant().revive(new Point(-23, 123));
        timeLine.getCurrentTimeInstant().revive(new Point(400, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(401, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(0, 0));

        assertTrue(timeLine.isAlive(new Point(0, 0)));
        timeLine.getCurrentTimeInstant().kill(new Point(0, 0));
        assertFalse(timeLine.isAlive(new Point(0, 0)));
        assertEquals(3, timeLine.getCurrentTimeInstant().getPopulation());
    }

    @Test
    public void testMove() {
        timeLine.getCurrentTimeInstant().revive(ORIGIN);
        timeLine.getCurrentTimeInstant().revive(DISTANT_POINT);
        assertTrue(timeLine.isAlive(ORIGIN));
        assertTrue(timeLine.isAlive(DISTANT_POINT));

        for (Direction direction : Direction.values()) {
            assertFalse(timeLine.isAlive(direction.shiftPoint(ORIGIN)));
            assertFalse(timeLine.isAlive(direction.shiftPoint(DISTANT_POINT)));
        }

        for (Direction direction : Direction.values()) {
            timeLine.move(direction);
            assertFalse(timeLine.isAlive(ORIGIN));
            assertFalse(timeLine.isAlive(DISTANT_POINT));
            timeLine.move(direction.opposite());
            assertTrue(timeLine.isAlive(ORIGIN));
            assertTrue(timeLine.isAlive(DISTANT_POINT));
        }
    }

    @Test
    public void testMoveFar() {
        timeLine.getCurrentTimeInstant().revive(ORIGIN);
        timeLine.getCurrentTimeInstant().revive(DISTANT_POINT);

        timeLine.move(UP);
        timeLine.move(UP);
        timeLine.move(UP);
        timeLine.move(UP);
        timeLine.move(UP); // 5 up

        timeLine.move(RIGHT);
        timeLine.move(RIGHT); // 2 right

        timeLine.move(DOWN);
        timeLine.move(DOWN);
        timeLine.move(DOWN);
        timeLine.move(DOWN);
        timeLine.move(DOWN); // 5 down

        timeLine.move(LEFT);
        timeLine.move(LEFT); // 2 left

        assertTrue(timeLine.isAlive(ORIGIN));
        assertTrue(timeLine.isAlive(DISTANT_POINT));
    }

    @Test
    public void testTickBasic() {
        for (int i = 1; i < MAX_TICKS; i++) {
            timeLine.forward();
            assertEquals(i, timeLine.getGeneration());
            assertEquals(i, timeLine.getActualGeneration());
            assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());
        }
    }

    @Test
    public void testTickDieInOneTick() {
        timeLine.getCurrentTimeInstant().revive(ORIGIN);
        assertEquals(0, timeLine.getGeneration());
        assertEquals(0, timeLine.getActualGeneration());
        assertEquals(1, timeLine.getCurrentTimeInstant().getPopulation());
        timeLine.forward();
        assertFalse(timeLine.isAlive(ORIGIN));
        assertEquals(1, timeLine.getGeneration());
        assertEquals(1, timeLine.getActualGeneration());
        assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());

        for (Direction direction : Direction.values()) {
            timeLine.getCurrentTimeInstant().revive(ORIGIN);
            timeLine.getCurrentTimeInstant().revive(direction.shiftPoint(ORIGIN));
            assertTrue(timeLine.isAlive(ORIGIN));
            assertTrue(timeLine.isAlive(direction.shiftPoint(ORIGIN)));
            assertEquals(2, timeLine.getCurrentTimeInstant().getPopulation());

            timeLine.forward();
            assertFalse(timeLine.isAlive(ORIGIN));
            assertFalse(timeLine.isAlive(direction.shiftPoint(ORIGIN)));
            assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());
        }
    }

    @Test
    public void testTickSimpleOscillator() {
        timeLine.getCurrentTimeInstant().revive(ORIGIN);
        timeLine.getCurrentTimeInstant().revive(UP.shiftPoint(ORIGIN));
        timeLine.getCurrentTimeInstant().revive(DOWN.shiftPoint(ORIGIN));
        /*
         * 0 1 0
         * 0 1 0
         * 0 1 0
         */

        for (int i = 0; i < MAX_TICKS; i++) {
            timeLine.forward();

            if (i % 2 == 0) {
                /*
                 * 0 0 0
                 * 1 1 1
                 * 0 0 0
                 */
                assertTrue(timeLine.isAlive(ORIGIN));
                assertTrue(timeLine.isAlive(RIGHT.shiftPoint(ORIGIN)));
                assertTrue(timeLine.isAlive(LEFT.shiftPoint(ORIGIN)));
                assertFalse(timeLine.isAlive(UP.shiftPoint(ORIGIN)));
                assertFalse(timeLine.isAlive(DOWN.shiftPoint(ORIGIN)));
            } else {
                /*
                 * 0 1 0
                 * 0 1 0
                 * 0 1 0
                 */
                assertTrue(timeLine.isAlive(ORIGIN));
                assertFalse(timeLine.isAlive(RIGHT.shiftPoint(ORIGIN)));
                assertFalse(timeLine.isAlive(LEFT.shiftPoint(ORIGIN)));
                assertTrue(timeLine.isAlive(UP.shiftPoint(ORIGIN)));
                assertTrue(timeLine.isAlive(DOWN.shiftPoint(ORIGIN)));
            }
        }
    }

    @Test
    public void testTickStableStructures() {
        /*   | 0 1 2 3 4 5 6 7 8
         *---|------------------
         * 1 | 0 1 1 0 0 1 1 0 0
         * 2 | 0 1 1 0 0 1 0 1 0
         * 3 | 0 0 0 0 0 0 1 0 0
         */
        // the square
        timeLine.getCurrentTimeInstant().revive(new Point(1, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(1, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 2));

        // the other thing
        timeLine.getCurrentTimeInstant().revive(new Point(5, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(5, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(6, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(6, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(7, 2));

        assertEquals(9, timeLine.getCurrentTimeInstant().getPopulation());
        // the square
        assertTrue(timeLine.isAlive(new Point(1, 1)));
        assertTrue(timeLine.isAlive(new Point(2, 1)));
        assertTrue(timeLine.isAlive(new Point(1, 2)));
        assertTrue(timeLine.isAlive(new Point(2, 2)));

        // the other thing
        assertTrue(timeLine.isAlive(new Point(5, 1)));
        assertTrue(timeLine.isAlive(new Point(5, 2)));
        assertTrue(timeLine.isAlive(new Point(6, 1)));
        assertTrue(timeLine.isAlive(new Point(6, 3)));
        assertTrue(timeLine.isAlive(new Point(7, 2)));

        for (int i = 0; i < MAX_TICKS; i++) {
            timeLine.forward();
            assertEquals(9, timeLine.getCurrentTimeInstant().getPopulation());
            // the square
            assertTrue(timeLine.isAlive(new Point(1, 1)));
            assertTrue(timeLine.isAlive(new Point(2, 1)));
            assertTrue(timeLine.isAlive(new Point(1, 2)));
            assertTrue(timeLine.isAlive(new Point(2, 2)));

            // the other thing
            assertTrue(timeLine.isAlive(new Point(5, 1)));
            assertTrue(timeLine.isAlive(new Point(5, 2)));
            assertTrue(timeLine.isAlive(new Point(6, 1)));
            assertTrue(timeLine.isAlive(new Point(6, 3)));
            assertTrue(timeLine.isAlive(new Point(7, 2)));
        }
    }

    @Test
    public void testTickDieHard() {
        /* Die Hard pattern:
         *   | 1 2 3 4 5 6 7 8
         *---|----------------
         * 1 | 0 0 0 0 0 0 1 0
         * 2 | 1 1 0 0 0 0 0 0
         * 3 | 0 1 0 0 0 1 1 1
         *
         * Should cease to exist in exactly 130 generations
         *
         */
        // the bottom left thing
        timeLine.getCurrentTimeInstant().revive(new Point(1, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 3));

        // other thing
        timeLine.getCurrentTimeInstant().revive(new Point(7, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(6, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(7, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(8, 3));

        assertEquals(7, timeLine.getActualTimeInstant().getPopulation());

        for (int i = 1; i < 130; i++) {
            timeLine.forward();
            assertNotEquals(0, timeLine.getCurrentTimeInstant().getPopulation());
        }

        timeLine.forward();
        assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());
    }

    @Test
    public void testThreading() throws InterruptedException {
        timeLine.getCurrentTimeInstant().revive(new Point(1, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 3));

        timeLine.getCurrentTimeInstant().revive(new Point(7, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(6, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(7, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(8, 3));

        Thread t1 = new Thread(timeLine);
        t1.start();
        while (timeLine.getActualGeneration() < 140) {
            Thread.sleep(1000);
            assertEquals(7, timeLine.getCurrentTimeInstant().getPopulation());
            assertEquals(0, timeLine.getGeneration());
        }
        t1.interrupt();
    }

    @Test
    public void testTickDieHardThreading() throws InterruptedException {
        /* Die Hard pattern:
         *   | 1 2 3 4 5 6 7 8
         *---|----------------
         * 1 | 0 0 0 0 0 0 1 0
         * 2 | 1 1 0 0 0 0 0 0
         * 3 | 0 1 0 0 0 1 1 1
         *
         * Should cease to exist in exactly 130 generations
         *
         */
        // the bottom left thing
        timeLine.getCurrentTimeInstant().revive(new Point(1, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 2));
        timeLine.getCurrentTimeInstant().revive(new Point(2, 3));

        // other thing
        timeLine.getCurrentTimeInstant().revive(new Point(7, 1));
        timeLine.getCurrentTimeInstant().revive(new Point(6, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(7, 3));
        timeLine.getCurrentTimeInstant().revive(new Point(8, 3));

        Thread t1 = new Thread(timeLine);
        t1.start();
        Thread.sleep(1000);

        assertEquals(7, timeLine.getCurrentTimeInstant().getPopulation());

        for (int i = 1; i < 130; i++) {
            timeLine.forward();
            assertNotEquals(0, timeLine.getCurrentTimeInstant().getPopulation());
        }

        timeLine.forward();
        assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());

        t1.interrupt();
    }
}
