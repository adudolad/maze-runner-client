package de.zalando.adudoladov.domain;

import de.zalando.adudoladov.AbstractMaze;

/**
 * Created by adudoladov on 20/03/15.
 */
class Point {
    private int y;
    private int x;

    Point(final int y, final int x) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        return (((Point) o).getY() == y) && (((Point) o).getX() == x);
    }

    @Override
    public int hashCode() {
        return (y + x) * AbstractMaze.SEED;
    }

    @Override
    public String toString() {
        return "y: " + y + " x:" + x;
    }
}
