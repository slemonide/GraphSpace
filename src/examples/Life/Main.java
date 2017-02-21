package examples.Life;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.space.Point;

import java.util.Timer;
import java.util.TimerTask;

import static examples.Life.State.ALIVE;
import static model.space.Direction.*;

public class Main extends Application {
    private static final int INTERVAL = 500;
    private int SIDE_SIZE = 16;
    private double height = 100;
    private double width = 100;
    private int fieldHeight = 100;
    private int fieldWidth = 100;
    private double cellDensity = 0.25;
    private Game game;
    private Thread gameThread;

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
        game = new Game(fieldWidth, fieldHeight);
        game.populate(cellDensity);
        gameThread = new Thread(game);

        Group root = new Group();
        final Scene scene = new Scene(root, Color.BLACK);
        final Group squares = new Group();
        root.getChildren().add(squares);

        // render the initial screen
        render(squares);


        // code from: http://stackoverflow.com/questions/16764549/timers-and-javafx#18654916
        Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        render(squares);
                    }
                });
            }
        }, 1, INTERVAL);
        // up to here
        gameThread.start();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                switch (ke.getCode()) {
                    case R:
                        game.purge();
                        game.populate(cellDensity);
                        break;
                    case UP:
                        game.move(LEFT);
                        break;
                    case DOWN:
                        game.move(DOWN);
                        break;
                    case LEFT:
                        game.move(LEFT);
                        break;
                    case RIGHT:
                        game.move(RIGHT);
                        break;
                    case SPACE:
                        game.tick();
                        break;
                    case F:
                        primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        break;
                    case F11:
                        primaryStage.setFullScreen(!primaryStage.isFullScreen());
                        break;
                    case ESCAPE:
                        mainScreen(primaryStage);
                    case Q:
                        mainScreen(primaryStage);
                    case P:
                        //if (!gameThread.isInterrupted()) {
                        //    gameThread.interrupt();
                        //    System.out.println("Interrupted thread!");
                        //} else {
                        //gameThread.start();
                        //System.out.println("Started thread!");
                        //}
                }
                render(squares);
            }
        });

        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                height = newValue.intValue();
                render(squares);
            }
        });
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                width = newValue.intValue();
                render(squares);
            }
        });

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);


        class Render implements Runnable {
            @Override
            public void run() {

            }
        }
    }

    // EFFECTS: render observed scene on the screen
    private void render(Group squares) {
        int SPACING = 1;

        for (int y = 0; y < height / (SIDE_SIZE + SPACING); y++) {
            for (int x = 0; x < width / (SIDE_SIZE + SPACING); x++) {
                State selectedNodeState = game.readState(new Point(x, y));
                String color;
                if (selectedNodeState == ALIVE) {
                    color = "white";
                } else {
                    color = "black";
                }
                Rectangle square = new Rectangle(SIDE_SIZE, SIDE_SIZE, Color.web(color));
                square.setX(x * (SIDE_SIZE + SPACING));
                square.setY(y * (SIDE_SIZE + SPACING));

                squares.getChildren().add(square);
            }
        }
    }
}
