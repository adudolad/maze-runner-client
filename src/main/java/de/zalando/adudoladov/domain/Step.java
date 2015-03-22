package de.zalando.adudoladov.domain;

/**
 * Created by adudoladov on 20/03/15.
 */
public class Step {
    private String direction;
    private Point from;

    public Step(final Point fromPoint, final String direction) {
        this.from = fromPoint;
        this.direction = direction;
    }

    public String getDirection() {
        return direction;
    }

    public Point getFrom() {
        return from;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" position: y = ");
        buffer.append(from.getY());
        buffer.append(", x = ");
        buffer.append(from.getX());
        return buffer.toString();
    }
}
