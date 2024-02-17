package dev.fneira.interfaceprocessor.controller;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
    return new ErrorDto(
        "Validation error",
        ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
  }

  @ExceptionHandler(DateTimeParseException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorDto handleDateTimeParseException(final DateTimeParseException ex) {
    return new ErrorDto(
        "Invalid date format", String.format("value '%s' is invalid", ex.getParsedString()));
  }

  public record ErrorDto(String error, Object details) {
    public ErrorDto(final String error, final String detail) {
      this(error, List.of(detail));
    }
  }
}
