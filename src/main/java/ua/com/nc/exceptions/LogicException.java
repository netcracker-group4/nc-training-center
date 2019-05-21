package ua.com.nc.exceptions;

/**
 * Class encapsulates error message and the course of a logic exception
 * This class is created to store specific error message that must be showed to user
 * while for handling PersistException the error message is specified directly inside of handling method
 * this approach is way too hardcoded
 * so this class is the way to make error handling more agile
 * anywhere where the error message is already defined ('You cannot delete group which has lessons coming') you can
 * wrap any actual exception into this one
 * .....
 * catch(exception){
 *     throw new LogicException(myMessage, realException);
 * }
 *
 * then this new LogicException will be caught automatically
 * myMessage - will be sent to front-end inside of 'error.response.data' (you can then get it inside of catch clause)
 * realException will be logged by Log$j log.trace(realException.getMessage(), realException);
 */
public class LogicException extends RuntimeException {

    /**
     * in case if there isn't any cause exception
     * @param message message to show to user
     */
    public LogicException(String message) {
        super(message);
    }

    public LogicException(String message, Throwable cause) {
        super(message, cause);
    }
}
