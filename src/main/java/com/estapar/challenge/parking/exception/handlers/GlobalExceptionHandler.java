package com.estapar.challenge.parking.exception.handlers;

import com.estapar.challenge.parking.dto.ExceptionDTO;
import com.estapar.challenge.parking.exception.GarageFullException;
import com.estapar.challenge.parking.util.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(GarageFullException.class)
  public ResponseEntity<ExceptionDTO> handleGarageFull(GarageFullException exception, WebRequest webRequest) {
    ExceptionDTO exceptionDTO = ExceptionUtils.createExceptionDTO(exception, webRequest, HttpStatus.CONFLICT);

    return ResponseEntity.status(HttpStatus.CONFLICT).body(exceptionDTO);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionDTO> handleIllegalArgument(IllegalArgumentException exception, WebRequest webRequest) {
    ExceptionDTO exceptionDTO = ExceptionUtils.createExceptionDTO(exception, webRequest, HttpStatus.BAD_REQUEST);

    return ResponseEntity.badRequest().body(exceptionDTO);
  }

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<Void> handleNoSuchElement() {
    return ResponseEntity.notFound().build();
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionDTO> handleGenericException(Exception exception, WebRequest webRequest) {
    ExceptionDTO exceptionDTO = ExceptionUtils.createExceptionDTO(exception, webRequest, HttpStatus.INTERNAL_SERVER_ERROR);

    return ResponseEntity.internalServerError().body(exceptionDTO);
  }
}
