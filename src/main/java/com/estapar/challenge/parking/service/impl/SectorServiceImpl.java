package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.dto.SectorDTO;
import com.estapar.challenge.parking.model.Sector;
import com.estapar.challenge.parking.repository.SectorRepository;
import com.estapar.challenge.parking.service.SectorService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SectorServiceImpl implements SectorService {

  private final SectorRepository sectorRepository;

  @Autowired
  public SectorServiceImpl(SectorRepository sectorRepository) {
    this.sectorRepository = sectorRepository;
  }

  @Override
  public Iterable<Sector> saveAll(Iterable<Sector> sectors) {
    return sectorRepository.saveAll(sectors);
  }

  @Override
  public Optional<Sector> findById(String id) {
    return sectorRepository.findById(id);
  }

  @Override
  public Integer getEligibleMaxCapacity(LocalTime entryTime) {
    return sectorRepository.getEligibleMaxCapacity(entryTime);
  }

  @Override
  public Sector fromDTO(SectorDTO sectorDTO) {
    Sector sector = new Sector();

    sector.setId(sectorDTO.id());
    sector.setBaseHourPrice(sectorDTO.basePrice());
    sector.setMaxCapacity(sectorDTO.maxCapacity());
    sector.setOpenTime(sectorDTO.openTime());
    sector.setCloseTime(sectorDTO.closeTime());
    sector.setDurationLimitMinutes(sectorDTO.durationLimitMinutes());

    return sector;
  }
}
