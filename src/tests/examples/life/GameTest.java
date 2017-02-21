package tests.examples.life;

import examples.Life.Game;
import model.space.Direction;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import static examples.Life.State.ALIVE;
import static examples.Life.State.DEAD;
import static model.space.Direction.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GameTest {
    private static final int MAX_TICKS = 100;
    private final double TOL = 0.1;
    private final int HEIGHT = 100;
    private final int WIDTH = 100;
    private final Point ORIGIN = new Point(0,0);
    private final Point DISTANT_POINT = new Point(HEIGHT / 2 + 32, WIDTH / 3 - 50);
    private Game testGame;

    @Before
    public void runBefore() {
        testGame = new Game(WIDTH, HEIGHT);
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testGame.getGeneration());
        assertEquals(0, testGame.getPopulation());

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Point selectedPoint = new Point(x, y);

                assertEquals(DEAD, testGame.readState(selectedPoint));
            }
        }
    }

    @Test
    public void testRevive() {
        testGame.revive(new Point(0, 0));
        testGame.revive(new Point(1, 100));

        assertEquals(ALIVE, testGame.readState(new Point(0, 0)));
        assertEquals(ALIVE, testGame.readState(new Point(1, 100)));
        assertEquals(0, testGame.getGeneration());
        assertEquals(2, testGame.getPopulation());
    }

    @Test
    public void testKill() {
        testGame.revive(new Point(-23, 123));
        testGame.revive(new Point(400, 1));
        testGame.revive(new Point(401, 1));
        testGame.revive(new Point(0, 0));

        assertEquals(ALIVE, testGame.readState(new Point(0, 0)));
        testGame.kill(new Point(0, 0));
        assertEquals(DEAD, testGame.readState(new Point(0, 0)));
        assertEquals(0, testGame.getGeneration());
        assertEquals(3, testGame.getPopulation());
    }

    @Test
    public void testPopulate() {
        testGame.populate(0);
        assertEquals(0, testGame.getPopulation());
        assertEquals(0, testGame.getGeneration());

        testGame.populate(0.5);
        assertEquals(HEIGHT * WIDTH * 0.5, testGame.getPopulation(), TOL);
        assertEquals(0, testGame.getGeneration());

        testGame.purge();
        testGame.populate(0.33);
        assertEquals(HEIGHT * WIDTH * 0.33, testGame.getPopulation(), TOL);
        assertEquals(0, testGame.getGeneration());

        testGame.purge();
        assertEquals(0, testGame.getPopulation());
        assertEquals(0, testGame.getGeneration());
        testGame.populate(1);
        assertEquals(HEIGHT * WIDTH, testGame.getPopulation());
        assertEquals(0, testGame.getGeneration());
    }

    @Test
    public void testPopulateTwice() {
        testGame.populate(0.5);
        testGame.populate(0.5);
        assertEquals(HEIGHT * WIDTH, testGame.getPopulation());

        testGame.purge();

        testGame.populate(0.3);
        testGame.populate(0.5);
        testGame.populate(0.2);
        assertEquals(HEIGHT * WIDTH, testGame.getPopulation());
    }

    @Test
    public void testMove() {
        testGame.revive(ORIGIN);
        testGame.revive(DISTANT_POINT);
        assertEquals(ALIVE, testGame.readState(ORIGIN));
        assertEquals(ALIVE, testGame.readState(DISTANT_POINT));

        for (Direction direction : Direction.values()) {
            assertEquals(DEAD, testGame.readState(direction.shiftPoint(ORIGIN)));
            assertEquals(DEAD, testGame.readState(direction.shiftPoint(DISTANT_POINT)));
        }

        for (Direction direction : Direction.values()) {
            testGame.move(direction);
            assertEquals(DEAD, testGame.readState(ORIGIN));
            assertEquals(DEAD, testGame.readState(DISTANT_POINT));
            testGame.move(direction.opposite());
            assertEquals(ALIVE, testGame.readState(ORIGIN));
            assertEquals(ALIVE, testGame.readState(DISTANT_POINT));
        }
    }

    @Test
    public void testMoveFar() {
        testGame.revive(ORIGIN);
        testGame.revive(DISTANT_POINT);

        testGame.move(UP);
        testGame.move(UP);
        testGame.move(UP);
        testGame.move(UP);
        testGame.move(UP); // 5 up

        testGame.move(RIGHT);
        testGame.move(RIGHT); // 2 right

        testGame.move(DOWN);
        testGame.move(DOWN);
        testGame.move(DOWN);
        testGame.move(DOWN);
        testGame.move(DOWN); // 5 down

        testGame.move(LEFT);
        testGame.move(LEFT); // 2 left

        assertEquals(ALIVE, testGame.readState(ORIGIN));
        assertEquals(ALIVE, testGame.readState(DISTANT_POINT));
    }

    // Now for tick...
    // ----------------------------------------

    @Test
    public void testTickBasic() {
        for (int i = 1; i < MAX_TICKS; i++) {
            testGame.tick();
            assertEquals(i, testGame.getGeneration());
            assertEquals(0, testGame.getPopulation());
        }
    }

    @Test
    public void testTickDieInOneTick() {
        testGame.revive(ORIGIN);
        assertEquals(0, testGame.getGeneration());
        assertEquals(1, testGame.getPopulation());
        testGame.tick();
        assertEquals(DEAD, testGame.readState(ORIGIN));
        assertEquals(1, testGame.getGeneration());
        assertEquals(0, testGame.getPopulation());

        for (Direction direction : Direction.values()) {
            testGame.revive(ORIGIN);
            testGame.revive(direction.shiftPoint(ORIGIN));
            assertEquals(ALIVE, testGame.readState(ORIGIN));
            assertEquals(ALIVE, testGame.readState(direction.shiftPoint(ORIGIN)));
            assertEquals(2, testGame.getPopulation());

            testGame.tick();
            assertEquals(DEAD, testGame.readState(ORIGIN));
            assertEquals(DEAD, testGame.readState(direction.shiftPoint(ORIGIN)));
            assertEquals(0, testGame.getPopulation());
        }
    }

    @Test
    public void testTickSimpleOscillator() {
        testGame.revive(ORIGIN);
        testGame.revive(UP.shiftPoint(ORIGIN));
        testGame.revive(DOWN.shiftPoint(ORIGIN));
        /*
         * 0 1 0
         * 0 1 0
         * 0 1 0
         */

        for (int i = 0; i < MAX_TICKS; i++) {
            testGame.tick();

            if (i % 2 == 0) {
                /*
                 * 0 0 0
                 * 1 1 1
                 * 0 0 0
                 */
                assertEquals(ALIVE, testGame.readState(ORIGIN));
                assertEquals(ALIVE, testGame.readState(RIGHT.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, testGame.readState(LEFT.shiftPoint(ORIGIN)));
                assertEquals(DEAD, testGame.readState(UP.shiftPoint(ORIGIN)));
                assertEquals(DEAD, testGame.readState(DOWN.shiftPoint(ORIGIN)));
            } else {
                /*
                 * 0 1 0
                 * 0 1 0
                 * 0 1 0
                 */
                assertEquals(ALIVE, testGame.readState(ORIGIN));
                assertEquals(DEAD, testGame.readState(RIGHT.shiftPoint(ORIGIN)));
                assertEquals(DEAD, testGame.readState(LEFT.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, testGame.readState(UP.shiftPoint(ORIGIN)));
                assertEquals(ALIVE, testGame.readState(DOWN.shiftPoint(ORIGIN)));
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
        testGame.revive(new Point(1, 1));
        testGame.revive(new Point(2, 1));
        testGame.revive(new Point(1, 2));
        testGame.revive(new Point(2, 2));

        // the other thing
        testGame.revive(new Point(5, 1));
        testGame.revive(new Point(5, 2));
        testGame.revive(new Point(6, 1));
        testGame.revive(new Point(6, 3));
        testGame.revive(new Point(7, 2));

        assertEquals(9, testGame.getPopulation());
        // the square
        assertEquals(ALIVE, testGame.readState(new Point(1, 1)));
        assertEquals(ALIVE, testGame.readState(new Point(2, 1)));
        assertEquals(ALIVE, testGame.readState(new Point(1, 2)));
        assertEquals(ALIVE, testGame.readState(new Point(2, 2)));

        // the other thing
        assertEquals(ALIVE, testGame.readState(new Point(5, 1)));
        assertEquals(ALIVE, testGame.readState(new Point(5, 2)));
        assertEquals(ALIVE, testGame.readState(new Point(6, 1)));
        assertEquals(ALIVE, testGame.readState(new Point(6, 3)));
        assertEquals(ALIVE, testGame.readState(new Point(7, 2)));

        for (int i = 0; i < MAX_TICKS; i++) {
            testGame.tick();
            assertEquals(9, testGame.getPopulation());
            // the square
            assertEquals(ALIVE, testGame.readState(new Point(1, 1)));
            assertEquals(ALIVE, testGame.readState(new Point(2, 1)));
            assertEquals(ALIVE, testGame.readState(new Point(1, 2)));
            assertEquals(ALIVE, testGame.readState(new Point(2, 2)));

            // the other thing
            assertEquals(ALIVE, testGame.readState(new Point(5, 1)));
            assertEquals(ALIVE, testGame.readState(new Point(5, 2)));
            assertEquals(ALIVE, testGame.readState(new Point(6, 1)));
            assertEquals(ALIVE, testGame.readState(new Point(6, 3)));
            assertEquals(ALIVE, testGame.readState(new Point(7, 2)));
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
        testGame.revive(new Point(1, 2));
        testGame.revive(new Point(2, 2));
        testGame.revive(new Point(2, 3));

        // other thing
        testGame.revive(new Point(7, 1));
        testGame.revive(new Point(6, 3));
        testGame.revive(new Point(7, 3));
        testGame.revive(new Point(8, 3));


        for (int i = 1; i < 130; i++) {
            testGame.tick();
            assertNotEquals(0, testGame.getPopulation());
        }

        testGame.tick();
        assertEquals(0, testGame.getPopulation());
    }

    @Test
    public void testThreading() throws InterruptedException {
        testGame.revive(new Point(1, 2));
        testGame.revive(new Point(2, 2));
        testGame.revive(new Point(2, 3));

        testGame.revive(new Point(7, 1));
        testGame.revive(new Point(6, 3));
        testGame.revive(new Point(7, 3));
        testGame.revive(new Point(8, 3));

        Thread t1 = new Thread(testGame);
        t1.start();
        while (testGame.getGeneration() < 140) {
            Thread.sleep(1000);
        }
        t1.interrupt();
    }
}
