package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Node;
import model.Point;

public class GridGUI extends Application {
    private Node<String> world = new Node<>(100, 100);

    public GridGUI() {
        for (int y = 0; y < world.height(); y++) {
            for (int x = 0; x < world.width(); x++) {
                world.getNodeAt(new Point(x, y)).place(x + "," + y);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Graph Space");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        int HEIGHT = 10;
        for (int y = 0; y < HEIGHT; y++) {
            int WIDTH = 10;
            for (int x = 0; x < WIDTH; x++) {
                final String text = world.getNodeAt(new Point(x - WIDTH /2, y - HEIGHT /2)).readObject();

                Button newButton = new Button(text);
                newButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.out.println(text);
                    }
                });

                grid.add(newButton, x, y);
            }
        }

        Scene scene = new Scene(grid);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
