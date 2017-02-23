package examples.Life;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.space.Node;
import model.space.Point;

import java.util.*;

import static model.space.Direction.*;

public class Main extends Application {
    private static final int INTERVAL = 50;
    private int sideSize = 8; // 16
    private double height = 100;
    private double width = 100;
    private int fieldHeight = 600;
    private int fieldWidth = 600;
    private double cellDensity = 0.10;
    private Time timeLine;
    private Thread gameThread;

    private Point screenCenter;

    // Render stuff
    private Map<Point, Rectangle> renderMap;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Game Of Life");

        mainScreen(primaryStage);

        primaryStage.show();
    }

    private void mainScreen(final Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text scenetitle = new Text("Game Of Life");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0);

        Label heightLabel = new Label("Height:");
        grid.add(heightLabel, 0, 2);

        final TextField heightField = new TextField();
        heightField.setText(String.valueOf(fieldHeight));
        grid.add(heightField, 1, 2);

        Label widthLabel = new Label("Width:");
        grid.add(widthLabel, 0, 3);

        final TextField widthField = new TextField();
        widthField.setText(String.valueOf(fieldWidth));
        grid.add(widthField, 1, 3);

        Label cellDensityLabel = new Label("Cell density:");
        grid.add(cellDensityLabel, 0, 4);

        final Slider cellDensitySlider = new Slider();
        cellDensitySlider.setMin(0);
        cellDensitySlider.setMax(1);
        cellDensitySlider.setValue(cellDensity);
        cellDensitySlider.setShowTickLabels(true);
        cellDensitySlider.setShowTickMarks(true);
        cellDensitySlider.setMajorTickUnit(0.50);
        cellDensitySlider.setMinorTickCount(10);
        grid.add(cellDensitySlider, 1, 4);

        final Label errorLabel = new Label("");
        errorLabel.setMinSize(150, 10);
        grid.add(errorLabel, 0, 6);

        Button start = new Button("Start");
        start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    fieldHeight = Integer.parseInt(heightField.getText());
                    fieldWidth = Integer.parseInt(widthField.getText());
                    cellDensity = cellDensitySlider.getValue();
                    gameScreen(primaryStage);
                } catch (NumberFormatException e) {
                    errorLabel.setText("Please enter an integer.");
                }
            }
        });
        grid.add(start,1,5);

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
    }

    private void gameScreen(final Stage primaryStage) {
        TimeInstant initialTimeInstant = new TimeInstant(new Node(fieldWidth, fieldHeight), new HashSet<Node>());
        initialTimeInstant.populate(cellDensity);
        timeLine = new Time(initialTimeInstant);
        gameThread = new Thread(timeLine);
        gameThread.start();

        Group root = new Group();
        final Scene scene = new Scene(root, Color.BLACK);
        final Group squares = new Group();
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
                    render(renderMap);
                    endTime = System.nanoTime();
                    duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.
                    System.out.println("render() time: " + duration + " ms");
                    break;
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
                case MINUS:
                    sideSize = Math.max(sideSize - 1, 0);
                    populateRenderCells(squares, primaryStage);
                    render(renderMap);
                    break;
                case PLUS:
                    sideSize++;
                    populateRenderCells(squares, primaryStage);
                    render(renderMap);
                case P:
                    if (!gameThread.isInterrupted()) {
                        gameThread.interrupt();
                    } else {
                    gameThread.start();
                    }
            }
            render(renderMap);
        });

        scene.heightProperty().addListener((observable, oldValue, newValue) -> {
            height = newValue.intValue();
            populateRenderCells(squares, primaryStage);
            render(renderMap);
        });
        scene.widthProperty().addListener((observable, oldValue, newValue) -> {
            width = newValue.intValue();
            populateRenderCells(squares, primaryStage);
            render(renderMap);
        });

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);

        populateRenderCells(squares, primaryStage);
        final Timer timer = new java.util.Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    render(renderMap);
                });
            }
        }, 0, INTERVAL);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                gameThread.interrupt();
                timer.cancel();
            }
        });
    }

    private void populateRenderCells(Group squares, Stage primaryScene) {
        int SPACING = 1;

        screenCenter = new Point((int) primaryScene.getWidth() / (2 * (sideSize + SPACING)),
                (int) primaryScene.getHeight() / (2 * (sideSize + SPACING)));
        renderMap = new HashMap<>();

        for (int y = 0; y < height / (sideSize + SPACING); y++) {
            for (int x = 0; x < width / (sideSize + SPACING); x++) {
                Point position = new Point(x, y).minus(screenCenter);
                Rectangle square = new Rectangle(sideSize, sideSize, Color.BLACK);
                square.setX(x * (sideSize + SPACING));
                square.setY(y * (sideSize + SPACING));
                if (!position.isOrigin()) {
                    renderMap.put(position, square);
                } else {
                    square.setFill(Color.ORANGE);
                }
                squares.getChildren().add(square);
            }
        }
    }

    // EFFECTS: render observed scene on the screen
    private void render(Map<Point, Rectangle> squares) {
        for (Map.Entry<Point, Rectangle> pointRectangle : squares.entrySet()) {
            Point point = pointRectangle.getKey();
            Rectangle square = pointRectangle.getValue();

            if (timeLine.isAlive(point)) {
                square.setFill(Color.WHITE);
            } else {
                square.setFill(Color.BLACK);
            }

        }
    }
}
