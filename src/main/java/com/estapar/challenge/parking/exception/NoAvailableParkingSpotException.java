package com.estapar.challenge.parking.exception;

import java.util.NoSuchElementException;

public class NoAvailableParkingSpotException extends NoSuchElementException {
  public NoAvailableParkingSpotException(String message) {
    super(message);
  }
}
