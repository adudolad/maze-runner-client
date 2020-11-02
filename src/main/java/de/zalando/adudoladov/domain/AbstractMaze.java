package de.zalando.adudoladov.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by adudoladov on 20/03/15.
 */
public class AbstractMaze {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMaze.class);
    public static final int SEED = 17;

    public static Logger getLogger() {
        return LOGGER;
    }
}
