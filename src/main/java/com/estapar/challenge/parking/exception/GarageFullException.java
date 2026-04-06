package com.estapar.challenge.parking.exception;

public class GarageFullException extends RuntimeException {
  public GarageFullException(String message) {
    super(message);
  }
}
