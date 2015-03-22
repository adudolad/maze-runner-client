package de.zalando.adudoladov.domain;

/**
 * Created by adudoladov on 20/03/15.
 */
public class Maze {
    private int height;
    private int weight;
    private String code;

    public Maze(final String code, final int height, final int width) {
        this.code = code;
        this.height = height;
        this.weight = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getCode() {
        return code;
    }

    public String toString() {
        return "de.zalando.adudoladov.domain.Maze: " + code + ", height " + height + ", weight" + weight;
    }
}
