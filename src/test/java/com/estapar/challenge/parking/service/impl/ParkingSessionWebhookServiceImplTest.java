package com.estapar.challenge.parking.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.exception.GarageFullException;
import com.estapar.challenge.parking.exception.NoAvailableParkingSpotException;
import com.estapar.challenge.parking.model.ParkingEvent;
import com.estapar.challenge.parking.model.ParkingSession;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.service.ParkingSessionService;
import com.estapar.challenge.parking.service.ParkingSpotService;
import com.estapar.challenge.parking.service.SectorService;
import com.estapar.challenge.parking.util.ParkingSessionTestBuilder;
import com.estapar.challenge.parking.util.ParkingSpotTestBuilder;
import com.estapar.challenge.parking.util.SectorTestBuilder;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ParkingSessionWebhookServiceImplTest {

  @Mock
  private ParkingSessionService parkingSessionService;
  @Mock
  private ParkingSpotService parkingSpotService;
  @Mock
  private SectorService sectorService;

  @InjectMocks
  private ParkingSessionWebhookServiceImpl parkingSessionWebhookService;

  private ParkingSpot parkingSpot;
  private ParkingSession parkingSession;

  @BeforeEach
  void beforeEach() {
    parkingSpot = ParkingSpotTestBuilder.builder()
        .withParentSector(SectorTestBuilder.defaultValues())
        .build();

    parkingSession = ParkingSessionTestBuilder.builder().build();
  }

  @Test
  void shouldSaveSessionOnEntry() {
    when(parkingSpotService.countOccupiedSpots()).thenReturn(5);
    when(sectorService.getEligibleMaxCapacity(any())).thenReturn(10);
    when(parkingSpotService.findFirstAvailable(any())).thenReturn(Optional.of(parkingSpot));

    parkingSessionWebhookService.handleVehicleEntry(toDTO(parkingSession));

    ParkingSession capturedSession = getSavedParkingSession();

    assertThat(capturedSession.getLicensePlate()).isEqualTo(parkingSession.getLicensePlate());
    assertThat(capturedSession.getParkingSpot()).isEqualTo(parkingSpot);
    assertThat(capturedSession.getParkingSpot().getOccupied()).isTrue();
    assertThat(capturedSession.getCurrentEvent()).isEqualTo(ParkingEvent.ENTRY);
    assertThat(capturedSession.getEntryOccupation()).isEqualTo(new BigDecimal("0.500"));
  }

  @Test
  void shouldThrowGarageFullExceptionOnEntry() {
    when(parkingSpotService.countOccupiedSpots()).thenReturn(10);
    when(sectorService.getEligibleMaxCapacity(any())).thenReturn(10);

    assertThatThrownBy(
        () -> parkingSessionWebhookService.handleVehicleEntry(toDTO(parkingSession))
    ).isInstanceOf(GarageFullException.class);
  }

  @Test
  void shouldThrowNoAvailableParkingSpotExceptionWithNoAvailableSpots() {
    when(parkingSpotService.countOccupiedSpots()).thenReturn(0);
    when(sectorService.getEligibleMaxCapacity(any())).thenReturn(10);
    when(parkingSpotService.findFirstAvailable(any())).thenReturn(Optional.empty());

    assertThatThrownBy(
        () -> parkingSessionWebhookService.handleVehicleEntry(toDTO(parkingSession))
    ).isInstanceOf(NoAvailableParkingSpotException.class);
  }

  @Test
  void shouldSaveParkEventWithSameParkingSpot() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    parkingSession.setParkingSpot(parkingSpot);
    parkingSessionWebhookService.handleVehicleParked(toDTO(parkingSession));

    ParkingSession capturedSession = getSavedParkingSession();

    assertThat(capturedSession.getParkingSpot()).isEqualTo(parkingSpot);
    assertThat(capturedSession.getCurrentEvent()).isEqualTo(ParkingEvent.PARKED);
  }

  @Test
  void shouldSaveParkEventWithDifferentParkingSpot() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    parkingSession.setParkingSpot(parkingSpot);
    parkingSpot.setOccupied(true);

    ParkingSpot diffentParkingSpot = ParkingSpotTestBuilder.builder()
        .withLatitude(new BigDecimal("18.283953"))
        .withLongitude(new BigDecimal("-66.109297"))
        .build();

    when(parkingSpotService.findByLatitudeAndLongitude(any(), any())).thenReturn(Optional.of(diffentParkingSpot));
    when(parkingSpotService.save(any())).thenReturn(diffentParkingSpot);

    ParkingSessionDTO dto = toDTO(
        ParkingSessionTestBuilder.builder()
            .withParkingSpot(diffentParkingSpot)
            .build()
    );

    parkingSessionWebhookService.handleVehicleParked(dto);

    ParkingSession capturedSession = getSavedParkingSession();

    assertThat(capturedSession.getParkingSpot()).isEqualTo(diffentParkingSpot);
    assertThat(capturedSession.getParkingSpot().getOccupied()).isTrue();
    assertThat(parkingSpot.getOccupied()).isFalse();
    assertThat(capturedSession.getCurrentEvent()).isEqualTo(ParkingEvent.PARKED);
  }

  @Test
  void shouldSaveExitEventWithNoCharge() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    setupExitSession(10);
    parkingSessionWebhookService.handleVehicleExit(toDTO(parkingSession));
    ParkingSession savedParkingSession = getSavedParkingSession();

    assertThat(savedParkingSession.getCurrentEvent()).isEqualTo(ParkingEvent.EXIT);
    assertThat(savedParkingSession.getTotalPrice()).isEqualTo(BigDecimal.ZERO);
    assertThat(savedParkingSession.getParkingSpot().getOccupied()).isFalse();
  }

  @Test
  void shouldSaveExitEventWithCharge() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    setupExitSession(120);
    parkingSessionWebhookService.handleVehicleExit(toDTO(parkingSession));
    ParkingSession savedParkingSession = getSavedParkingSession();

    assertThat(savedParkingSession.getCurrentEvent()).isEqualTo(ParkingEvent.EXIT);
    assertThat(savedParkingSession.getTotalPrice()).isEqualByComparingTo(new BigDecimal("20"));
    assertThat(savedParkingSession.getParkingSpot().getOccupied()).isFalse();
  }

  @Test
  // 10% discount with less than 25% occupation ratio
  void shouldApply10PercentDiscountWhenOccupancyBelow25Percent() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    setupExitSession(60, new BigDecimal("0.100"));
    parkingSessionWebhookService.handleVehicleExit(toDTO(parkingSession));
    ParkingSession savedParkingSession = getSavedParkingSession();

    assertThat(savedParkingSession.getCurrentEvent()).isEqualTo(ParkingEvent.EXIT);
    assertThat(savedParkingSession.getTotalPrice()).isEqualByComparingTo(new BigDecimal("9"));
    assertThat(savedParkingSession.getParkingSpot().getOccupied()).isFalse();
  }

  @Test
  void shouldApply10PercentSurchargeWhenOccupancyBetween50And75Percent() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    setupExitSession(60, new BigDecimal("0.650"));
    parkingSessionWebhookService.handleVehicleExit(toDTO(parkingSession));
    ParkingSession savedParkingSession = getSavedParkingSession();

    assertThat(savedParkingSession.getCurrentEvent()).isEqualTo(ParkingEvent.EXIT);
    assertThat(savedParkingSession.getTotalPrice()).isEqualByComparingTo(new BigDecimal("11"));
    assertThat(savedParkingSession.getParkingSpot().getOccupied()).isFalse();
  }

  @Test
  void shouldApply25PercentSurchargeWhenOccupancyAbove75Percent() {
    when(parkingSessionService.findByLicensePlateAndCurrentEvent(any(), any())).thenReturn(Optional.of(parkingSession));

    setupExitSession(60, new BigDecimal("0.900"));
    parkingSessionWebhookService.handleVehicleExit(toDTO(parkingSession));
    ParkingSession savedParkingSession = getSavedParkingSession();

    assertThat(savedParkingSession.getCurrentEvent()).isEqualTo(ParkingEvent.EXIT);
    assertThat(savedParkingSession.getTotalPrice()).isEqualByComparingTo(new BigDecimal("12.50"));
    assertThat(savedParkingSession.getParkingSpot().getOccupied()).isFalse();
  }

  private void setupExitSession(int extraMinutes) {
    setupExitSession(extraMinutes, parkingSession.getEntryOccupation());
  }

  private void setupExitSession(int extraMinutes, BigDecimal entryOccupation) {
    parkingSpot.setOccupied(true);
    parkingSession.setParkingSpot(parkingSpot);
    parkingSession.setExitTime(parkingSession.getEntryTime().plusMinutes(extraMinutes));
    parkingSession.setEntryOccupation(entryOccupation);
  }

  private ParkingSession getSavedParkingSession() {
    ArgumentCaptor<ParkingSession> parkingSessionArgumentCaptor = ArgumentCaptor.forClass(ParkingSession.class);
    verify(parkingSessionService).save(parkingSessionArgumentCaptor.capture());
    return parkingSessionArgumentCaptor.getValue();
  }

  private ParkingSessionDTO toDTO(ParkingSession session) {
    return ParkingSessionTestBuilder.toDTO(session);
  }
}