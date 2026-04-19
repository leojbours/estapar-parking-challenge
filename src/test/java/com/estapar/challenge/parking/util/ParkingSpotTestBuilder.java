package com.estapar.challenge.parking.util;

import com.estapar.challenge.parking.model.ParkingSpot;
import com.estapar.challenge.parking.model.Sector;
import java.math.BigDecimal;

public class ParkingSpotTestBuilder {
  public static final Integer DEFAULT_PARKING_SPOT_ID = null;
  public static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal("-46.633308");
  public static final BigDecimal DEFAULT_LATITUDE = new BigDecimal("-23.550520");
  public static final Boolean DEFAULT_OCCUPIED = false;
  public static final Sector DEFAULT_PARENT_SECTOR = null;

  public static ParkingSpotTestBuilder builder() {
    return new ParkingSpotTestBuilder();
  }

  public static ParkingSpot defaultValues() {
    return ParkingSpotTestBuilder.builder().build();
  }

  private ParkingSpotTestBuilder() {}

  private Integer id = DEFAULT_PARKING_SPOT_ID;
  private BigDecimal longitude = DEFAULT_LONGITUDE;
  private BigDecimal latitude = DEFAULT_LATITUDE;
  private Boolean occupied = DEFAULT_OCCUPIED;
  private Sector parentSector = DEFAULT_PARENT_SECTOR;

  public ParkingSpotTestBuilder withId(Integer id) {
    this.id = id;
    return this;
  }

  public ParkingSpotTestBuilder withLongitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  public ParkingSpotTestBuilder withLatitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  public ParkingSpotTestBuilder withOccupied(Boolean occupied) {
    this.occupied = occupied;
    return this;
  }

  public ParkingSpotTestBuilder withParentSector(Sector parentSector) {
    this.parentSector = parentSector;
    return this;
  }

  public ParkingSpot build() {
    ParkingSpot parkingSpot = new ParkingSpot();
    parkingSpot.setId(id);
    parkingSpot.setLongitude(longitude);
    parkingSpot.setLatitude(latitude);
    parkingSpot.setOccupied(occupied);
    parkingSpot.setParentSector(parentSector);

    return parkingSpot;
  }
}
