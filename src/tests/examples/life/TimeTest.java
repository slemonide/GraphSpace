package tests.examples.life;

import examples.Life.Time;
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
import static org.junit.Assert.assertNotEquals;

public class TimeTest {
    private int WIDTH = 100;
    private int HEIGHT = 100;
    private int MAX_TICKS = 500;
    private Time timeLine;

    private final Point ORIGIN = new Point(0,0);

    @Before
    public void runBefore() {
        timeLine = new Time(new TimeInstant(new Node(WIDTH, HEIGHT), new HashSet<Node>()));
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
        assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(ORIGIN));
        assertEquals(1, timeLine.getGeneration());
        assertEquals(1, timeLine.getActualGeneration());
        assertEquals(0, timeLine.getCurrentTimeInstant().getPopulation());

        for (Direction direction : Direction.values()) {
            timeLine.getCurrentTimeInstant().revive(ORIGIN);
            timeLine.getCurrentTimeInstant().revive(direction.shiftPoint(ORIGIN));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(ORIGIN));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(direction.shiftPoint(ORIGIN)));
            assertEquals(2, timeLine.getCurrentTimeInstant().getPopulation());

            timeLine.forward();
            assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(ORIGIN));
            assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(direction.shiftPoint(ORIGIN)));
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
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(ORIGIN));
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(RIGHT.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(LEFT.shiftPoint(ORIGIN)));
                assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(UP.shiftPoint(ORIGIN)));
                assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(DOWN.shiftPoint(ORIGIN)));
            } else {
                /*
                 * 0 1 0
                 * 0 1 0
                 * 0 1 0
                 */
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(ORIGIN));
                assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(RIGHT.shiftPoint(ORIGIN)));
                assertEquals(DEAD, timeLine.getCurrentTimeInstant().readState(LEFT.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(UP.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(DOWN.shiftPoint(ORIGIN)));
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
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(1, 1)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(2, 1)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(1, 2)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(2, 2)));

        // the other thing
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(5, 1)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(5, 2)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(6, 1)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(6, 3)));
        assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(7, 2)));

        for (int i = 0; i < MAX_TICKS; i++) {
            timeLine.forward();
            assertEquals(9, timeLine.getCurrentTimeInstant().getPopulation());
            // the square
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(1, 1)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(2, 1)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(1, 2)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(2, 2)));

            // the other thing
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(5, 1)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(5, 2)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(6, 1)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(6, 3)));
            assertEquals(ALIVE, timeLine.getCurrentTimeInstant().readState(new Point(7, 2)));
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
        }
        t1.interrupt();
    }
}
