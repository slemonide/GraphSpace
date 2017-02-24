package tests.examples.life;

import examples.Life.TimeInstant;
import examples.Life.TimeLine;
import examples.Life.TwoDimensionalProjection;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import model.space.Node;
import org.junit.Before;
import org.junit.Test;

public class TwoDimensionalProjectionTest {
    private TimeInstant timeInstant;
    private TimeLine timeLine;
    private Scene scene;
    private GridPane projectionPane;

    private int HEIGHT = 200;
    private int WIDTH = 200;

    private TwoDimensionalProjection projection;

    @Before
    public void runBefore() {
        Node field = new Node(WIDTH, HEIGHT);
        timeInstant = new TimeInstant(field);
        timeLine = new TimeLine(timeInstant);

        projection = new TwoDimensionalProjection(timeLine);

        projectionPane = projection.getProjection();
        scene = new Scene(projectionPane);
    }

    @Test
    public void testConstructor() {
        for (javafx.scene.Node node : projectionPane.getChildren()) {
        }
    }
}
