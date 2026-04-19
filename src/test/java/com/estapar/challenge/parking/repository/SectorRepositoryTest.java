package com.estapar.challenge.parking.repository;

import static org.assertj.core.api.Assertions.*;

import com.estapar.challenge.parking.config.ApplicationTestConfig;
import com.estapar.challenge.parking.util.SectorTestBuilder;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ApplicationTestConfig.class)
class SectorRepositoryTest {

  @Autowired
  SectorRepository sectorRepository;

  @Test
  void shouldFindMaxCapacityWithValidEntryTime() {
    sectorRepository.save(SectorTestBuilder.defaultValues());

    Integer eligibleMaxCapacity = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(10, 0)
    );

    assertThat(eligibleMaxCapacity).isEqualTo(SectorTestBuilder.DEFAULT_MAX_CAPACITY);
  }

  @Test
  void shouldNotFindMaxCapacityBeforeOpenTime() {
    sectorRepository.save(SectorTestBuilder.defaultValues());

    Integer eligibleMaxCapacityBeforeOpen = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(7, 0)
    );

    assertThat(eligibleMaxCapacityBeforeOpen).isEqualTo(0);
  }

  @Test
  void shouldNotFindMaxCapacityAfterCloseTime() {
    sectorRepository.save(SectorTestBuilder.defaultValues());

    Integer eligibleMaxCapacityAfterClose = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(22, 0)
    );

    assertThat(eligibleMaxCapacityAfterClose).isEqualTo(0);
  }

  @Test
  void shouldFindMaxCapacityBeforeMidnight() {
    sectorRepository.save(SectorTestBuilder.overMidnightOpenCloseTime());

    Integer eligibleMaxCapacityBeforeMidnight = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(23, 59)
    );

    assertThat(eligibleMaxCapacityBeforeMidnight).isEqualTo(SectorTestBuilder.DEFAULT_MAX_CAPACITY);
  }

  @Test
  void shouldFindMaxCapacityAfterMidnight() {
    sectorRepository.save(SectorTestBuilder.overMidnightOpenCloseTime());

    Integer eligibleMaxCapacityAfterMidnight = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(2, 0)
    );

    assertThat(eligibleMaxCapacityAfterMidnight).isEqualTo(SectorTestBuilder.DEFAULT_MAX_CAPACITY);
  }

  @Test
  void shouldNotFindMaxCapacityBeforeOpenTimeWhenCrossingMidnight() {
    sectorRepository.save(SectorTestBuilder.overMidnightOpenCloseTime());

    Integer eligibleMaxCapacityBeforeOpen = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(21, 0)
    );

    assertThat(eligibleMaxCapacityBeforeOpen).isEqualTo(0);
  }

  @Test
  void shouldNotFindMaxCapacityAfterCloseTimeWhenCrossingMidnight() {
    sectorRepository.save(SectorTestBuilder.overMidnightOpenCloseTime());

    Integer eligibleMaxCapacityAfterClose = sectorRepository.getEligibleMaxCapacity(
        LocalTime.of(9, 0)
    );

    assertThat(eligibleMaxCapacityAfterClose).isEqualTo(0);
  }
}