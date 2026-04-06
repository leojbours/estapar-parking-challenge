package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.dto.ParkingSpotDTO;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.model.Sector;
import com.estapar.challenge.parking.repository.ParkingSpotRepository;
import com.estapar.challenge.parking.service.ParkingSpotService;
import com.estapar.challenge.parking.service.SectorService;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingSpotServiceImpl implements ParkingSpotService {

  private final ParkingSpotRepository parkingSpotRepository;
  private final SectorService sectorService;

  @Autowired
  public ParkingSpotServiceImpl(ParkingSpotRepository parkingSpotRepository, SectorService sectorService) {
    this.parkingSpotRepository = parkingSpotRepository;
    this.sectorService = sectorService;
  }

  @Override
  public ParkingSpot save(ParkingSpot parkingSpot) {
    return parkingSpotRepository.save(parkingSpot);
  }

  @Override
  public Iterable<ParkingSpot> saveAll(Iterable<ParkingSpot> parkingSpots) {
    return parkingSpotRepository.saveAll(parkingSpots);
  }

  @Override
  public ParkingSpot findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude) {
    return parkingSpotRepository.findByLatitudeAndLongitude(latitude, longitude);
  }

  @Override
  public Optional<ParkingSpot> findFirstAvailable(LocalTime entryHour) {
    return parkingSpotRepository.findFirstAvailable(entryHour);
  }

  @Override
  public Integer countOccupiedSpots() {
    return parkingSpotRepository.countByOccupiedTrue();
  }

  @Override
  public ParkingSpot fromDto(ParkingSpotDTO parkingSpotDTO)  {
    ParkingSpot parkingSpot = new ParkingSpot();

//    parkingSpot.setId(parkingSpotDTO.id());
    parkingSpot.setLatitude(parkingSpotDTO.latitude());
    parkingSpot.setLongitude(parkingSpotDTO.longitude());
    parkingSpot.setOccupied(parkingSpotDTO.occupied());

    Optional<Sector> optionalSector = sectorService.findById(parkingSpotDTO.sector());
    optionalSector.ifPresent(parkingSpot::setParentSector);

    return parkingSpot;
  }

}
