package plantventure.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtils {
    private LoggerUtils() {
    }

    public static final Logger getLogger(Class clazz) {
        return LoggerFactory.getLogger(clazz);
    }


}
