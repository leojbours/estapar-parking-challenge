package com.estapar.challenge.parking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalTime;

public record SectorDTO(
    @JsonProperty("sector") String id,
    @JsonProperty("base_price") BigDecimal basePrice,
    @JsonProperty("max_capacity") Integer maxCapacity,
    @JsonProperty("open_hour") LocalTime openTime,
    @JsonProperty("close_hour") LocalTime closeTime,
    @JsonProperty("duration_limit_minutes") Integer durationLimitMinutes
) {

}
