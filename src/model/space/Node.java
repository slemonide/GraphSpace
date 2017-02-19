package model.space;

import java.util.*;

/**
 * A node in a planar graph
 */
public class Node<E> {
    private Map<Direction, Node<E>> children = new HashMap<>();
    private E contents;

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
                this.glueBottom(new Node<E>(width));
            }
        }
    }

    // REQUIRES: width >= 1
    // EFFECTS: constructs an empty graph stripe of given width that
    // is looped onto itself
    public Node(int width) {
        this();

        if (width != 1) {
            Node<E> traveller = this;

            for (int x = 1; x < width; x++) {
                traveller.connect(new Node<E>(), Direction.RIGHT);
                traveller = traveller.getNode(Direction.RIGHT);
            }

            traveller.connect(this, Direction.RIGHT);
        }
    }

    // MODIFIES: this
    // EFFECTS: gets the node at the specified location
    public Node<E> getNode(Direction dir) {
        return children.get(dir);
    }

    // MODIFIES: this
    // EFFECTS: places obj in this node
    public void place(E obj) {
        contents = obj;
    }

    // EFFECTS: returns object stored at this node
    public E readObject() {
        return contents;
    }

    // MODIFIES: this, node
    // EFFECTS: connects given node to this node in the given direction
    // relative to this node
    public void connect(Node<E> node, Direction dir) {
        if (children.get(dir) != node) {
            children.put(dir, node);
            node.connect(this, dir.opposite());
        }
    }

    // REQUIRES: space is closed
    // MODIFIES: this, bottom
    // EFFECTS: inserts bottom graph under this graph, making sure that the
    // bottom graph connects to this graph and its old bottom graph
    public void glueBottom(Node<E> bottom) {
        Node<E> topTraveller = this;
        Node<E> lostChild = this.getNode(Direction.DOWN);
        Node<E> bottomTraveller = bottom;

        do {
            topTraveller.connect(bottomTraveller, Direction.DOWN);

            topTraveller = topTraveller.getNode(Direction.RIGHT);
            bottomTraveller = bottomTraveller.getNode(Direction.RIGHT);
        } while (topTraveller != this && bottomTraveller != bottom);

        bottomTraveller = bottom;
        Node<E> originalBottomTraveller = bottomTraveller;
        Node<E> originalLostChild = lostChild;

        while (bottomTraveller.getNode(Direction.DOWN) != originalBottomTraveller) {
            bottomTraveller = bottomTraveller.getNode(Direction.DOWN);
        }

        do {
            lostChild.connect(bottomTraveller, Direction.UP);

            lostChild = lostChild.getNode(Direction.RIGHT);
            bottomTraveller = bottomTraveller.getNode(Direction.RIGHT);
        } while (lostChild != originalLostChild && bottomTraveller != originalBottomTraveller);
    }

    // EFFECTS: produce the number of nodes in the graph
    public int size() {
        Queue<Node<E>> todo = new LinkedList<>();
        Set<Node<E>> visited = new HashSet<>();

        todo.add(this);

        while (!todo.isEmpty()) {
            Node<E> nextNode = todo.remove();
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

        Node<E> traveller = this;

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

        Node<E> traveller = this;

        do {
            traveller = traveller.getNode(Direction.DOWN);
            visited.add(traveller);
        } while (traveller != this);

        return visited.size();
    }

    // EFFECTS: produce the node at the given path relative to this node
    public Node<E> getNodeAt(Path path) {
        Node<E> traveller = this;

        for (Direction direction : path) {
            traveller = traveller.getNode(direction);
        }

        return traveller;
    }

    // EFFECTS: produce the node at the given location relative the this node
    // with path that intersects with the given vector the most
    // If more than one node is possible, take the node closest to the horizontal
    public Node<E> getNodeAt(Point point) {
        return getNodeAt(new Path(point));
    }
}
