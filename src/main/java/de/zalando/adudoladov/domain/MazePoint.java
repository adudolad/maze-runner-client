package de.zalando.adudoladov.domain;

/**
 * Created by adudoladov on 21/03/15.
 */
public class MazePoint extends Point {
    private char code;

    public MazePoint(final int y, final int x, final char code) {
        super(y, x);
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public Point getPoint() {
        return new Point(super.getY(), super.getX());
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" x = ");
        buffer.append(super.getX());
        buffer.append(", y = ");
        buffer.append(super.getY());
        buffer.append("; code = ");
        buffer.append(code);
        return buffer.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof Point)) {
            return false;
        }

        return (((MazePoint) o).getY() == super.getY())
                && (((MazePoint) o).getX() == super.getX() && ((MazePoint) o).getCode() == code);
    }

    @Override
    public int hashCode() {
        return ((super.getY() + AbstractMaze.SEED) * (super.getX() + code)) * AbstractMaze.SEED;
    }
}
