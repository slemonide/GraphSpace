package examples.Life;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.space.Point;

import static model.space.Direction.*;

public class TwoDimensionalProjectionOld {
    // CONSTANTS:
    private static final int INTERVAL = 50;
    private int SPACING = 1;

    private int sideSize = 8; // 16
    private double height = 100;
    private double width = 100;

    public TwoDimensionalProjectionOld(Scene scene, TimeLine timeLine) {
        // Initialization stuff
        /*
        TimeInstant initialTimeInstant = new TimeInstant(new Node(fieldWidth, fieldHeight), new HashSet<Node>());
        initialTimeInstant.populate(cellDensity);
        TimeLine timeLine = new TimeLine(initialTimeInstant);
        */
        Thread gameThread = new Thread(timeLine);
        gameThread.start();
        // Done

        // What the heck is this?
        Group root = new Group();

        scene.setFill(Color.BLACK);

        // Start here
        GridPane squares = new GridPane();
        squares.setHgap(SPACING);
        squares.setVgap(SPACING);
        populateRenderCells(squares);

        // This shall be moved to an upper class
        root.getChildren().add(squares);

        scene.setOnKeyPressed(ke -> {
            switch (ke.getCode()) {
                case R:
                    // TODO: fix purge
                    //timeInstant.purge();
                    //timeInstant.populate(cellDensity);
                    break;
                case UP:
                    timeLine.move(UP);
                    break;
                case DOWN:
                    timeLine.move(DOWN);
                    break;
                case LEFT:
                    timeLine.move(LEFT);
                    break;
                case RIGHT:
                    timeLine.move(RIGHT);
                    break;
                case SPACE:
                    long startTime = System.nanoTime();
                    timeLine.forward();
                    long endTime = System.nanoTime();

                    long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                    System.out.println("forward() time: " + duration + " ms");

                    startTime = System.nanoTime();
                    //render(timeLine);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                    System.out.println("render() time: " + duration + " ms");
                    break;
                /*
                case F:
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
                    break;
                case F11:
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
                    break;
                case ESCAPE:
                    mainScreen(primaryStage);
                    gameThread.interrupt();
                    break;
                case Q:
                    mainScreen(primaryStage);
                    gameThread.interrupt();
                    break;
                    */
                case MINUS:
                    sideSize = Math.max(sideSize - 1, 0);
                    //render(timeLine);
                    break;
                case PLUS:
                    sideSize++;
                    //render(timeLine);
                case P:
                    if (!gameThread.isInterrupted()) {
                        gameThread.interrupt();
                    } else {
                        gameThread.start();
                    }
                    break;
                    /*
                case C:
                    ObservableList<javafx.scene.Node> children = ((Group) scene.getRoot()).getChildren();
                    if (children.contains(populationsScreen(scene))) {
                        children.remove(populationsScreen(scene));
                    } else {
                        children.add(populationsScreen(scene));
                    }
                    */
            }
            //render(timeLine);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.intValue();
            //render(timeLine);
        });
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = newValue.intValue();
            //render(timeLine);
        });

        //primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
/*
        final Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    render(timeLine);
                });
            }
        }, 0, INTERVAL);
*/
       // primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
       //     @Override
       //     public void handle(WindowEvent event) {
       //         gameThread.interrupt();
       //         timer.cancel();
       //     }
       // });
    }

    private void populateRenderCells(GridPane squares) {
        Point screenCenter = new Point((int) width / (2 * (sideSize + SPACING)),
                (int) height / (2 * (sideSize + SPACING)));

        for (int y = 0; y < height / (sideSize + SPACING); y++) {
            for (int x = 0; x < width / (sideSize + SPACING); x++) {
                squares.add(new Rectangle(sideSize, sideSize), x, y);
            }
        }
    }
/*
    // EFFECTS: render observed scene on the screen
    private void render(TimeLine timeLine, GridPane squares) {
        for (javafx.scene.Node square : squares.getChildren()) {

            Rectangle square = pointRectangle.getValue();

            if (timeLine.isAlive(point)) {
                square.setFill(Color.WHITE);
            } else {
                square.setFill(Color.BLACK);
            }

        }
    }
    */
}
