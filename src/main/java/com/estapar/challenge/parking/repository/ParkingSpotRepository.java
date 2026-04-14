package com.estapar.challenge.parking.repository;

import com.estapar.challenge.parking.model.ParkingSpot;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Integer> {
  Optional<ParkingSpot> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);

  @Query("SELECT park FROM ParkingSpot park WHERE park.occupied = false AND park.parentSector.id"
      + " IN (SELECT sec.id FROM Sector sec WHERE"
      + "   (sec.openTime <= sec.closeTime AND :entryHour BETWEEN sec.openTime AND sec.closeTime)"
      + "   OR"
      + "   (sec.openTime > sec.closeTime AND (:entryHour >= sec.openTime OR :entryHour <= sec.closeTime))"
      + " )"
      + " ORDER BY park.id ASC LIMIT 1")
  Optional<ParkingSpot> findFirstAvailable(LocalTime entryHour);

  Integer countByOccupiedTrue();
}