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

    public void setDirection(final String direction) {
        this.direction = direction;
    }

    public Point getFrom() {
        return from;
    }

    public void setFrom(final Point from) {
        this.from = from;
    }

    @Override
    public String toString() {
        return " position: y = " + from.getY() + ", x = " + from.getX();
    }
}
