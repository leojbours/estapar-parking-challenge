package com.estapar.challenge.parking.service;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.dto.ParkingSpotDTO;
import com.estapar.challenge.parking.model.ParkingSession;
import com.estapar.challenge.parking.model.ParkingSpot;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public interface ParkingSpotService {
  ParkingSpot save(ParkingSpot parkingSpot);
  Iterable<ParkingSpot> saveAll(Iterable<ParkingSpot> parkingSpots);
  ParkingSpot findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
  Optional<ParkingSpot> findFirstAvailable(LocalTime entryHour);
  Integer countOccupiedSpots();

  ParkingSpot fromDto(ParkingSpotDTO parkingSpotDTO);
}
