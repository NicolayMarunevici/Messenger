package com.nickcourse.chat.exception;

import com.nickcourse.chat.model.dto.ErrorDetails;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserException exception,
                                                                  WebRequest webRequest) {
    ErrorDetails errorDetails =
        new ErrorDetails(new Date(), exception.getMessage(), webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception,
                                                                  WebRequest webRequest){
    ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
        webRequest.getDescription(false));
    return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
  }
}
