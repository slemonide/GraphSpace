package model.creature;

import model.observer.Observer;
import model.world.World;

/**
 * An creature inside the graph space.
 * There could be more than one creature per node.
 */
public class Creature extends Observer<Creature> {

    // EFFECTS: creates a creature at the given node in the world
    public Creature(World node) {
        super(node);
        observedNode.readObject().add(this);
    }

    // EFFECTS: destroys the creature & turns it into an observer
    public void kill() {
        observedNode.readObject().remove(this);
    }
}
