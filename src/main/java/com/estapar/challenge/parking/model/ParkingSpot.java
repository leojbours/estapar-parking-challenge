package com.estapar.challenge.parking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "parking_spots",
    uniqueConstraints = @UniqueConstraint(columnNames = {"longitude", "latitude"})
)
public class ParkingSpot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "longitude", precision = 10, scale = 8, nullable = false)
  private BigDecimal longitude;

  @Column(name = "latitude", precision = 10, scale = 8, nullable = false)
  private BigDecimal latitude;

  @Column(name = "occupied", nullable = false)
  private Boolean occupied;

  @ManyToOne
  @JoinColumn(name = "sector_id")
  private Sector parentSector;

  public boolean isSame(BigDecimal longitude, BigDecimal latitude) {
    return this.longitude.equals(longitude) && this.latitude.equals(latitude);
  }
}
