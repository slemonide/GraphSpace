package examples.Life;

import javafx.scene.layout.GridPane;

public class TwoDimensionalProjection implements Runnable {
    // CONSTANTS:
    public static final int DELAY = 50;
    public static final int SPACING = 1;
    private int sideSize = 8;

    // EFFECTS: constructs a new two dimensional projection of the given timeline
    public TwoDimensionalProjection(TimeLine timeLine) {

    }

    @Override
    // MODIFIES: this
    // EFFECTS: render current timeline every DELAY ms
    public void run() {

    }

    // EFFECTS: produce the side size of the squares
    public int getSideSize() {
        return sideSize;
    }

    // MODIFIES: this
    // EFFECTS: set the height of the projection
    public void setHeight() {

    }

    // MODIFIES: this
    // EFFECTS: set the width of the projection
    public void setWidth() {

    }

    // EFFECTS: returns the projection
    public GridPane getProjection() {
        return null; //stub
    }
}
