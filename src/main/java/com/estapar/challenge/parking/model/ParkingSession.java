package com.estapar.challenge.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "parking_sessions")
public class ParkingSession {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer sessionId;

  @Column(name = "current_event", nullable = false)
  private ParkingEvent currentEvent;

  @Column(name = "entry_time", nullable = false)
  private LocalDateTime entryTime;

  @Column(name = "license_plate", nullable = false, length = 8)
  private String licensePlate;

  @ManyToOne
  @JoinColumn(name = "parking_spot_id")
  private ParkingSpot parkingSpot;

  @Column(name = "exit_time")
  private LocalDateTime exitTime;

  @Column(name = "entry_occupation", precision = 4, scale = 3, nullable = false)
  private BigDecimal entryOccupation;

  @Column(name = "total_price", precision = 10, scale = 2)
  private BigDecimal totalPrice;

  public BigDecimal getBaseHourPrice() {
    return parkingSpot.getParentSector().getBaseHourPrice();
  }
}
