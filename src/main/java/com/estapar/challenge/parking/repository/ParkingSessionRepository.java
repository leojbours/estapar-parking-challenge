package com.estapar.challenge.parking.repository;

import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Integer> {
  Optional<ParkingSession> findByLicensePlateAndCurrentEvent(String licensePlate, ParkingEvent exitTime);

  @Query("SELECT SUM(session.totalPrice) FROM ParkingSession session"
      + " WHERE"
      + " session.parkingSpot.parentSector.id = :sectorId"
      + " AND"
      + " CAST(session.entryTime AS date) = :date")
  Optional<BigDecimal> sumRevenueBySectorAndDate(String sectorId, LocalDate date);
}