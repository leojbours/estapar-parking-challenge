package com.estapar.challenge.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sectors")
public class Sector {
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "base_hour_price", nullable = false)
  private BigDecimal baseHourPrice;

  @Column(name = "max_capacity", nullable = false)
  private Integer maxCapacity;

  @Column(name = "open_time", nullable = false)
  private LocalTime openTime;

  @Column(name = "close_time", nullable = false)
  private LocalTime closeTime;

  @Column(name = "duration_limit_minutes", nullable = false)
  private Integer durationLimitMinutes;

  @OneToMany(mappedBy = "parentSector")
  private List<ParkingSpot> parkingSpots;
}
