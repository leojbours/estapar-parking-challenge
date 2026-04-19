package com.estapar.challenge.parking.util;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import com.estapar.challenge.parking.model.ParkingSpot;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ParkingSessionTestBuilder {
  public static final Integer DEFAULT_SESSION_ID = null;
  public static final ParkingEvent DEFAULT_CURRENT_EVENT = ParkingEvent.ENTRY;
  public static final LocalDateTime DEFAULT_ENTRY_TIME = LocalDateTime.of(2024, 1, 1, 8, 0);
  public static final String DEFAULT_LICENSE_PLATE = "ABC1D23";
  public static final ParkingSpot DEFAULT_PARKING_SPOT = null;
  public static final LocalDateTime DEFAULT_EXIT_TIME = null;
  public static final BigDecimal DEFAULT_ENTRY_OCCUPATION = new BigDecimal("0.500");
  public static final BigDecimal DEFAULT_TOTAL_PRICE = null;

  public static ParkingSessionTestBuilder builder() {
    return new ParkingSessionTestBuilder();
  }

  public static ParkingSession defaultValues() {
    return ParkingSessionTestBuilder.builder().build();
  }

  private ParkingSessionTestBuilder() {}

  private Integer sessionId = DEFAULT_SESSION_ID;
  private ParkingEvent event = DEFAULT_CURRENT_EVENT;
  private LocalDateTime entryTime = DEFAULT_ENTRY_TIME;
  private String licensePlate = DEFAULT_LICENSE_PLATE;
  private ParkingSpot parkingSpot = DEFAULT_PARKING_SPOT;
  private LocalDateTime exitTime = DEFAULT_EXIT_TIME;
  private BigDecimal entryOccupation = DEFAULT_ENTRY_OCCUPATION;
  private BigDecimal totalPrice = DEFAULT_TOTAL_PRICE;

  public ParkingSessionTestBuilder withSessionId(Integer sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  public ParkingSessionTestBuilder withCurrentEvent(ParkingEvent currentEvent) {
    this.event = currentEvent;
    return this;
  }

  public ParkingSessionTestBuilder withEntryTime(LocalDateTime entryTime) {
    this.entryTime = entryTime;
    return this;
  }

  public ParkingSessionTestBuilder withLicensePlate(String licensePlate) {
    this.licensePlate = licensePlate;
    return this;
  }

  public ParkingSessionTestBuilder withParkingSpot(ParkingSpot parkingSpot) {
    this.parkingSpot = parkingSpot;
    return this;
  }

  public ParkingSessionTestBuilder withExitTime(LocalDateTime exitTime) {
    this.exitTime = exitTime;
    return this;
  }

  public ParkingSessionTestBuilder withEntryOccupation(BigDecimal entryOccupation) {
    this.entryOccupation = entryOccupation;
    return this;
  }

  public ParkingSessionTestBuilder withTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  public ParkingSession build() {
    ParkingSession parkingSession = new ParkingSession();
    parkingSession.setSessionId(sessionId);
    parkingSession.setCurrentEvent(event);
    parkingSession.setEntryTime(entryTime);
    parkingSession.setLicensePlate(licensePlate);
    parkingSession.setParkingSpot(parkingSpot);
    parkingSession.setExitTime(exitTime);
    parkingSession.setEntryOccupation(entryOccupation);
    parkingSession.setTotalPrice(totalPrice);

    return parkingSession;
  }

  public static ParkingSessionDTO toDTO(ParkingSession session) {
    return new ParkingSessionDTO(
        session.getCurrentEvent(),
        session.getEntryTime(),
        session.getLicensePlate(),
        session.getParkingSpot() != null ? session.getParkingSpot().getLatitude() : null,
        session.getParkingSpot() != null ? session.getParkingSpot().getLongitude() : null,
        session.getExitTime()
    );
  }
}
