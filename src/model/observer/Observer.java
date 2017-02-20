package model.observer;

import model.space.Direction;
import model.space.Node;

import java.util.Set;

/**
 * an immaterial observer inside the graph space
 *
 * INVARIANT: observer always has an assigned node
 */
public class Observer<E> {
    protected Node<Set<E>> observedNode;

    // EFFECTS: create an observer with assigned node
    public Observer(Node<Set<E>> node) {
        observedNode = node;
    }

    // EFFECTS: assigns a given node to the observer
    public void teleport(Node<Set<E>> node) {
        observedNode = node;
    }

    // EFFECTS: move observer in the given direction
    public void move(Direction direction) {
        observedNode = observedNode.getNode(direction);
    }

    // EFFECTS: produce the node associated with the observer
    public Node<Set<E>> getNode() {
        return observedNode;
    }
}
