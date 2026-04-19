package com.estapar.challenge.parking.repository;

import static org.assertj.core.api.Assertions.*;

import com.estapar.challenge.parking.config.ApplicationTestConfig;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.model.Sector;
import com.estapar.challenge.parking.util.ParkingSpotTestBuilder;
import com.estapar.challenge.parking.util.SectorTestBuilder;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ApplicationTestConfig.class)
public class ParkingSpotRepositoryTest {

  @Autowired
  SectorRepository sectorRepository;

  @Autowired
  ParkingSpotRepository parkingSpotRepository;

  @Test
  void shouldFindAvailableSpotWithNormalTime() {
    saveParkingSpotForDefaultSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(10, 0)
    );

    assertThat(firstAvailable).isPresent();
  }

  @Test
  void shouldNotFindAvailableSpotBeforeOpenTime() {
    saveParkingSpotForDefaultSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(7, 0)
    );

    assertThat(firstAvailable).isNotPresent();
  }

  @Test
  void shouldNotFindAvailableSpotAfterCloseTime() {
    saveParkingSpotForDefaultSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(22, 0)
    );

    assertThat(firstAvailable).isNotPresent();
  }

  @Test
  void shouldFindAvailableSpotBeforeMidnightWhenSectorCrossesMidnight() {
    saveParkingSpotForOverMidnightSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(23, 59)
    );

    assertThat(firstAvailable).isPresent();
  }

  @Test
  void shouldFindAvailableSpotAfterMidnightWhenSectorCrossesMidnight() {
    saveParkingSpotForOverMidnightSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(2, 0)
    );

    assertThat(firstAvailable).isPresent();
  }

  @Test
  void shouldNotFindAvailableSpotBeforeOpenTimeWhenSectorCrossesMidnight() {
    saveParkingSpotForOverMidnightSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(21, 0)
    );

    assertThat(firstAvailable).isNotPresent();
  }

  @Test
  void shouldNotFindAvailableSpotAfterCloseTimeWhenSectorCrossesMidnight() {
    saveParkingSpotForOverMidnightSector();

    Optional<ParkingSpot> firstAvailable = parkingSpotRepository.findFirstAvailable(
        LocalTime.of(9, 0)
    );

    assertThat(firstAvailable).isNotPresent();
  }

  private void saveParkingSpotForDefaultSector() {
    Sector savedSector = sectorRepository.save(SectorTestBuilder.defaultValues());
    saveParkingSpot(savedSector);
  }

  private void saveParkingSpotForOverMidnightSector() {
    Sector savedSector = sectorRepository.save(SectorTestBuilder.overMidnightOpenCloseTime());
    saveParkingSpot(savedSector);
  }

  private void saveParkingSpot(Sector savedSector) {
    ParkingSpot parkingSpot = ParkingSpotTestBuilder.defaultValues();
    parkingSpot.setParentSector(savedSector);
    parkingSpotRepository.save(parkingSpot);
  }
}
