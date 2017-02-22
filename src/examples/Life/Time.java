package examples.Life;

import model.space.Direction;
import model.space.Node;

import java.util.HashSet;
import java.util.Set;

/*
 * Represents time
 * Allows the timeInstant to run asynchronously from the render method
 */
public class Time implements Runnable {
    private Node field;
    private TimeInstant currentTime;
    private TimeInstant lastTime;
    private int currentGenerationNumber;
    private int actualGenerationNumber;

    // EFFECTS: create a Time instance with the given starting timeInstant
    public Time(TimeInstant initialTimeInstant) {
        currentGenerationNumber = 0;
        actualGenerationNumber = 0;
        currentTime = initialTimeInstant;
        lastTime = currentTime;
        field = initialTimeInstant.getField();
    }

    // EFFECTS: produces the current state of the timeInstant in the observer's timeline
    public TimeInstant getCurrentTimeInstant() {
        return currentTime;
    }

    // MODIFIES: this
    // EFFECTS: change current timeInstant to the next timeInstant (like TimeInstant.tick()) and produce the timeInstant
    public TimeInstant forward() {
        tick();
        currentGenerationNumber++;
        currentTime = currentTime.getFuture();
        return currentTime;
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
        return actualGenerationNumber;
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
        //long startTime = System.nanoTime();

        Set<Node> nextGeneration = new HashSet<>();
        Set<Node> currentGeneration = lastTime.getAliveCells();

        for (Node cell : currentGeneration) {
            if (survives(cell)) {
                nextGeneration.add(cell);
            }

            nextGeneration.addAll(produceOffspring(cell));
        }

        currentTime.setNext(new TimeInstant(field, nextGeneration));
        lastTime.setNext(new TimeInstant(field, nextGeneration));
        actualGenerationNumber++;

        // TODO: remove this
        //long endTime = System.nanoTime();

        //long duration = (endTime - startTime) / 1000000;  //divide by 1000000 to get milliseconds.

        //System.out.println("tick() time: " + duration + " ms");
        //System.out.println("# of alive nodes: " + nextGeneration.size());
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
        Set<Node> currentGeneration = currentTime.getAliveCells();

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
}
