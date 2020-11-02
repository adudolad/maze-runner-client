package de.zalando.adudoladov.client;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;

import de.zalando.adudoladov.domain.MazePoint;
import de.zalando.adudoladov.domain.Step;

/**
 * Created by adudoladov on 21/03/15.
 */
public interface MazeServiceClient {
    List<HashMap> getNewMazes() throws IOException;

    MazePoint getMazeStartPosition(String mazeCode) throws IOException;

    MazePoint moveToDirection(String mazeCode, Step step) throws IOException;
}
