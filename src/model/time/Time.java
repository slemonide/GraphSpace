package model.time;

import model.world.World;

import java.util.Map;

/*
 * Implements the concept of time
 * TimeLine is a graph; time travel creates new branches;
 * these may converge in the future
 */
public class Time {
    private Map<TimeVault, TimeVault> timeGraph;

    private class TimeVault {

        // EFFECTS: constructs a time vault with the given world and the number of ticks
        public TimeVault(World world, int ticks) {

        }
    }

    // EFFECTS: constructs a time line with the given initial world
    public Time(World initialWorld) {
    }

    // EFFECTS: constructs a time line with the given initial time line
    // Useful for time travelling
    public Time(Time initialTime) {
    }

    // EFFECTS: creates a new world, preserving the old one
    public void tick() {}

    // EFFECTS: returns the number of ticks since the creation of time
    public int getTime() {
        return 0; // stub
    }

    // REQUIRES: getTime() > 0
    // EFFECTS: rollbacks to the previous world
    public Time tickBack() {
        return null; // stub
    }

    // EFFECTS: produces the world at this time
    public World getWorld() {
        return null; // stub
    }

    // REQUIRES: getTime() + ticks > 0
    // EFFECTS: produces the world at given ticks in the future if ticks > 0,
    // or at given ticks in the past if ticks < 0
    public World getWorldAtRelative(int ticks) {
        return null; // stub
    }

    // REQUIRES: ticks > 0
    // EFFECTS: produces the world at given ticks from the start of time
    public World getWorldAtAbsolute(int ticks) {
        return null; // stub
    }
}
