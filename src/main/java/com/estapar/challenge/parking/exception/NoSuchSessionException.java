package com.estapar.challenge.parking.exception;

import java.util.NoSuchElementException;

public class NoSuchSessionException extends NoSuchElementException {

  public NoSuchSessionException(String message) {
    super(message);
  }
}
