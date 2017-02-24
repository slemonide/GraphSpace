package tests.examples.life;

import examples.Life.TimeInstant;
import examples.Life.TimeLine;
import examples.Life.TwoDimensionalProjection;
import javafx.collections.ObservableList;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import model.space.Node;
import model.space.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TwoDimensionalProjectionTest {
    private static final int SCN_WIDTH = 600;
    private static final int SCN_HEIGHT = 800;
    private final double MAX_ERROR = 0.00001;

    private TimeInstant timeInstant;
    private TimeLine timeLine;
    private GridPane projectionPane;

    private int HEIGHT = 200;
    private int WIDTH = 200;

    private TwoDimensionalProjection projection;

    @Before
    public void runBefore() {
        Node field = new Node(WIDTH, HEIGHT);
        timeInstant = new TimeInstant(field);
        timeLine = new TimeLine(timeInstant);

        projection = new TwoDimensionalProjection(timeLine, SCN_HEIGHT, SCN_WIDTH);
        projectionPane = projection.getProjection();
    }

    @Test
    public void testConstructor() {
        for (javafx.scene.Node node : projectionPane.getChildren()) {
            Rectangle square = (Rectangle) node;
            assertEquals(projection.getSideSize(), square.getHeight(), MAX_ERROR);
            assertEquals(projection.getSideSize(), square.getWidth(), MAX_ERROR);

            assertEquals(getX(node), square.getX(), MAX_ERROR);
            assertEquals(getY(node), square.getY(), MAX_ERROR);

            //assertEquals(Color.BLACK, square.getFill());
        }

        assertEquals(SCN_HEIGHT, projectionPane.getHeight(), MAX_ERROR);
        assertEquals(SCN_WIDTH, projectionPane.getWidth(), MAX_ERROR);
    }

    private double getY(javafx.scene.Node node) {
        int row = GridPane.getRowIndex(node);
        return (projection.getSideSize() + TwoDimensionalProjection.SPACING) * row;
    }

    private double getX(javafx.scene.Node node) {
        int column = GridPane.getColumnIndex(node);
        return (projection.getSideSize() + TwoDimensionalProjection.SPACING) * column;
    }

    @Test
    public void testSetHeight() {
        projection.setHeight(0);
        assertEquals(0, projectionPane.getHeight(), MAX_ERROR);
        projection.setHeight(0.1);
        //assertEquals(0.1, projectionPane.getHeight(), MAX_ERROR);
        projection.setHeight(1);
        //assertEquals(1, projectionPane.getHeight(), MAX_ERROR);
        projection.setHeight(19430.123);
        assertEquals(19430.123, projectionPane.getHeight(), MAX_ERROR);
    }

    @Test
    public void testSetWidth() {
        projection.setWidth(0);
        assertEquals(0, projectionPane.getWidth(), MAX_ERROR);
        projection.setWidth(0.1);
        assertEquals(0.1, projectionPane.getWidth(), MAX_ERROR);
        projection.setWidth(1);
        assertEquals(1, projectionPane.getWidth(), MAX_ERROR);
        projection.setWidth(19430.123);
        assertEquals(19430.123, projectionPane.getWidth(), MAX_ERROR);
    }

    @Test
    public void testRunBasic() {
        Thread projectionRenderThread = new Thread(timeLine);
        projectionRenderThread.start();

        timeInstant.revive(new Point(0, 0));

        int centerRow = getCenterRow(projection);
        int centerColumn = getCenterColumn(projection);

        Rectangle centerSquare = (Rectangle) getNodeByRowColumnIndex(centerRow, centerColumn, projectionPane);
        //assertEquals(Color.BLACK, centerSquare.getFill());

        for (javafx.scene.Node node : projectionPane.getChildren()) {
            Rectangle square = (Rectangle) node;

            if (!square.getId().equals(centerSquare.getId())) {
                assertEquals(projection.getSideSize(), square.getHeight(), MAX_ERROR);
                assertEquals(projection.getSideSize(), square.getWidth(), MAX_ERROR);

                assertEquals(getX(node), square.getX(), MAX_ERROR);
                assertEquals(getY(node), square.getY(), MAX_ERROR);

                //assertEquals(Color.BLACK, square.getFill());
            }
        }

        projectionRenderThread.interrupt();
    }

    @Test
    public void testRunHard() {
        Thread projectionRenderThread = new Thread(timeLine);
        projectionRenderThread.start();

        timeInstant.revive(new Point(0, 0));
        timeInstant.revive(new Point(1, 1));
        timeInstant.revive(new Point(-1, -1));

        int centerRow = getCenterRow(projection);
        int centerColumn = getCenterColumn(projection);

        Rectangle centerSquare = (Rectangle) getNodeByRowColumnIndex(centerRow, centerColumn, projectionPane);
        //assertEquals(Color.BLACK, centerSquare.getFill());
        Rectangle squareOne = (Rectangle) getNodeByRowColumnIndex(centerRow + 1, centerColumn + 1, projectionPane);
        //assertEquals(Color.BLACK, centerSquare.getFill());
        Rectangle squareTwo = (Rectangle) getNodeByRowColumnIndex(centerRow - 1, centerColumn - 1, projectionPane);
        //assertEquals(Color.BLACK, centerSquare.getFill());

        projectionRenderThread.interrupt();
    }

    private int getCenterColumn(TwoDimensionalProjection projection) {
        int totalNumberOfColumns =
                ((int) projectionPane.getWidth() - TwoDimensionalProjection.SPACING) /
                        (projection.getSideSize() + TwoDimensionalProjection.SPACING);
        return totalNumberOfColumns / 2;
    }

    private int getCenterRow(TwoDimensionalProjection projection) {
        int totalNumberOfRows =
                ((int) projectionPane.getHeight() - TwoDimensionalProjection.SPACING) /
                        (projection.getSideSize() + TwoDimensionalProjection.SPACING);
        return totalNumberOfRows / 2;
    }

    // Taken from: http://stackoverflow.com/questions/20825935/javafx-get-node-by-row-and-column
    private javafx.scene.Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        javafx.scene.Node result = null;
        ObservableList<javafx.scene.Node> children = gridPane.getChildren();

        for (javafx.scene.Node node : children) {
            if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
