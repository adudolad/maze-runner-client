package de.zalando.adudoladov;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.zalando.adudoladov.client.MazeServiceClient;
import de.zalando.adudoladov.client.MazeServiceClientImpl;
import de.zalando.adudoladov.domain.AbstractMaze;
import de.zalando.adudoladov.domain.Directions;
import de.zalando.adudoladov.domain.MazePoint;
import de.zalando.adudoladov.domain.Step;

/**
 * Created by adudoladov on 21/03/15.
 */
public class PathFinder extends AbstractMaze {
    private static MazeServiceClient mazeServiceClient;
    private static PathFinder pathFinder;
    private String requestedMaze;
    private List<HashMap> availableMazes;
    private List<MazePoint> pathToExit;
    private Map<Integer, MazePoint> history;
    private HashMap mazeMap;

    private PathFinder() {
        pathToExit = new ArrayList<>();
        history = new LinkedHashMap<>();
        availableMazes = null;
    }

    public static PathFinder getPathFinder() {
        if (pathFinder == null) {
            pathFinder = new PathFinder();
        }

        return pathFinder;
    }

    public static void main(final String[] args) {

        // init
        getPathFinder();
        pathFinder.pathToExit = new ArrayList<>();

        MazePoint currentPoint = null;

        // Input parameter validation
        // Command line parameters: code (maze code) and url (URL to maze service)
        if (args.length == 2) {
            pathFinder.requestedMaze = args[0];
            mazeServiceClient = new MazeServiceClientImpl(args[1]);
        } else {
            final String message = "Command line parameters: code (maze code) and url (URL to maze service)";
            getLogger().error(message);
            System.out.println(message);
            return;
        }

        // Request all mazes from the server
        pathFinder.getAllMazesFromServer();

        // Search a Maze by its name in the list of available mazes
        if (pathFinder.isMazeAvailableOnServer(pathFinder.requestedMaze)) {

            // If the Maze is found, request its start point
            currentPoint = pathFinder.getMazeStartPoint();

            // If StartPoint is null -> exit. Logging is in getMazeStartPoint()
            if (currentPoint == null) {
                return;
            }
            // Maze is not found by its name
        } else {
            final String message = "Maze \"" + pathFinder.requestedMaze + "\" is not found";
            getLogger().info(message);
            System.err.println(message);
            return;
        }

        // Main loop for seeking a path in the maze
        while (currentPoint.getCode() != 'x' || currentPoint == null) {
            List<MazePoint> availableDirections = pathFinder.getAvailableDirections(currentPoint);
            if (availableDirections.size() > 0) {
                Iterator availableDirectionsItr = availableDirections.iterator();
                while (availableDirectionsItr.hasNext()) {
                    MazePoint nextPoint = (MazePoint) availableDirectionsItr.next();
                    if (pathFinder.history.containsKey(nextPoint.hashCode())) {
                        getLogger().info("Point " + nextPoint.toString() + " is in History list");

                        // Remove point if it's in the History list
                        availableDirectionsItr.remove();

                        // If there is no directions are in the list
                        // that means Dead End and need to step back and seek another way
                        if (availableDirections.size() == 0) {
                            currentPoint = nextPoint;
                            break;
                        }
                    } else {
                        getLogger().info("Do the step FROM: " + currentPoint.toString() + " TO "
                                + nextPoint.toString());

                        // Do the step to next position
                        currentPoint = nextPoint;
                        pathFinder.pathToExit.add(currentPoint);
                        pathFinder.history.put(currentPoint.hashCode(), currentPoint);
                        break;
                    }
                }
            }
        }

        if (currentPoint == null) {
            final String message = "Current Maze point is not defined. Something went wrong, please check logs. Exit.";
            System.err.println(message);
            getLogger().error(message);
        } else {

            // The Path to Exit is found
            // Print, log it and exit
            getLogger().info("The path is found in " + pathFinder.requestedMaze + " : ");
            System.out.println("The path is found in " + pathFinder.requestedMaze + " : ");

            int steps = 1;
            for (MazePoint step : pathFinder.pathToExit) {
                getLogger().info(steps + ". Go to " + step.toString());
                System.out.println(steps + ". Go to " + step.toString());
                steps++;
            }
        }

    }

