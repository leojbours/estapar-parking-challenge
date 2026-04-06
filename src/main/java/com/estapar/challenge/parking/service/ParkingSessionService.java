package com.estapar.challenge.parking.service;

import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public interface ParkingSessionService {
  ParkingSession save(ParkingSession parkingSession);
  Optional<ParkingSession> findByLicensePlateAndCurrentEvent(String licensePlate, ParkingEvent currentEvent);

  Optional<BigDecimal> sumRevenueBySectorAndDate(String sectorId, LocalDate date);
}
