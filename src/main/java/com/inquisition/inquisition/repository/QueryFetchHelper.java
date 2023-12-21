package com.inquisition.inquisition.repository;

import java.util.function.Function;

import com.inquisition.inquisition.service.impl.InquisitionServiceImpl;
import org.jooq.exception.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryFetchHelper<Param, Resp> {
    private final Param param;
    private final Function<Param, Resp> function;
    public QueryFetchHelper(Param param, Function<Param, Resp> function) {
        this.param = param;
        this.function = function;
    }
    private static final int MAX_RETRIES = 3;
    private static final Logger logger = LoggerFactory.getLogger(QueryFetchHelper.class);
    public Resp fetch() {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                return function.apply(param);
            } catch (DataAccessException e) {
                retries++;

                logger.error("Database error occurred: {}. Retry {} of {}", e.getMessage(), retries, MAX_RETRIES);
                try {
                    Thread.sleep(500 * retries);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null;
    }
}
