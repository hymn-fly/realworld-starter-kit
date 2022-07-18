package org.realworld.demo.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomControllerAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleClientException(IllegalArgumentException ex) {
    return ErrorResponse.of(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }


  @AllArgsConstructor
  @Getter
  private static class ErrorResponse {

    String message;
    int statusCode;

    private static ErrorResponse of(String message, HttpStatus httpStatus) {
      return new ErrorResponse(message, httpStatus.value());
    }
  }
}
