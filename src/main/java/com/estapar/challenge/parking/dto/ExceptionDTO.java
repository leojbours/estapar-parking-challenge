package com.estapar.challenge.parking.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ExceptionDTO(
    String apiPath,
    HttpStatus errorCode,
    String errorMessage,
    Instant errorTimestamp
) {
}
