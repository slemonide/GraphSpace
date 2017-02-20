package tests.time;

import model.time.Time;
import model.world.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TimeTest {
    private static final int MAX_TICKS = 100;
    private World testWorld;
    private Time testTime;

    @Before
    public void runBefore() {
        testWorld = new World(100, 40);
        testTime = new Time(testWorld);
    }

    @Test
    public void testConstructorWorld() {
        assertEquals(0, testTime.getTime());
        assertEquals(testWorld, testTime.getWorld());
    }

    @Test
    public void testConstructorTime() {
        testTime.tick();
        testTime.tick();
        testTime.tick();
        Time myTime = new Time(testTime);
        assertEquals(3, myTime.getTime());
        assertEquals(testWorld, myTime.getWorld());
    }

    @Test
    public void testTickSimple() {
        testTime.tick();
        assertEquals(1, testTime.getTime());
        assertEquals(testWorld, testTime.getWorld());
    }

    @Test
    public void testTickHard() {
        for (int i = 0; i < MAX_TICKS; i++) {
            assertEquals(i, testTime.getTime());
            assertEquals(testWorld, testTime.getWorld());
            testTime.tick();
        }
    }

    @Test
    public void testTickBackSimple() {
        testTime.tick();
        testTime.tickBack();
        assertEquals(0, testTime.getTime());
        assertEquals(testWorld, testTime.getWorld());
    }

    @Test
    public void testTickBackHard() {
        for (int i = 0; i < MAX_TICKS; i++) {
            assertEquals(i, testTime.getTime());
            assertEquals(testWorld, testTime.getWorld());
            testTime.tick();
        }

        for (int i = MAX_TICKS; i >= 0; i--) {
            assertEquals(i, testTime.getTime());
            assertEquals(testWorld, testTime.getWorld());
            testTime = testTime.tickBack();
        }
    }

    @Test
    public void testGetWorldAtRelative() {
        World futureWorld = testTime.getWorldAtRelative(MAX_TICKS);
        assertNotNull(futureWorld);
        for (int i = 0; i < MAX_TICKS; i++) {
            testTime.tick();
        }
        assertEquals(futureWorld, testTime.getWorld());
    }

    @Test
    public void testGetWorldAtAbsolute() {
        World futureWorld = testTime.getWorldAtAbsolute(MAX_TICKS);
        assertNotNull(futureWorld);
        for (int i = 0; i < MAX_TICKS; i++) {
            testTime.tick();
        }
        assertEquals(futureWorld, testTime.getWorld());
    }

    @Test
    public void testGetWorldAtRelativeAbsolute() {
        assertEquals(testTime.getWorldAtAbsolute(MAX_TICKS),
                testTime.getWorldAtRelative(MAX_TICKS));
        assertEquals(testTime.getWorldAtAbsolute(0),
                testTime.getWorldAtRelative(0));
        assertEquals(testTime.getWorldAtAbsolute(MAX_TICKS / 2),
                testTime.getWorldAtRelative(MAX_TICKS / 2));

        for (int i = 0; i < MAX_TICKS; i++) {
            testTime.tick();
        }
        assertEquals(testWorld, testTime.getWorldAtAbsolute(0));
        assertEquals(testWorld, testTime.getWorldAtRelative(-MAX_TICKS));
    }
}
