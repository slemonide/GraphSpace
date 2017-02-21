package examples.Life;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.space.Point;

import static examples.Life.State.ALIVE;
import static model.space.Direction.*;

public class Main extends Application {
    private int SIDE_SIZE = 16;
    private int height = SIDE_SIZE * 40;
    private int width = SIDE_SIZE * 40;
    private Game game;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        game = new Game(30, 30);

        Group root = new Group();
        final Scene scene = new Scene(root, Color.BLACK);
        primaryStage.setScene(scene);
        final Group squares = new Group();
        root.getChildren().add(squares);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().getName().equals("R")) {
                    game.purge();
                    game.populate(0.25);
                } else if (ke.getCode().isArrowKey()) {
                    switch (ke.getCode()) {
                        case LEFT:
                            game.move(LEFT);
                            break;
                        case UP:
                            game.move(UP);
                            break;
                        case RIGHT:
                            game.move(RIGHT);
                            break;
                        case DOWN:
                            game.move(DOWN);
                    }
                } else if (ke.getCode().getName().equals("Space")) {
                    game.tick();
                } else if (ke.getCode().getName().equals("F")) {
                    primaryStage.setFullScreen(!primaryStage.isFullScreen());
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

        render(squares);

        primaryStage.show();
    }

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
