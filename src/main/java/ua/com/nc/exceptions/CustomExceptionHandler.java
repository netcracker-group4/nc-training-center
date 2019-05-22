package ua.com.nc.exceptions;


import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.com.nc.dao.PersistException;

import java.nio.file.AccessDeniedException;

/**
 * Class with methods that automatically catch defined exceptions classes
 * exceptions also are logged here
 * message is stored in 'error.response.data' field
 * * how to display message to user
 * * Example
 * *  axios.delete('/groups/' + self.groups[index].id)
 * *      .then(function (response) {
 * *         .... code in case of success
 * *      })
 * *      .catch(function (error) {
 * *          console.log(error.response.data);
 * *          ... it outputs message error to console
 * *      });
 */

@Log4j2
@ControllerAdvice
public class CustomExceptionHandler {

    /**
     * @param exception PersistException caught automatically
     *                  (this type specified by @ExceptionHandler(PersistException.class) annotation)
     * @return error message that can then be displayed to user with some notification
     */
    @ExceptionHandler(PersistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handlePersistException(PersistException exception) {
        log.error(exception.getMessage(), exception);
        return "Oops \nServer error happened\n" +
                "Please go watch some YouTube video while we fix it ";
    }

    /**
     * For "what LogicException is ?" question answer
     *
     * @param exception LogicException
     * @return error message that can then be displayed to user with some notification
     * @see ua.com.nc.exceptions.LogicException
     */
    @ExceptionHandler(LogicException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleLogicException(LogicException exception) {
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleAccessDeniedException(AccessDeniedException exception) {
        log.error(exception.getMessage(), exception);
        return "Access denied";
    }
}
