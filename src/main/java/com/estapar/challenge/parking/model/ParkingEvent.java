package com.estapar.challenge.parking.model;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum ParkingEvent {
  ENTRY,
  PARKED,
  EXIT,
  @JsonEnumDefaultValue
  UNKNOWN
}
