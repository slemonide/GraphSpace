package model.world;

import model.creature.Creature;
import model.space.Node;

import java.util.HashSet;
import java.util.Set;

/**
 * A world is a graph where creatures can live and observers can dwell
 */
public class World extends Node<Set<Creature>> {

    // EFFECTS: constructs an empty world
    public World() {
        super();
        this.place(new HashSet<Creature>());
    }

    // EFFECTS: constructs an empty world of given height and width
    public World(int width, int height) {
        super(width, height);
        this.place(new HashSet<Creature>());
    }

    // EFFECTS: ticks all time lines
    public void tick() {
        // TODO: finish
    }
}
