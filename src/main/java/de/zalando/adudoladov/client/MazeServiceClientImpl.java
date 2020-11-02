package de.zalando.adudoladov.client;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.io.InputStream;

import java.net.ConnectException;
import java.net.UnknownHostException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ExtractableResponse;

import de.zalando.adudoladov.domain.AbstractMaze;
import de.zalando.adudoladov.domain.MazePoint;
import de.zalando.adudoladov.domain.Step;

/**
 * Created by adudoladov on 20/03/15.
 */
public class MazeServiceClientImpl extends AbstractMaze implements MazeServiceClient {

    private static Properties defaultContext = null;
    private static String MAZE_SERVICE_URL;
    private static String GET_MAZE_METHOD;
    private static String GET_START_POSITION_METHOD;
    private static String MOVE_TO_POSITION_METHOD;

    static {
        defaultContext = new Properties();

        final InputStream readingDefaultContext = MazeServiceClientImpl.class.getClassLoader().getResourceAsStream(
                "application.properties");

        try {
            defaultContext.load(readingDefaultContext);
            getLogger().info("Application resources are loaded correctly.");
            MAZE_SERVICE_URL = defaultContext.getProperty("server.url.default");
            GET_MAZE_METHOD = defaultContext.getProperty("server.method.getMazes");
            GET_START_POSITION_METHOD = defaultContext.getProperty("server.method.getStartPosition");
            MOVE_TO_POSITION_METHOD = defaultContext.getProperty("server.method.moveToPosition");

        } catch (IOException ioe) {
            getLogger().error("Application properties could not be found. " + ioe.getLocalizedMessage());
            System.exit(0);
        }
    }

    public MazeServiceClientImpl() {
        super();
    }

    public MazeServiceClientImpl(final String serverURL) throws IllegalArgumentException {
        if (serverURL == null || serverURL.isEmpty()) {
            final String message = "Service URL must be defined correctly; actual value is " + serverURL.toString();
            getLogger().error(message);
            throw new IllegalArgumentException(message);
        } else {
            getLogger().info("Maze service URL is " + serverURL);
            MAZE_SERVICE_URL = serverURL;
        }
    }

    /**
     * @return
     */
    public List<HashMap> getNewMazes() throws UnknownHostException, ConnectException {
        List<HashMap> maze = new ArrayList<HashMap>();
        ExtractableResponse response = get(MAZE_SERVICE_URL + GET_MAZE_METHOD).then().extract();
        isValidResponse(response.statusCode());

        try {
            JsonPath mazeJson = response.jsonPath();
            maze = mazeJson.get("mazes");
            getLogger().info("The following maze(s) exist: " + mazeJson.getString("mazes.code"));
        } catch (com.jayway.restassured.path.json.exception.JsonPathException jpe) {
            final String message = "Unable to parse the maze list from server: " + jpe.getLocalizedMessage()
                    + ". Please check service URL";
            getLogger().error(message);
            System.err.println(message);
        } finally {
            return maze;
        }
    }

    /**
     * @param   mazeCode
     *
     * @return
     */
    public MazePoint getMazeStartPosition(final String mazeCode) throws UnknownHostException, ConnectException {
        MazePoint startPoint = null;
        try {
            JsonPath startPointJson = get(MAZE_SERVICE_URL + GET_MAZE_METHOD + mazeCode + GET_START_POSITION_METHOD)
                    .getBody().jsonPath();
            startPoint = new MazePoint(startPointJson.getInt("y"), startPointJson.getInt("x"), '@');
            getLogger().info("Maze " + mazeCode + " start point is " + startPoint);
        } catch (com.jayway.restassured.path.json.exception.JsonPathException jpe) {
            final String message = "Unable to parse the maze start point: " + jpe.getLocalizedMessage()
                    + ". Please check service URL or method declaration";
            getLogger().error(message);
            System.err.println(message);
        } finally {
            return startPoint;
        }

    }

    /**
     * @param   mazeCode
     * @param   step
     *
     * @return
     */
    public MazePoint moveToDirection(final String mazeCode, final Step step) throws UnknownHostException,
        ConnectException {
        JsonPath stepResultJson = null;

        ExtractableResponse response = given().contentType("application/json").body(step).when()
                                              .post(MAZE_SERVICE_URL + GET_MAZE_METHOD + mazeCode
                    + MOVE_TO_POSITION_METHOD).then().extract();
        try {
            stepResultJson = response.jsonPath();
        } catch (com.jayway.restassured.path.json.exception.JsonPathException jpe) {
            final String message = "Moving to direction" + step.toString() + " is not possible"
                    + jpe.getLocalizedMessage() + ". Exit.";
            System.err.println(message);
            getLogger().error(message);
        }

        if (isValidResponse(response.statusCode())) {
            MazePoint point = new MazePoint(stepResultJson.getInt("position.y"), stepResultJson.getInt("position.x"),
                    stepResultJson.getChar("field"));

            getLogger().debug("Valid step: moved FROM " + step.toString() + ", TO " + point.toString());

            return point;
        } else {
            getLogger().debug(stepResultJson.getString("message") + " : " + stepResultJson.getString("error"));
            return null;
        }
    }

    private boolean isValidResponse(final int code) throws UnknownHostException, ConnectException {
        switch (code) {

            case (200) : {
                getLogger().debug("Request is executed successfully, response code is HTTP 200");
                return true;
            }

            case (404) : {
                getLogger().debug("Bad Request. Response code is HTTP 404");
                return false;
            }

            case (500) : {
                getLogger().debug("Internal server error. The response code is HTTP 500");
                return false;
            }

            default : {
                getLogger().debug("Unknown error: Server returns HTTP " + code);
                return false;
            }
        }
    }

}
