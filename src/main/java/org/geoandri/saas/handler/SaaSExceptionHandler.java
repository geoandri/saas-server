package org.geoandri.saas.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import org.geoandri.saas.dto.response.GenericErrorsExceptionResponse;
import org.geoandri.saas.dto.response.GenericExceptionResponse;
import org.geoandri.saas.dto.response.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SaaSExceptionHandler {

  private static Logger logger = LoggerFactory.getLogger(SaaSExceptionHandler.class);

  @ExceptionHandler
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public GenericErrorsExceptionResponse handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    List<ValidationError> validationErrors = getAllValidationErrors(exception.getBindingResult());
    logger.error(exception.getMessage());
    return new GenericErrorsExceptionResponse(validationErrors, Instant.now());
  }

  @ExceptionHandler
  @ResponseStatus(FORBIDDEN)
  @ResponseBody
  public GenericExceptionResponse handleMethodArgumentNotValidException(
      AccountStatusException exception) {
    logger.error(exception.getMessage());
    return new GenericExceptionResponse(exception.getMessage(), Instant.now());
  }

  private List<ValidationError> getAllValidationErrors(BindingResult bindingResult) {
    return bindingResult.getFieldErrors().stream()
        .map(
            fieldError ->
                new ValidationError(
                    fieldError.getField(),
                    fieldError.getRejectedValue() == null
                        ? "null"
                        : fieldError.getRejectedValue().toString(),
                    fieldError.getDefaultMessage()))
        .collect(Collectors.toList());
  }
}
