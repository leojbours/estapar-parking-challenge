package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.dto.GarageStartupDTO;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.model.Sector;
import com.estapar.challenge.parking.service.ParkingSpotService;
import com.estapar.challenge.parking.service.SectorService;
import java.util.List;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GarageInitializerService {

  private final RestClient restClient = RestClient.create();

  private final ParkingSpotService parkingSpotService;
  private final SectorService sectorService;

  public GarageInitializerService(ParkingSpotService parkingSpotService, SectorService sectorService) {
    this.parkingSpotService = parkingSpotService;
    this.sectorService = sectorService;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void initialize() {
    GarageStartupDTO dto = restClient.get()
        .uri("http://localhost:3000/garage")
        .retrieve()
        .body(GarageStartupDTO.class);

    if (dto != null && dto.sectors() != null)
    {
      List<Sector> sectors = dto.sectors().stream().map(sectorService::fromDTO).toList();
      sectorService.saveAll(sectors);

      if (dto.parkingSpots() != null) {
        List<ParkingSpot> parkingSpots = dto.parkingSpots().stream().map(parkingSpotService::fromDto).toList();
        parkingSpotService.saveAll(parkingSpots);
      }
    }
  }
}
