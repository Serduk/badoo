package core.helpers.jettyLogger;

import org.apache.log4j.Logger;

/**
 * Loger
 * Created by serdyuk on 5/10/17.
 */
public class Log {
    final static Logger logger = Logger.getLogger(Log.class);

    public static void info(Object message){
        logger.info(message);
    }

    public static void debug(Object message){
        logger.debug(message);}

    public static void error(Object message, Throwable throwable){logger.error(message, throwable);}

    public static void warn(Object message){logger.warn(message);}

}
