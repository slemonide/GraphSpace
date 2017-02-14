package tests;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import model.Direction;
import model.Node;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.awt.*;

import static org.junit.Assert.*;

@RunWith(HierarchicalContextRunner.class)
public class NodeTest {
    private int MAXSIZE = 200;
    private Node testNode;

    @Before
    public void runBefore() {
        testNode = new Node();
    }

    public class TestConstructor {
        @Test
        public void testConstructor() {
            assertNull(testNode.readObject());
            assertEquals(testNode, testNode.getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT));
        }

        @Test
        public void testConstructorStripeBase() {
            testNode = new Node(1);
            assertNull(testNode.readObject());
            assertEquals(testNode, testNode.getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT));
        }

        @Test
        public void testStripeTwo() {
            testNode = new Node(2);
            assertEquals(testNode, testNode.getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.LEFT).getNode(Direction.RIGHT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT));
            assertEquals(testNode, testNode.getNode(Direction.LEFT).getNode(Direction.LEFT));
        }

        @Test
        public void testConstructorStripeGoFarRight() {
            testNode = new Node(MAXSIZE);
            Node traveller;
            traveller = testNode.getNode(Direction.RIGHT);

            for (int x = 1; x < MAXSIZE; x++) {
                assertNull(traveller.readObject());
                assertEquals(traveller, traveller.getNode(Direction.UP));
                assertEquals(traveller, traveller.getNode(Direction.DOWN));
                assertNotEquals(traveller, testNode);

                traveller = traveller.getNode(Direction.RIGHT);
            }

            assertEquals(traveller, testNode);
        }

        @Test
        public void testConstructorStripeGoFarLeft() {
            testNode = new Node(MAXSIZE);
            Node traveller;
            traveller = testNode.getNode(Direction.LEFT);

            for (int x = 1; x < MAXSIZE; x++) {
                assertNull(traveller.readObject());
                assertEquals(traveller, traveller.getNode(Direction.UP));
                assertEquals(traveller, traveller.getNode(Direction.DOWN));
                assertNotEquals(traveller, testNode);

                traveller = traveller.getNode(Direction.LEFT);
            }

            assertEquals(traveller, testNode);
        }

        @Test
        public void testConstructorPlaneBase() {
            testNode = new Node(1, 1);
            assertNull(testNode.readObject());
            assertEquals(testNode, testNode.getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT));
        }

        @Test
        public void testConstructorPlaneTwoByOne() {
            testNode = new Node(2, 1);
            assertNull(testNode.readObject());
            assertNull(testNode.getNode(Direction.RIGHT).readObject());
            assertNotEquals(testNode, testNode.getNode(Direction.LEFT));
            assertEquals(testNode.getNode(Direction.UP), testNode.getNode(Direction.DOWN));

            assertEquals(testNode, testNode.getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.LEFT).getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT));
            assertEquals(testNode, testNode.getNode(Direction.LEFT).getNode(Direction.RIGHT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.LEFT));
        }

        @Test
        public void testConstructorPlaneOneByTwo() {
            testNode = new Node(1, 2);
            assertNull(testNode.readObject());
            assertNull(testNode.getNode(Direction.DOWN).readObject());
            assertNotEquals(testNode, testNode.getNode(Direction.DOWN));
            assertEquals(testNode.getNode(Direction.DOWN), testNode.getNode(Direction.UP));

            assertEquals(testNode, testNode.getNode(Direction.RIGHT));
            assertEquals(testNode, testNode.getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.UP).getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.UP).getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.DOWN).getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.DOWN).getNode(Direction.UP));
        }

        @Test
        public void testConstructorPlaneTwoByTwo() {
            testNode = new Node(2, 2);
            assertNull(testNode.readObject());
            assertEquals(testNode, testNode.getNode(Direction.UP).getNode(Direction.UP));
            assertEquals(testNode, testNode.getNode(Direction.DOWN).getNode(Direction.DOWN));
            assertEquals(testNode, testNode.getNode(Direction.LEFT).getNode(Direction.LEFT));
            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT));

            assertEquals(testNode, testNode.getNode(Direction.RIGHT).getNode(Direction.DOWN)
                    .getNode(Direction.RIGHT).getNode(Direction.DOWN));
        }

        @Test
        public void testConstructorPlaneBigConnection() {
            int width = MAXSIZE;
            int height = MAXSIZE;
            testNode = new Node(width, height);

            Node currentNode = testNode;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    currentNode = currentNode.getNode(Direction.RIGHT);
                }
                currentNode = currentNode.getNode(Direction.RIGHT);
                currentNode = currentNode.getNode(Direction.DOWN);
            }
            assertEquals(testNode, currentNode);
        }

        @Test
        public void testConstructorPlaneBigEmptiness() {
            int width = MAXSIZE;
            int height = MAXSIZE;
            testNode = new Node(width, height);

            Node currentNode = testNode;
            for (int y = 1; y < height; y++) {
                for (int x = 1; x < width; x++) {
                    currentNode = currentNode.getNode(Direction.RIGHT);
                    assertNotEquals(testNode, currentNode);
                }
                currentNode = currentNode.getNode(Direction.RIGHT);
                currentNode = currentNode.getNode(Direction.DOWN);
            }
        }
    }

    public class testPlace {
        @Test
        public void testPlaceReadBasic() {
            testNode.place("A Sign On The Wall");
            assertEquals("A Sign On The Wall", testNode.readObject());

            // Also test the wrapping of space
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.UP).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.DOWN).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.LEFT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.RIGHT).readObject());

            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.UP).getNode(Direction.UP).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.LEFT).getNode(Direction.LEFT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT).readObject());

            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.UP).getNode(Direction.DOWN).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.DOWN).getNode(Direction.UP).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.LEFT).getNode(Direction.RIGHT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.RIGHT).getNode(Direction.LEFT).readObject());

            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.UP).getNode(Direction.RIGHT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.LEFT).getNode(Direction.UP).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.RIGHT).getNode(Direction.UP).readObject());

            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.UP).getNode(Direction.LEFT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.DOWN).getNode(Direction.LEFT).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.LEFT).getNode(Direction.DOWN).readObject());
            assertEquals("A Sign On The Wall",
                    testNode.getNode(Direction.RIGHT).getNode(Direction.DOWN).readObject());
        }

        @Test
        public void testPlaceReadTwoByTwo() {
            testNode = new Node(2, 2);

            testNode.place(1);
            testNode = testNode.getNode(Direction.RIGHT);
            testNode.place(2);
            testNode = testNode.getNode(Direction.DOWN);
            testNode.place(3);
            testNode = testNode.getNode(Direction.LEFT);
            testNode.place(4);

            testNode = testNode.getNode(Direction.UP);
            assertEquals(1, testNode.readObject());
            testNode = testNode.getNode(Direction.RIGHT);
            assertEquals(2, testNode.readObject());
            testNode = testNode.getNode(Direction.DOWN);
            assertEquals(3, testNode.readObject());
            testNode = testNode.getNode(Direction.LEFT);
            assertEquals(4, testNode.readObject());

            testNode = testNode.getNode(Direction.LEFT);
            assertEquals(3, testNode.readObject());
            testNode = testNode.getNode(Direction.DOWN);
            assertEquals(2, testNode.readObject());
            testNode = testNode.getNode(Direction.RIGHT);
            assertEquals(1, testNode.readObject());
            testNode = testNode.getNode(Direction.UP);
            assertEquals(4, testNode.readObject());
        }
    }

    public class TestRead {
        @Test
        public void testRead() {
            testNode = new Node(3, 3);

            testNode.place("a");
            testNode.getNode(Direction.RIGHT).place("b");
            testNode.getNode(Direction.LEFT).place("c");

            testNode.getNode(Direction.DOWN).place("e");
            testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT).place("f");
            testNode.getNode(Direction.DOWN).getNode(Direction.LEFT).place("g");

            testNode.getNode(Direction.UP).place("n");
            testNode.getNode(Direction.UP).getNode(Direction.RIGHT).place("o");
            testNode.getNode(Direction.UP).getNode(Direction.LEFT).place("p");

            assertEquals("a", testNode.readObject());
            assertEquals("b", testNode.getNode(Direction.RIGHT).readObject());
            assertEquals("c", testNode.getNode(Direction.LEFT).readObject());

            assertEquals("e", testNode.getNode(Direction.DOWN).readObject());
            assertEquals("f", testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT).readObject());
            assertEquals("g", testNode.getNode(Direction.DOWN).getNode(Direction.LEFT).readObject());

            assertEquals("n", testNode.getNode(Direction.UP).readObject());
            assertEquals("o", testNode.getNode(Direction.UP).getNode(Direction.RIGHT).readObject());
            assertEquals("p", testNode.getNode(Direction.UP).getNode(Direction.LEFT).readObject());
        }

        @Test
        public void testReadVertical() {
            testNode = new Node(1, 5);
            testNode.place(1);
            testNode.getNode(Direction.DOWN).place(2);
            testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).place(3);
            testNode.getNode(Direction.UP).place(4);
            testNode.getNode(Direction.UP).getNode(Direction.UP).place(5);

            assertEquals(1, testNode.readObject());
            assertEquals(2, testNode.getNode(Direction.DOWN).readObject());
            assertEquals(3, testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).readObject());
            assertEquals(4, testNode.getNode(Direction.UP).readObject());
            assertEquals(5, testNode.getNode(Direction.UP).getNode(Direction.UP).readObject());
        }

        @Test
        public void testReadHorizontal() {
            testNode = new Node(5, 1);
            testNode.place(1);
            testNode.getNode(Direction.RIGHT).place(2);
            testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT).place(3);
            testNode.getNode(Direction.LEFT).place(4);
            testNode.getNode(Direction.LEFT).getNode(Direction.LEFT).place(5);

            assertEquals(1, testNode.readObject());
            assertEquals(2, testNode.getNode(Direction.RIGHT).readObject());
            assertEquals(3, testNode.getNode(Direction.RIGHT).getNode(Direction.RIGHT).readObject());
            assertEquals(4, testNode.getNode(Direction.LEFT).readObject());
            assertEquals(5, testNode.getNode(Direction.LEFT).getNode(Direction.LEFT).readObject());
        }
    }

    public class testConnect {
        @Test
        public void testConnectItself() {
            for (Direction dir : Direction.values()) {
                testNode.connect(testNode, dir);
                assertEquals(testNode.getNode(dir), testNode);
            }
        }

        @Test
        public void testConnectOne() {
            Node testNodeA = new Node();
            Node testNodeB = new Node();

            for (Direction dir : Direction.values()) {
                testNodeA.connect(testNodeB, dir);
                assertEquals(testNodeA.getNode(dir), testNodeB);
                assertEquals(testNodeA.getNode(dir).getNode(dir.opposite()), testNodeA);
            }
        }
    }

    public class TestGlueBottom {
        @Test
        public void testGlueBottom() {
            testNode = new Node(MAXSIZE);
            testNode.glueBottom(new Node(MAXSIZE));
            Node traveller;
            traveller = testNode.getNode(Direction.LEFT);

            for (int x = 1; x < MAXSIZE; x++) {
                assertNull(traveller.readObject());
                assertEquals(traveller, traveller.getNode(Direction.UP).getNode(Direction.UP));
                assertEquals(traveller, traveller.getNode(Direction.DOWN).getNode(Direction.DOWN));
                assertEquals(traveller, traveller.getNode(Direction.UP).getNode(Direction.DOWN));
                assertEquals(traveller, traveller.getNode(Direction.DOWN).getNode(Direction.UP));
                assertNotEquals(traveller, traveller.getNode(Direction.UP));
                assertNotEquals(traveller, traveller.getNode(Direction.DOWN));
                assertNotEquals(traveller, testNode);

                traveller = traveller.getNode(Direction.LEFT);
            }

            assertEquals(traveller, testNode);
        }

        @Test
        public void testGlueBottomThreeLayers() {
            testNode = new Node(MAXSIZE);
            testNode.glueBottom(new Node(MAXSIZE));
            testNode.glueBottom(new Node(MAXSIZE));
            Node traveller;
            traveller = testNode.getNode(Direction.LEFT);

            for (int x = 1; x < MAXSIZE; x++) {
                assertNull(traveller.readObject());
                assertEquals(traveller, traveller.getNode(Direction.UP).getNode(Direction.UP).getNode(Direction.UP));
                assertEquals(traveller, traveller.getNode(Direction.DOWN).getNode(Direction.DOWN).getNode(Direction.DOWN));
                assertEquals(traveller, traveller.getNode(Direction.UP).getNode(Direction.DOWN));
                assertEquals(traveller, traveller.getNode(Direction.DOWN).getNode(Direction.UP));
                assertNotEquals(traveller, traveller.getNode(Direction.UP));
                assertNotEquals(traveller, traveller.getNode(Direction.DOWN));
                assertNotEquals(traveller.getNode(Direction.UP), traveller.getNode(Direction.DOWN));
                assertNotEquals(traveller, testNode);

                traveller = traveller.getNode(Direction.LEFT);
            }

            assertEquals(traveller, testNode);
        }

        @Test
        public void testGlueBottomRead3() {
            testNode = new Node();
            testNode.glueBottom(new Node());
            testNode.glueBottom(new Node());

            testNode.place(0);
            testNode.getNode(Direction.DOWN).place(1);
            testNode.getNode(Direction.UP).place(-1);

            assertEquals(0, testNode.readObject());
            assertEquals(1, testNode.getNode(Direction.DOWN).readObject());
            assertEquals(-1, testNode.getNode(Direction.UP).readObject());
        }

        @Test
        public void testGlueBottomRead5() {
            testNode = new Node();
            testNode.glueBottom(new Node());
            testNode.glueBottom(new Node());
            testNode.glueBottom(new Node());
            testNode.glueBottom(new Node());

            testNode.place(0);
            testNode.getNode(Direction.DOWN).place(1);
            testNode.getNode(Direction.UP).place(-1);
            testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).place(2);
            testNode.getNode(Direction.UP).getNode(Direction.UP).place(-2);

            assertEquals(0, testNode.readObject());
            assertEquals(1, testNode.getNode(Direction.DOWN).readObject());
            assertEquals(-1, testNode.getNode(Direction.UP).readObject());
            assertEquals(2, testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).readObject());
            assertEquals(-2, testNode.getNode(Direction.UP).getNode(Direction.UP).readObject());
        }

        @Test
        public void testGlueBottomInsertSingleInPlaneRead() {
            /*O - m - O
              |   |   |
              |   w   |
              |   |   |
              e - y - h
              |   |   |
              O - t - O
             */
            testNode = new Node(7, 42);
            testNode.place("me");
            testNode.getNode(Direction.DOWN).place("you");
            testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).place("them");
            testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT).place("him");
            testNode.getNode(Direction.DOWN).getNode(Direction.LEFT).place("her");

            testNode.glueBottom(new Node());
            testNode.getNode(Direction.DOWN).place("what?");
            assertEquals("me", testNode.readObject());
            assertEquals("what?", testNode.getNode(Direction.DOWN).readObject());
            assertEquals("what?", testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT).readObject());
            assertEquals("what?", testNode.getNode(Direction.DOWN).getNode(Direction.LEFT).readObject());
            assertEquals("you", testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).readObject());
            assertEquals("him", testNode.getNode(Direction.RIGHT).getNode(Direction.DOWN).readObject());
            assertEquals("her", testNode.getNode(Direction.LEFT).getNode(Direction.DOWN).readObject());
            assertEquals("you", testNode.getNode(Direction.RIGHT).getNode(Direction.DOWN).getNode(Direction.LEFT).readObject());
        }

        @Test
        public void testGlueBottomInsertStripeInPlaneRead() {
            testNode = new Node(100, 200);
            testNode.place("me");
            testNode.getNode(Direction.DOWN).place("you");

            testNode.glueBottom(new Node(20));
            assertEquals("me", testNode.readObject());
            assertEquals("you", testNode.getNode(Direction.DOWN).getNode(Direction.DOWN).readObject());
        }
    }

    public class testSize {
        @Test
        public void testSizeBase() {
            assertEquals(1, testNode.size());
        }

        @Test
        public void testSizeTwoCase() {
            testNode.connect(new Node(), Direction.RIGHT);
            assertEquals(2, testNode.size());
        }

        @Test
        public void testSizeStripe() {
            testNode = new Node(100);
            assertEquals(100, testNode.size());
        }

        @Test
        public void testSizePlane() {
            testNode = new Node(100, 342);
            assertEquals(100 * 342, testNode.size());
        }

        @Test
        public void testSizeDoubleStripeEqual() {
            testNode = new Node(200);
            testNode.glueBottom(new Node(200));

            assertEquals(200 * 2, testNode.size());
        }

        @Test
        public void testSizeDoubleStripeDifferent() {
            testNode = new Node(200);
            testNode.glueBottom(new Node(100));

            assertEquals(200 + 100, testNode.size());
        }

        @Test
        public void testSizeCombined() {
            testNode = new Node(100);
            testNode.glueBottom(new Node(42, 15));
            testNode.glueBottom(new Node());

            assertEquals(100 + 42 * 15 + 1, testNode.size());
        }
    }

    public class testWidth {
        @Test
        public void testWidthBase() {
            assertEquals(1, testNode.width());
        }

        @Test
        public void testWidthStripe() {
            assertEquals(1232, new Node(1232).width());
        }

        @Test
        public void testWidthPlane() {
            assertEquals(231, new Node(231, 23).width());
        }
    }

    public class testHeight {
        @Test
        public void testHeightBase() {
            assertEquals(1, testNode.height());
        }

        @Test
        public void testHeightStripe() {
            assertEquals(1, new Node(1232).height());
        }

        @Test
        public void testHeightPlane() {
            assertEquals(23, new Node(231, 23).height());
        }
    }

    @Test
    public void testGetNodeAtBase() {
        assertEquals(testNode, testNode.getNodeAt(new Point(0, 0)));
        assertEquals(testNode.getNode(Direction.UP), testNode.getNodeAt(new Point(0, -1)));
        assertEquals(testNode.getNode(Direction.DOWN), testNode.getNodeAt(new Point(0, 1)));
        assertEquals(testNode.getNode(Direction.LEFT), testNode.getNodeAt(new Point(-1, 0)));
        assertEquals(testNode.getNode(Direction.RIGHT), testNode.getNodeAt(new Point(1, 0)));
    }

    @Test
    public void testGetNodeAtConflict() {
        assertEquals(testNode.getNode(Direction.UP).getNode(Direction.LEFT),
                testNode.getNodeAt(new Point(-1, -1)));
        assertEquals(testNode.getNode(Direction.LEFT).getNode(Direction.DOWN),
                testNode.getNodeAt(new Point(-1, 1)));
        assertEquals(testNode.getNode(Direction.DOWN).getNode(Direction.RIGHT),
                testNode.getNodeAt(new Point(1, -1)));
        assertEquals(testNode.getNode(Direction.RIGHT).getNode(Direction.UP),
                testNode.getNodeAt(new Point(1, 1)));
    }

    @Test
    public void testGetNodeAtFar() {
    }
}
