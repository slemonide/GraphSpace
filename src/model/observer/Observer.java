package model.observer;

import model.space.Direction;
import model.space.Node;

/**
 * an immaterial observer inside the graph space
 *
 * INVARIANT: observer always has an assigned node
 */
public class Observer {
    protected Node<?> observedNode;

    // EFFECTS: create an observer with assigned node
    public Observer(Node<?> node) {
        observedNode = node;
    }

    // EFFECTS: assigns a given node to the observer
    public void teleport(Node<?> node) {
        observedNode = node;
    }

    // EFFECTS: move observer in the given direction
    public void move(Direction direction) {
        observedNode = observedNode.getNode(direction);
    }

    // EFFECTS: produce the node associated with the observer
    public Node<?> getNode() {
        return observedNode;
    }
}
