package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import com.estapar.challenge.parking.repository.ParkingSessionRepository;
import com.estapar.challenge.parking.service.ParkingSessionService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingSessionServiceImpl implements ParkingSessionService {

  private final ParkingSessionRepository parkingSessionRepository;

  @Autowired
  public ParkingSessionServiceImpl( ParkingSessionRepository parkingSessionRepository) {
    this.parkingSessionRepository = parkingSessionRepository;
  }

  @Override
  public ParkingSession save(ParkingSession parkingSession) {
    return parkingSessionRepository.save(parkingSession);
  }

  @Override
  public Optional<ParkingSession> findByLicensePlateAndCurrentEvent(String licensePlate, ParkingEvent currentEvent) {
    return parkingSessionRepository.findByLicensePlateAndCurrentEvent(licensePlate, currentEvent);
  }

  @Override
  public Optional<BigDecimal> sumRevenueBySectorAndDate(String sectorId, LocalDate date) {
    return parkingSessionRepository.sumRevenueBySectorAndDate(sectorId, date);
  }
}
