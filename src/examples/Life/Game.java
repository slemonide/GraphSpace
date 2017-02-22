package examples.Life;

import model.space.Direction;
import model.space.Node;
import model.space.Point;

import java.util.*;

import static examples.Life.State.ALIVE;
import static examples.Life.State.DEAD;

/*
 * Represents a game of life
 */
public class Game implements Runnable {
    private Node field; // TODO: make this static
    private Set<Node> aliveCells;
    private int generation;

    // Caching
    private Map<Point, Node> cachedNodes;

    // EFFECTS: create a game of life with an empty field of given size;
    // set generation to 0
    public Game(int width, int height) {
        field = new Node(width, height);
        aliveCells = new HashSet<>();

        cachedNodes = new HashMap<>();
        cachedNodes.put(new Point(0, 0), field);// TODO: replace with constant
    }

    // MODIFIES: this
    // EFFECTS: runs tick in an infinite while loop until finish() is called
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            tick();
        }
    }

    // MODIFIES: this
    // EFFECTS: tick forward one generation
    public void tick() {
        long startTime = System.nanoTime();

        Set<Node> nextGeneration = new HashSet<>();

        for (Node cell : aliveCells) {
            if (survives(cell)) {
                nextGeneration.add(cell);
            }

            nextGeneration.addAll(produceOffspring(cell));
        }

        aliveCells = nextGeneration;

        generation++;


        // TODO: remove this
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        System.out.println("tick() time: " + duration + " ms");
        System.out.println("# of alive nodes: " + aliveCells.size());
    }

    private Set<Node> produceOffspring(Node cell) {
        Set<Node> offspring = new HashSet<>();

        for (Direction direction : Direction.values()) {
            if (breeds(cell.getNode(direction))) {
                offspring.add(cell.getNode(direction));
            }

            for (Direction normalDirection : direction.normal()) {
                if (breeds(cell.getNode(direction).getNode(normalDirection))) {
                    offspring.add(cell.getNode(direction).getNode(normalDirection));
                }
            }
        }

        return offspring;
    }

    private boolean breeds(Node cell) {
        return (numberOfNeighbours(cell) == 3);
    }

    private boolean survives(Node cell) {
        return (numberOfNeighbours(cell) == 2 || numberOfNeighbours(cell) == 3);
    }

    private int numberOfNeighbours(Node cell) {
        int count = 0;
        Set<Node> visitedDirections = new HashSet<>();

        // TODO: add tests for 1x1 and 100x1 and 1x100 fields
        for (Direction direction : Direction.values()) {
            if (aliveCells.contains(cell.getNode(direction))
                    && !visitedDirections.contains(cell.getNode(direction))) {
                count++;
                visitedDirections.add(cell.getNode(direction));
            }

            for (Direction normalDirection : direction.normal()) {
                if (aliveCells.contains(cell.getNode(direction).getNode(normalDirection))
                        && !visitedDirections.contains(cell.getNode(direction).getNode(normalDirection))) {
                    count++;
                    visitedDirections.add(cell.getNode(direction).getNode(normalDirection));
                }
            }
        }

        return count;
    }

    // EFFECTS: return the generation number
    public int getGeneration() {
        return generation;
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
}
