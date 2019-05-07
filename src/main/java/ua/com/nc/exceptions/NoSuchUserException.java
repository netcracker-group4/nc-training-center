package ua.com.nc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoSuchUserException extends RuntimeException {

    public NoSuchUserException(String message) {
        super(message);
    }

    public NoSuchUserException() {
    }
}
