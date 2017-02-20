package tests.creature;

import model.creature.Creature;
import model.world.World;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreatureTest {
    private World testNode;
    private Creature testCreature;

    @Before
    public void runBefore() {
        testNode = new World(100, 100);
        testCreature = new Creature(testNode);
    }

    @Test
    public void testConstructor() {
        assertTrue(testCreature.getNode().readObject().contains(testCreature));
    }

    @Test
    public void testKill() {
        assertNotNull(testCreature.getNode().readObject());
        assertTrue(testCreature.getNode().readObject().contains(testCreature));
        testCreature.kill();
        assertNotNull(testCreature.getNode().readObject());
        assertFalse(testCreature.getNode().readObject().contains(testCreature));
    }
}
