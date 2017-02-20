package tests.observer;

import model.observer.Observer;
import model.space.Direction;
import model.space.Node;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ObserverTest {
    private int PATH_LENGTH = 65;
    private int FIELD_SIZE = 100;
    private Node<Set<String>> testNode;
    private Observer<String> testObserver;

    @Before
    public void runBefore() {
        testNode = new Node<>(FIELD_SIZE, FIELD_SIZE);
        testObserver = new Observer<>(testNode);
    }

    @Test
    public void testConstructor() {
        assertEquals(testNode, testObserver.getNode());
    }

    @Test
    public void testMoveBasic() {
        for (Direction direction : Direction.values()) {
            testObserver.move(direction);
            assertEquals(testNode.getNode(direction), testObserver.getNode());
            testObserver.move(direction.opposite());
        }
    }

    @Test
    public void testMoveFar() {
        Node<Set<String>> traveller = testNode;

        for (Direction directionA : Direction.values()) {
            for (Direction directionB : Direction.values()) {
                for (int i = 0; i < PATH_LENGTH; i++) {
                    testObserver.move(directionA);
                    testObserver.move(directionB);
                    traveller = traveller.getNode(directionA).getNode(directionB);
                    assertEquals(traveller, testObserver.getNode());
                }
            }
        }
    }

    @Test
    public void testMoveCurvedSpace() {
        for (Direction direction : Direction.values()) {
            for (int i = 0; i < FIELD_SIZE; i++) {
                testObserver.move(direction);
            }
            assertEquals(testNode, testObserver.getNode());
        }
    }

    @Test
    public void testTeleport() {
        Node<Set<String>> exitNode = testNode.getNodeAt(new Point(12, -123));
        testObserver.teleport(exitNode);
        assertEquals(exitNode, testObserver.getNode());
    }
}
