package com.estapar.challenge.parking.service;

import com.estapar.challenge.parking.dto.SectorDTO;
import com.estapar.challenge.parking.model.Sector;
import java.time.LocalTime;
import java.util.Optional;

public interface SectorService {
  Iterable<Sector> saveAll(Iterable<Sector> sectors);

  Optional<Sector> findById(String id);
  Integer getEligibleMaxCapacity(LocalTime entryTime);

  Sector fromDTO(SectorDTO sectorDTO);
}
