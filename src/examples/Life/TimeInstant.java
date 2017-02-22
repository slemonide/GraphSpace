package examples.Life;

import model.space.Direction;
import model.space.Node;
import model.space.Point;

import java.util.*;

import static examples.Life.State.ALIVE;
import static examples.Life.State.DEAD;

/*
 * Represents a game of life
 * TODO: should be a static class
 */
public class TimeInstant {
    private Node field; // TODO: make this static
    private Set<Node> aliveCells;

    // Caching
    private Map<Point, Node> cachedNodes;
    private TimeInstant next;

    // EFFECTS: create a game of life on the given field;
    // set generation to 0
    public TimeInstant(Node field, Set<Node> currentGeneration) {
        this.field = field;
        aliveCells = currentGeneration;

        cachedNodes = new HashMap<>();
        cachedNodes.put(new Point(0, 0), field);// TODO: replace with constant
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

    // EFFECTS: get the node state at the given position relative to the observer
    public examples.Life.State readState(Point point) {
        Node requestedNode;

        if (cachedNodes.containsKey(point)) {
            requestedNode = cachedNodes.get(point);
        } else {
            Point closestPoint = getClosestPointTo(cachedNodes, point);
            Point pathFromClosestPointToRequestedPoint = point.minus(closestPoint);

            requestedNode = cachedNodes.get(closestPoint).getNodeAt(pathFromClosestPointToRequestedPoint);
            cachedNodes.put(point, requestedNode);
        }

        if (aliveCells.contains(requestedNode)) {
            return ALIVE;
        } else {
            return DEAD;
        }
    }

    private Point getClosestPointTo(Map<Point, Node> cachedNodes, Point point) {
        Point closestPointSoFar = new Point(0, 0);

        // from: http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap#1066607
        for (Point newPoint : cachedNodes.keySet()) {
            if (newPoint.taxicabDistance(point) < closestPointSoFar.taxicabDistance(point)) {
                closestPointSoFar = newPoint;
            }
        }

        return closestPointSoFar;
    }

    // MODIFIES: this
    // EFFECTS: moves the observer in the specified direction
    public void move(Direction direction) {
        field = field.getNode(direction);

        // from: http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap#1066607
        Map<Point, Node> newCachedNodes = new HashMap<>();
        for (Map.Entry<Point, Node> entry : cachedNodes.entrySet()) {
            Point point = entry.getKey();
            Node node = entry.getValue();

            newCachedNodes.put(point, node.getNode(direction));
        }

        cachedNodes = newCachedNodes;
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

    // EFFECTS: produce the next time instance, if no such instance available, produce null
    protected TimeInstant getFuture() {
        return next;
    }

    // EFFECTS: produce the previous time instant
    // TODO: finish
    protected TimeInstant getPast() {
        return null; // stub
    }

    public void setNext(TimeInstant next) {
        this.next = next;
    }
}
