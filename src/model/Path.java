package model;

import java.util.Collection;
import java.util.Iterator;
import java.util.Queue;

/*
 * A sequence of steps that lead from one node in the graph to another
 */
public class Path implements Queue<Direction> {

    // EFFECTS: Constructs an empty path
    public Path() {

    }

    // EFFECTS: Constructs a path with directions provided
    public Path(Direction... directions) {
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Direction> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Direction direction) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Direction> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean offer(Direction direction) {
        return false;
    }

    @Override
    public Direction remove() {
        return null;
    }

    @Override
    public Direction poll() {
        return null;
    }

    @Override
    public Direction element() {
        return null;
    }

    @Override
    public Direction peek() {
        return null;
    }
}
