package com.estapar.challenge.parking.util;

import com.estapar.challenge.parking.model.Sector;
import java.math.BigDecimal;
import java.time.LocalTime;

public class SectorTestBuilder {
  public static final String DEFAULT_SECTOR_ID = "A";
  public static final BigDecimal DEFAULT_BASE_HOUR_PRICE = new BigDecimal("10");
  public static final Integer DEFAULT_MAX_CAPACITY = 10;
  public static final LocalTime DEFAULT_OPEN_TIME = LocalTime.of(8, 0);
  public static final LocalTime DEFAULT_CLOSE_TIME = LocalTime.of(20, 0);
  public static final LocalTime OVER_MIDNIGHT_OPEN_TIME = LocalTime.of(22, 0);
  public static final LocalTime OVER_MIDNIGHT_CLOSE_TIME = LocalTime.of(6, 0);
  public static final Integer DEFAULT_DURATION_LIMIT_MINUTES = 120;

  public static SectorTestBuilder builder() {
    return new SectorTestBuilder();
  }

  public static Sector defaultValues() {
    return SectorTestBuilder.builder().build();
  }

  public static Sector overMidnightOpenCloseTime() {
    return SectorTestBuilder.builder()
        .withOpenTime(OVER_MIDNIGHT_OPEN_TIME)
        .withCloseTime(OVER_MIDNIGHT_CLOSE_TIME)
        .build();
  }

  private SectorTestBuilder() {}

  private String id = DEFAULT_SECTOR_ID;
  private BigDecimal baseHourPrice = DEFAULT_BASE_HOUR_PRICE;
  private Integer maxCapacity = DEFAULT_MAX_CAPACITY;
  private LocalTime openTime = DEFAULT_OPEN_TIME;
  private LocalTime closeTime = DEFAULT_CLOSE_TIME;
  private Integer durationLimitMinutes = DEFAULT_DURATION_LIMIT_MINUTES;

  public SectorTestBuilder withId(String id) {
    this.id = id;
    return this;
  }

  public SectorTestBuilder withBaseHourPrice(BigDecimal baseHourPrice) {
    this.baseHourPrice = baseHourPrice;
    return this;
  }

  public SectorTestBuilder withMaxCapacity(Integer maxCapacity) {
    this.maxCapacity = maxCapacity;
    return this;
  }

  public SectorTestBuilder withOpenTime(LocalTime openTime) {
    this.openTime = openTime;
    return this;
  }

  public SectorTestBuilder withCloseTime(LocalTime closeTime) {
    this.closeTime = closeTime;
    return this;
  }

  public SectorTestBuilder withDurationLimitMinutes(Integer durationLimitMinutes) {
    this.durationLimitMinutes = durationLimitMinutes;
    return this;
  }

  public Sector build() {
    Sector sector = new Sector();
    sector.setId(id);
    sector.setBaseHourPrice(baseHourPrice);
    sector.setMaxCapacity(maxCapacity);
    sector.setOpenTime(openTime);
    sector.setCloseTime(closeTime);
    sector.setDurationLimitMinutes(durationLimitMinutes);

    return sector;
  }
}
