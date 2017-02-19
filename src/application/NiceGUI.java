package application;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class NiceGUI extends Application {
    private int SIDE_SIZE = 16;
    private int HEIGHT = SIDE_SIZE * 100;
    private int WIDTH = SIDE_SIZE * 100;
    private int SPACING = 1;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);
        primaryStage.setScene(scene);

        Group squares = new Group();
        for (int y = 0; y < HEIGHT; y += SIDE_SIZE + SPACING) {
            for (int x = 0; x < WIDTH; x += SIDE_SIZE + SPACING) {
                Rectangle square = new Rectangle(SIDE_SIZE, SIDE_SIZE, Color.web("brown"));
                square.setX(x);
                square.setY(y);

                squares.getChildren().add(square);
            }
        }
        root.getChildren().add(squares);

        primaryStage.show();
    }
}
