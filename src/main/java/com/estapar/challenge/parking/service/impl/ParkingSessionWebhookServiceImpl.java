package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.exception.GarageFullException;
import com.estapar.challenge.parking.exception.InvalidEventException;
import com.estapar.challenge.parking.exception.NoAvailableParkingSpotException;
import com.estapar.challenge.parking.exception.NoSuchSessionException;
import com.estapar.challenge.parking.model.OccupancyPricingMultiplier;
import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.service.ParkingSessionService;
import com.estapar.challenge.parking.service.ParkingSessionWebhookService;
import com.estapar.challenge.parking.service.ParkingSpotService;
import com.estapar.challenge.parking.service.SectorService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParkingSessionWebhookServiceImpl implements ParkingSessionWebhookService {

  private final ParkingSessionService parkingSessionService;
  private final SectorService sectorService;
  private final ParkingSpotService parkingSpotService;

  @Autowired
  public ParkingSessionWebhookServiceImpl(ParkingSessionService parkingSessionService, SectorService sectorService,
      ParkingSpotService parkingSpotService) {
    this.parkingSessionService = parkingSessionService;
    this.sectorService = sectorService;
    this.parkingSpotService = parkingSpotService;
  }

  @Override
  @Transactional
  public void handleWebhookEvent(ParkingSessionDTO parkingSessionDTO) {
    switch (parkingSessionDTO.event()) {
      case ENTRY -> handleVehicleEntry(parkingSessionDTO);
      case PARKED -> handleVehicleParked(parkingSessionDTO);
      case EXIT -> handleVehicleExit(parkingSessionDTO);
      default -> throw new InvalidEventException("Evento invalido: " + parkingSessionDTO.event());
    }
  }

  @Override
  @Transactional
  public void handleVehicleEntry(ParkingSessionDTO parkingSessionDTO) {
    ParkingSession parkingSession = new ParkingSession();
    parkingSession.setCurrentEvent(ParkingEvent.ENTRY);
    parkingSession.setEntryTime(parkingSessionDTO.entryTime());
    parkingSession.setLicensePlate(parkingSessionDTO.licensePlate());

    BigDecimal occupationRatio = getOccupationRatio(parkingSession.getEntryTime());

    if (occupationRatio.compareTo(BigDecimal.ONE) >= 0) {
      throw new GarageFullException("A garagem está com todas suas vagas elegíveis ocupadas. Tente novamente mais tarde");
    }

    parkingSession.setEntryOccupation(occupationRatio);

    Optional<ParkingSpot> firstAvailableSpot = parkingSpotService.findFirstAvailable(
        LocalTime.from(parkingSession.getEntryTime())
    );

    ParkingSpot parkingSpot = firstAvailableSpot.orElseThrow(
        () -> new NoAvailableParkingSpotException("Não há mais vagas disponíveis!")
    );

    updateSpotOccupancy(parkingSpot, true);

    parkingSession.setParkingSpot(parkingSpot);
    parkingSessionService.save(parkingSession);
  }

  private BigDecimal getOccupationRatio(LocalDateTime entryTime) {
    BigDecimal currentOccupation = new BigDecimal(parkingSpotService.countOccupiedSpots());
    BigDecimal garageMaxCapacity = new BigDecimal(sectorService.getEligibleMaxCapacity(LocalTime.from(entryTime)));

    return currentOccupation.divide(garageMaxCapacity, 3, RoundingMode.HALF_UP);
  }

  @Override
  @Transactional
  public void handleVehicleParked(ParkingSessionDTO parkingSessionDTO) {
    ParkingSession currentSession = getSessionFromDb(
        parkingSessionDTO.licensePlate(),
        ParkingEvent.ENTRY
    );

    currentSession.setCurrentEvent(ParkingEvent.PARKED);

    if (currentSession.getParkingSpot().isSame(parkingSessionDTO.lat(), parkingSessionDTO.lng())) {
      parkingSessionService.save(currentSession);
    }

    else {
      Optional<ParkingSpot> newParkingSpotOptional = parkingSpotService.findByLatitudeAndLongitude(
          parkingSessionDTO.lat(),
          parkingSessionDTO.lng()
      );

      newParkingSpotOptional.ifPresent(newParkingSpot -> {
        reassignParkingSpot(currentSession, newParkingSpot);
        parkingSessionService.save(currentSession);
      });
    }
  }

  private void reassignParkingSpot(ParkingSession currentSession, ParkingSpot newParkingSpot) {
    updateSpotOccupancy(currentSession.getParkingSpot(), false);

    currentSession.setParkingSpot(updateSpotOccupancy(newParkingSpot, true));
  }

  private ParkingSpot updateSpotOccupancy(ParkingSpot parkingSpot, boolean occupied) {
    parkingSpot.setOccupied(occupied);
    return parkingSpotService.save(parkingSpot);
  }

  @Override
  @Transactional
  public void handleVehicleExit(ParkingSessionDTO parkingSessionDTO) {
    ParkingSession currentSession = getSessionFromDb(
        parkingSessionDTO.licensePlate(),
        ParkingEvent.PARKED
    );

    currentSession.setCurrentEvent(ParkingEvent.EXIT);
    currentSession.setExitTime(parkingSessionDTO.exitTime());
    currentSession.setTotalPrice(getTotalPrice(currentSession));

    updateSpotOccupancy(currentSession.getParkingSpot(), false);

    parkingSessionService.save(currentSession);
  }

  private ParkingSession getSessionFromDb(String licensePlate, ParkingEvent event) {
    Optional<ParkingSession> dbSession = parkingSessionService.findByLicensePlateAndCurrentEvent(
        licensePlate,
        event
    );

    return dbSession.orElseThrow(
        () -> new NoSuchSessionException("Sessão invalida para o evento " + event + " e placa " + licensePlate)
    );
  }

  private BigDecimal getTotalPrice(ParkingSession parkingSession) {
    long totalMinutes = ChronoUnit.MINUTES.between(
        parkingSession.getEntryTime(),
        parkingSession.getExitTime()
    );

    BigDecimal totalPrice = BigDecimal.ZERO;

    if (totalMinutes > 30) {
      // Transforma minutos em horas, arrendonda para cima
      BigDecimal totalHours = new BigDecimal(Math.ceilDiv(totalMinutes, 60));
      BigDecimal pricePerHour = parkingSession.getBaseHourPrice()
          .multiply(OccupancyPricingMultiplier.multiplierFor(parkingSession.getEntryOccupation()));

      totalPrice = totalHours.multiply(pricePerHour);
    }

    return totalPrice;
  }
}