    private void getAllMazesFromServer() {
        try {
            pathFinder.availableMazes = mazeServiceClient.getNewMazes();
        } catch (IOException uhe) {
            final String message = "Could not connect to the service. " + uhe.getLocalizedMessage();
            getLogger().error(message);
            System.out.println(message);
        }
    }

    public boolean isMazeAvailableOnServer(final String mazeName) {
        if (pathFinder.availableMazes != null && pathFinder.availableMazes.size() > 0) {
            for (HashMap maze : pathFinder.availableMazes) {

                if (maze.get("code").equals(mazeName)) {
                    pathFinder.mazeMap = maze;
                    return true;
                }
            }

            getLogger().error("Maze \"" + mazeName + "\" doesn't exist on the Maze server");
            return false;

        } else {
            getLogger().error("Unable to get new maze(s) from the Maze service. List of availableMazes is "
                    + availableMazes);
            return false;
        }
    }

    public MazePoint getMazeStartPoint() {
        MazePoint startPoint = null;

        try {
            startPoint = mazeServiceClient.getMazeStartPosition(pathFinder.requestedMaze);
        } catch (IOException uhe) {
            final String message = "Could not connect to the service " + uhe.getLocalizedMessage();
            getLogger().error(message);
            System.out.println(message);
            return null;
        }

        if (startPoint == null) {
            final String message = "Maze start point is not found. Exit.";
            System.err.println(message);
            getLogger().error(message);
        } else {

            // Adding a start point into the Path and History
            pathFinder.pathToExit.add(startPoint);
            pathFinder.history.put(startPoint.hashCode(), startPoint);

            // Set current point as the start point
        }

        return startPoint;
    }

    private List<MazePoint> getAvailableDirections(final MazePoint current) {
        List<MazePoint> availableMazePoints = new ArrayList();
        MazePoint nextMazePoint = null;

        for (Directions direction : Directions.values()) {
            if (current.getX() == 0 && "WEST".equals(direction.toString())) {

                // moving to NORTH is not possible
                getLogger().info("Movement to WEST is not reasonable");
            } else if (current.getX() == (Integer) pathFinder.mazeMap.get("width") - 1
                    && "EAST".equals(direction.toString())) {
                getLogger().info("Movement to EAST is not reasonable");
                // moving to EAST is not possible
            } else if (current.getY() == 0 && "NORTH".equals(direction.toString())) {
                getLogger().info("Movement to WEST is not reasonable");
                // moving to WEST is not possible
            } else if (current.getY() == (Integer) pathFinder.mazeMap.get("height") - 1
                    && "NORTH".equals(direction.toString())) {
                getLogger().info("Movement to SOUTH is not reasonable");
                // moving to SOUTH is not possible
            } else {

                // Do the step to the defined direction
                nextMazePoint = moveToDirection(current, direction);
            }

            // check if nextMazePoint is not NUL after the last movement
            if (nextMazePoint != null) {

                // If th Exit has been found
                if (nextMazePoint.getCode() == 'x') {

                    // reset the available points list and add exit point only
                    availableMazePoints = new ArrayList<MazePoint>();
                    availableMazePoints.add(0, nextMazePoint);
                    getLogger().info("Exit has been found on position " + nextMazePoint.toString());
                    break;
                } else if (nextMazePoint.getCode() == '#') {
                    getLogger().info("There is a wall on position" + nextMazePoint.toString());
                } else {
                    availableMazePoints.add(nextMazePoint);
                    getLogger().info("FROM " + current.toString() + " the next steps is possible to "
                            + nextMazePoint.toString());
                }
            }
        }

        return availableMazePoints;
    }

    private MazePoint moveToDirection(final MazePoint point, final Directions direction) {
        try {
            return mazeServiceClient.moveToDirection(requestedMaze, new Step(point.getPoint(), direction.toString()));
        } catch (IOException uhe) {
            final String message = "Could not connect to the service " + ". " + uhe.getLocalizedMessage();
            getLogger().error(message);
            System.out.println(message);
            return null;
        }
    }

    public void setAvailableMazes(final List<HashMap> mazes) {
        this.availableMazes = mazes;
    }
}
