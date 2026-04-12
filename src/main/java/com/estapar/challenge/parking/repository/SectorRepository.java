package com.estapar.challenge.parking.repository;

import com.estapar.challenge.parking.model.Sector;
import java.time.LocalTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectorRepository extends JpaRepository<Sector, String> {

  @Query("SELECT SUM(sec.maxCapacity) FROM Sector sec"
      + " WHERE"
      + " (sec.openTime <= sec.closeTime AND :entryTime BETWEEN sec.openTime AND sec.closeTime)"
      + " OR"
      + " (sec.openTime > sec.closeTime AND (:entryTime >= sec.openTime OR :entryTime <= sec.closeTime))")
  Integer getEligibleMaxCapacity(LocalTime entryTime);
}