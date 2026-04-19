package com.estapar.challenge.parking.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.estapar.challenge.parking.config.ApplicationTestConfig;
import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.model.Sector;
import com.estapar.challenge.parking.util.ParkingSessionTestBuilder;
import com.estapar.challenge.parking.util.ParkingSpotTestBuilder;
import com.estapar.challenge.parking.util.SectorTestBuilder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ApplicationTestConfig.class)
public class ParkingSessionRepositoryTest {

  @Autowired
  private ParkingSessionRepository parkingSessionRepository;

  @Autowired
  private SectorRepository sectorRepository;

  @Autowired
  private ParkingSpotRepository parkingSpotRepository;

  @BeforeEach
  void beforeEach() {
    Sector sector = sectorRepository.save(SectorTestBuilder.defaultValues());

    ParkingSpot parkingSpot = parkingSpotRepository.save(
        ParkingSpotTestBuilder.builder().withParentSector(sector).build()
    );

    parkingSessionRepository.save(
        ParkingSessionTestBuilder.builder()
            .withParkingSpot(parkingSpot)
            .withTotalPrice(new BigDecimal("50"))
            .build()
    );
  }

  @Test
  void shouldFindCorrectTotalValueWithParkingSessionOnDate() {
    Optional<BigDecimal> bigDecimal = parkingSessionRepository.sumRevenueBySectorAndDate(
        SectorTestBuilder.DEFAULT_SECTOR_ID,
        LocalDate.from(ParkingSessionTestBuilder.DEFAULT_ENTRY_TIME)
    );

    assertThat(bigDecimal).isPresent();
    assertThat(bigDecimal.get()).isEqualTo(new BigDecimal("50.00"));
  }

  @Test
  void shouldFindZeroWithNoParkingSessionOnDate() {
    Optional<BigDecimal> bigDecimal = parkingSessionRepository.sumRevenueBySectorAndDate(
        SectorTestBuilder.DEFAULT_SECTOR_ID,
        LocalDate.of(2025, 12, 2)
    );

    assertThat(bigDecimal).isPresent();
    assertThat(bigDecimal.get()).isEqualTo(new BigDecimal("0.00"));
  }
}
