package exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        ResponseEntity<String> response =
                new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        return response;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleDataAccessException(DataAccessException e) {
        ResponseEntity<String> response =
                new ResponseEntity<>("DB 오류", HttpStatus.INTERNAL_SERVER_ERROR);
        return response;
    }
}
