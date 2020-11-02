package de.zalando.adudoladov;

import org.junit.Assert;
import org.junit.Test;

import de.zalando.adudoladov.domain.MazePoint;

/**
 * Created by adudoladov on 22/03/15.
 */
public class MazePointTest {
    @Test
    public void equalsTest() {
        MazePoint point1 = new MazePoint(5, 7, '.');
        MazePoint point2 = new MazePoint(5, 7, '.');
        MazePoint point3 = new MazePoint(5, 7, 'x');
        MazePoint point4 = new MazePoint(4, 7, '.');
        MazePoint point5 = new MazePoint(5, 8, '.');

        Assert.assertTrue(point1.equals(point2));
        Assert.assertFalse(point1.equals(point3));
        Assert.assertFalse(point1.equals(point4));
        Assert.assertFalse(point1.equals(point5));
    }

    @Test
    public void hasCode() {
        MazePoint point1 = new MazePoint(5, 7, '.');
        MazePoint point2 = new MazePoint(5, 7, '.');
        MazePoint point3 = new MazePoint(5, 7, 'x');
        MazePoint point4 = new MazePoint(4, 7, '.');
        MazePoint point5 = new MazePoint(5, 8, '.');

        Assert.assertEquals(point2.hashCode(), point1.hashCode());
        Assert.assertNotEquals(point3.hashCode(), point1.hashCode());
        Assert.assertNotEquals(point4.hashCode(), point1.hashCode());
        Assert.assertNotEquals(point5.hashCode(), point1.hashCode());
    }

    @Test
    public void toStringTest() {
        MazePoint point = new MazePoint(5, 7, '.');
        Assert.assertEquals(" x = 7, y = 5; code = .", point.toString());
    }
}
