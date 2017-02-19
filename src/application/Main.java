package application;

import model.Direction;
import model.Node;

import java.util.Scanner;

class Main {
    private static int height = 10;
    private static int width = 10;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Node<String> node = new Node<>(width, height);

        while (true) {
            String input = scanner.nextLine();

            switch (input) {
                case "w":
                    node = node.getNode(Direction.UP);
                    break;
                case "a":
                    node = node.getNode(Direction.LEFT);
                    break;
                case "s":
                    node = node.getNode(Direction.DOWN);
                    break;
                case "d":
                    node = node.getNode(Direction.RIGHT);
                    break;
                case "r":
                    System.out.print(render(node));
                    break;
                case "q":
                    return;
                default:
                    node.place(input);
            }

            System.out.println(node.readObject());
        }
    }

    private static String render(Node node) {
        String stringSoFar = "";

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (node.readObject() == null) {
                    stringSoFar = stringSoFar + " ";
                } else {
                    stringSoFar = stringSoFar + node.readObject();
                }
                node = node.getNode(Direction.RIGHT);
            }
            stringSoFar = stringSoFar + "\n";
            node = node.getNode(Direction.DOWN);
        }

        return stringSoFar;
    }
}
