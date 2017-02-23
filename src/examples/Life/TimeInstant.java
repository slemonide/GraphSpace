package examples.Life;

import model.space.Direction;
import model.space.Node;
import model.space.Point;

import java.util.*;

/*
 * Represents a game of life
 * TODO: should be a static class
 */
public class TimeInstant {
    private Node field; // TODO: make this static
    private Set<Node> aliveCells;

    private TimeInstant next;

    // EFFECTS: create a game of life on the given field;
    // set generation to 0
    public TimeInstant(Node field, Set<Node> currentGeneration) {
        this.field = field;
        aliveCells = currentGeneration;
    }

    // EFFECTS: produce the total number of alive cells
    public int getPopulation() {
        return aliveCells.size();
    }

    // MODIFIES: this
    // EFFECTS: revive the node at the given position relative to the observer
    public void revive(Point point) {
        aliveCells.add(field.getNodeAt(point));
    }

    // MODIFIES: this
    // EFFECTS: kill the node at the given position relative to the observer
    public void kill(Point point) {
        aliveCells.remove(field.getNodeAt(point));
    }

    // MODIFIES: this
    // EFFECTS: completely clean the field
    public void purge() {
        aliveCells = new HashSet<>();
    }

    // TODO: make more robust
    // REQUIRES: 0 <= population() / field.size() <= 1
    // MODIFIES: this
    // EFFECTS: populate the field with the given cell density (total number of cells / total area)
    public void populate(double density) {
        int numberOfCellsToAdd = (int) (density * field.width() * field.height());

        List<Node> allNodes = new ArrayList<>();
        allNodes.addAll(getAllNodes(field));
        allNodes.removeAll(aliveCells);
        Collections.shuffle(allNodes);

        for (int i = 0; i < numberOfCellsToAdd; i++) {
            aliveCells.add(allNodes.get(i));
        }
    }

    private Set<Node> getAllNodes(Node node) {
        Queue<Node> todo = new LinkedList<>();
        Set<Node> visited = new HashSet<>();

        todo.add(field);

        while (!todo.isEmpty()) {
            Node nextNode = todo.remove();
            if (!visited.contains(nextNode)) {
                visited.add(nextNode);

                for (Direction dir : Direction.values()) {
                    todo.add(nextNode.getNode(dir));
                }
            }
        }

        return visited;
    }

    // EFFECTS: return the set of alive cells
    public Set<Node> getAliveCells() {
        return aliveCells;
    }

    public Node getField() {
        return field;
    }
}
