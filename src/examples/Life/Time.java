package examples.Life;

import model.space.Direction;
import model.space.Node;
import model.space.Point;

import java.util.*;

import static examples.Life.State.ALIVE;
import static examples.Life.State.DEAD;

/*
 * Represents time
 * Allows the timeInstant to run asynchronously from the render method
 */
public class Time implements Runnable {
    private Node field;
    private List<TimeInstant> timeInstants;
    private int currentGenerationNumber;
    // Caching
    private Map<Point, Node> cachedNodes;

    // EFFECTS: create a Time instance with the given starting timeInstant
    public Time(TimeInstant initialTimeInstant) {
        currentGenerationNumber = 0;
        timeInstants = new ArrayList<>();
        timeInstants.add(initialTimeInstant);
        field = initialTimeInstant.getField();

        cachedNodes = new HashMap<>();
        cachedNodes.put(new Point(0, 0), field);// TODO: replace with constant
    }

    // EFFECTS: produces the current state of the timeInstant in the observer's timeline
    public TimeInstant getCurrentTimeInstant() {
        return getCurrentTime();
    }

    // MODIFIES: this
    // EFFECTS: change current timeInstant to the next timeInstant (like TimeInstant.tick()) and produce the timeInstant
    public TimeInstant forward() {
        if (currentGenerationNumber == timeInstants.size() - 1) {
            tick();
        }

        currentGenerationNumber++;
        return timeInstants.get(currentGenerationNumber);
    }

    // MODIFIES: this
    // EFFECTS: change current timeInstant to the previous timeInstant and return it
    // TODO: test & implement
    public TimeInstant backward() {
        return null; // stub
    }

    // EFFECTS: produce the observed generation number
    public int getGeneration() {
        return currentGenerationNumber;
    }

    // EFFECTS: produce the actual generation of the timeInstant, not the observed one
    public int getActualGeneration() {
        return timeInstants.size() - 1;
    }

    // MODIFIES: this
    // EFFECTS: runs tick in an infinite while loop until finish() is called
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            tick();
        }
    }

    // MODIFIES: this
    // EFFECTS: tick forward one generation
    private void tick() {
        long startTime = System.nanoTime();

        Set<Node> nextGeneration = new HashSet<>();
        Set<Node> currentGeneration =  timeInstants.get(timeInstants.size() - 1).getAliveCells();

        for (Node cell : currentGeneration) {
            if (survives(cell)) {
                nextGeneration.add(cell);
            }

            nextGeneration.addAll(produceOffspring(cell));
        }

        timeInstants.add(new TimeInstant(field, nextGeneration));

        // TODO: remove this
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        System.out.println("tick() time: " + duration + " ms");
        System.out.println("# of alive nodes: " + nextGeneration.size());
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

    // TODO: add aliveCells parameter
    private int numberOfNeighbours(Node cell) {
        int count = 0;
        Set<Node> visitedDirections = new HashSet<>();
        Set<Node> currentGeneration = getCurrentTime().getAliveCells();

        // TODO: add tests for 1x1 and 100x1 and 1x100 fields
        for (Direction direction : Direction.values()) {
            if (currentGeneration.contains(cell.getNode(direction))
                    && !visitedDirections.contains(cell.getNode(direction))) {
                count++;
                visitedDirections.add(cell.getNode(direction));
            }

            for (Direction normalDirection : direction.normal()) {
                if (currentGeneration.contains(cell.getNode(direction).getNode(normalDirection))
                        && !visitedDirections.contains(cell.getNode(direction).getNode(normalDirection))) {
                    count++;
                    visitedDirections.add(cell.getNode(direction).getNode(normalDirection));
                }
            }
        }

        return count;
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

        if (getCurrentTime().getAliveCells().contains(requestedNode)) {
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

    public TimeInstant getCurrentTime() {
        return timeInstants.get(timeInstants.size() - 1);
    }
}
