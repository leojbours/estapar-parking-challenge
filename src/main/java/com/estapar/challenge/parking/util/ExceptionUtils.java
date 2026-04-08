package com.estapar.challenge.parking.util;

import com.estapar.challenge.parking.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;

public class ExceptionUtils {

  public static ExceptionDTO createExceptionDTO(Exception exception, WebRequest webRequest, HttpStatus status) {
    return new ExceptionDTO(
        webRequest.getDescription(false),
        HttpStatus.BAD_REQUEST,
        exception.getMessage(),
        Instant.now()
    );
  }
}
