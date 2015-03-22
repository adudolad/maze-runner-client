package de.zalando.adudoladov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by adudoladov on 20/03/15.
 */
public class PathFinderTest {
    @Test
    public void availableMazesTest() {
        List<HashMap> mazes = new ArrayList<>();
        HashMap maze1 = new HashMap<>();
        maze1.put("code", "maze-1");
        maze1.put("height", "10");
        maze1.put("width", "10");
        mazes.add(maze1);

        HashMap maze2 = new HashMap<>();
        maze2.put("code", "maze990 992");
        maze2.put("height", "1000");
        maze2.put("width", "1000");
        mazes.add(maze2);

        PathFinder pathFinder = PathFinder.getPathFinder();
        pathFinder.setAvailableMazes(mazes);
        Assert.assertTrue(pathFinder.isMazeAvailableOnServer("maze-1"));
        Assert.assertTrue(pathFinder.isMazeAvailableOnServer("maze990 992"));
    }

    @Test
    public void availableMazesDoesNotMatchNameTest() {
        List<HashMap> mazes = new ArrayList<>();
        HashMap maze1 = new HashMap<>();
        maze1.put("code", "maze-1");
        maze1.put("height", "10");
        maze1.put("width", "10");
        mazes.add(maze1);

        PathFinder pathFinder = PathFinder.getPathFinder();
        pathFinder.setAvailableMazes(mazes);
        Assert.assertFalse(pathFinder.isMazeAvailableOnServer("maze-2"));
    }

    @Test
    public void notAvailableMazesTest() {
        List<HashMap> mazes = new ArrayList<>();

        PathFinder pathFinder = PathFinder.getPathFinder();
        pathFinder.setAvailableMazes(mazes);
        Assert.assertFalse(pathFinder.isMazeAvailableOnServer("maze-1"));
    }
}
