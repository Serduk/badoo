package exceptions;

import org.testng.Reporter;

/**
 * Exception handler
 *
 * Created by serdyuk on 3/12/17.
 */
public class BadooException extends Exception{
    public BadooException(String message) {
        super(message);
        Reporter.log(message);
    }
}
