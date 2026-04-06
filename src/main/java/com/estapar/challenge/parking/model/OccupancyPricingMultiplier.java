package com.estapar.challenge.parking.model;

import java.math.BigDecimal;

public enum OccupancyPricingMultiplier {
  LOW("0.25", "0.9"),
  STANDARD("0.50", "1.0"),
  HIGH("0.75", "1.1"),
  FULL("1.00", "1.25");

  private final BigDecimal threshold;
  private final BigDecimal multiplier;

  OccupancyPricingMultiplier(String threshold, String multiplier) {
    this.threshold = new BigDecimal(threshold);
    this.multiplier = new BigDecimal(multiplier);
  }

  public static BigDecimal multiplierFor(BigDecimal occupancy) {
    for (OccupancyPricingMultiplier tier : values()) {
      if (occupancy.compareTo(tier.threshold) <= 0) {
        return tier.multiplier;
      }
    }
    return FULL.multiplier;
  }
}
