package ua.com.nc.dao;

/**
 * Exception that the dao-layer wraps into all other exceptions,
 * that are connected with database
 * This exception is handled by ua.com.nc.exceptions.CustomExceptionHandler
 * if there is no provided try-catch
 * @see ua.com.nc.exceptions.CustomExceptionHandler
 */
public class PersistException extends RuntimeException {

    public PersistException(String message) {
        super(message);
    }

    public PersistException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistException(Throwable cause) {
        super(cause);
    }
}
