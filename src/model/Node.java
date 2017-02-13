package model;

import java.awt.*;
import java.util.*;

/**
 * A node in a plane graph
 */
public class Node {
    private Map<Direction, Node> children = new HashMap<>();
    private Object contents;

    // EFFECTS: constructs a single empty node looped onto itself
    // set contents to null
    public Node() {
        for (Direction direction : Direction.values()) {
            children.put(direction, this);
        }
        contents = null;
    }

    // REQUIRES: width, height >= 1
    // EFFECTS: constructs an empty rectangular graph space looped onto
    // itself on the borders; this node is located at the top left of the graph
    public Node(int width, int height) {
        this(width);

        if (height != 1) {
            for (int x = 1; x < height; x++) {
                this.glueBottom(new Node(width));
            }
        }
    }

    // REQUIRES: width >= 1
    // EFFECTS: constructs an empty graph stripe of given width that
    // is looped onto itself
    public Node(int width) {
        this();

        if (width != 1) {
            Node traveller = this;

            for (int x = 1; x < width; x++) {
                traveller.connect(new Node(), Direction.RIGHT);
                traveller = traveller.getNode(Direction.RIGHT);
            }

            traveller.connect(this, Direction.RIGHT);
        }
    }

    // MODIFIES: this
    // EFFECTS: gets the node at the specified location
    public Node getNode(Direction dir) {
        return children.get(dir);
    }

    // MODIFIES: this
    // EFFECTS: places obj in this node
    public void place(Object obj) {
        contents = obj;
    }

    // EFFECTS: returns object stored at this node
    public Object readObject() {
        return contents;
    }

    // MODIFIES: this, node
    // EFFECTS: connects given node to this node in the given direction
    // relative to this node
    public void connect(Node node, Direction dir) {
        if (children.get(dir) != node) {
            children.put(dir, node);
            node.connect(this, dir.opposite());
        }
    }

    // REQUIRES: space is closed
    // MODIFIES: this, bottom
    // EFFECTS: inserts bottom graph under this graph, making sure that the
    // bottom graph connects to this graph and its old bottom graph
    public void glueBottom(Node bottom) {
        Node topTraveller = this;
        Node bottomTraveller = bottom;

        do {
            topTraveller.connect(bottomTraveller, Direction.DOWN);

            topTraveller = topTraveller.getNode(Direction.RIGHT);
            bottomTraveller = bottomTraveller.getNode(Direction.RIGHT);
        } while (topTraveller != this && bottomTraveller != bottom);

        Node originalTopTraveller = topTraveller;
        Node originalBottomTraveller = bottomTraveller;

        while (topTraveller.getNode(Direction.UP) != originalTopTraveller) {
            topTraveller = topTraveller.getNode(Direction.UP);
        }

        while (bottomTraveller.getNode(Direction.DOWN) != originalBottomTraveller) {
            bottomTraveller = bottomTraveller.getNode(Direction.DOWN);
        }

        do {
            topTraveller.connect(bottomTraveller, Direction.UP);

            topTraveller = topTraveller.getNode(Direction.RIGHT);
            bottomTraveller = bottomTraveller.getNode(Direction.RIGHT);
        } while (topTraveller != originalTopTraveller && bottomTraveller != originalBottomTraveller);
    }

    // EFFECTS: produce the number of nodes in the graph
    public int size() {
        Queue<Node> todo = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        todo.add(this);

        while (!todo.isEmpty()) {
            Node nextNode = todo.remove();
            if (!visited.contains(nextNode)) {
                visited.add(nextNode);

                for (Direction dir : Direction.values()) {
                    todo.add(nextNode.getNode(dir));
                }
            }
        }

        return visited.size();
    }

    // REQUIRES: the horizontal stripe is closed
    // EFFECTS: produce the number of nodes to be traversed in the
    // horizontal direction before coming to this node
    public int width() {
        Set<Node> visited = new HashSet<>();

        Node traveller = this;

        do {
            traveller = traveller.getNode(Direction.RIGHT);
            visited.add(traveller);
        } while (traveller != this);

        return visited.size();
    }

    // REQUIRES: the vertical stripe is closed
    // EFFECTS: produce the number of nodes to be traversed in the
    // vertical direction before coming to this node
    public int height() {
        Set<Node> visited = new HashSet<>();

        Node traveller = this;

        do {
            traveller = traveller.getNode(Direction.DOWN);
            visited.add(traveller);
        } while (traveller != this);

        return visited.size();
    }

    // REQUIRES: given node is connected to this node
    // EFFECTS: produce the shortest radius vector pointing from this node
    // to the given node
    public Point distance() {
        return null; //stub
    }

    // EFFECTS: produce the node at the given location relative the this node
    // with path that intersects with the given vector the most
    public Node getNodeAt(Point point) {
        return null; //stub
    }
}
