package examples.Life;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.space.Point;

public class TwoDimensionalProjection implements Runnable {
    // CONSTANTS:
    public static final int DELAY = 50;
    public static final int SPACING = 1;
    public static final Color ALIVE_COLOR = Color.BEIGE;
    public static final Color DEAD_COLOR = Color.BLACK;

    private int sideSize = 8;
    private GridPane scene;
    private double height;
    private double width;
    private TimeLine timeLine;


    // REQUIRES: timeLine is not null, height, width >= 0
    // EFFECTS: constructs a new two dimensional projection of the given timeline
    public TwoDimensionalProjection(TimeLine timeLine, double height, double width) {
        scene = new GridPane();
        this.height = height;
        this.width = width;
        this.timeLine = timeLine;

        initialize();
    }

    // EFFECTS: create the grid that is appropriate for the given height and width
    private void initialize() {
        int columnsNumber = getCellNumber(width, SPACING, sideSize);
        int rowsNumber = getCellNumber(height, SPACING, sideSize);

        for (int x = 0; x < columnsNumber; x++) {
            for (int y = 0; y < rowsNumber; y++) {
                scene.add(new Rectangle(sideSize, sideSize, DEAD_COLOR), x, y);
            }
        }
    }

    @Override
    // MODIFIES: this
    // EFFECTS: render current timeline every DELAY ms
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            render(timeLine);
        }
    }

    // EFFECTS: render current timeline on the scene
    private void render(TimeLine timeLine) {
        int columnsNumber = getCellNumber(width, SPACING, sideSize);
        int rowsNumber = getCellNumber(height, SPACING, sideSize);

        int centerColumn = columnsNumber / 2;
        int centerRow = rowsNumber / 2;

        for (Node node : scene.getChildren()) {
            int x = GridPane.getColumnIndex(node);
            int y = GridPane.getRowIndex(node);

            Rectangle square = (Rectangle) node;
            if (timeLine.isAlive(new Point(x - centerColumn, y - centerRow))) {
                square.setFill(ALIVE_COLOR);
            } else {
                square.setFill(DEAD_COLOR);
            }
        }
    }

    // EFFECTS: returns the number of cells that can be packed in the given dimension
    private int getCellNumber(double dimension, int spacing, int sideSize) {
        return ((int) dimension - spacing) / (sideSize + spacing);
    }

    // EFFECTS: produce the side size of the squares
    public int getSideSize() {
        return sideSize;
    }

    // REQUIRES: height >= 0
    // MODIFIES: this
    // EFFECTS: set the height of the projection
    public void setHeight(double height) {
        this.height = height;
        initialize();
    }

    // REQUIRES: width >= 0
    // MODIFIES: this
    // EFFECTS: set the width of the projection
    public void setWidth(double width) {
        this.width = width;
        initialize();
    }

    // EFFECTS: returns the projection
    public GridPane getProjection() {
        return scene;
    }
}
