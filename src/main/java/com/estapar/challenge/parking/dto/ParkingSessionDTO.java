package com.estapar.challenge.parking.dto;

import com.estapar.challenge.parking.model.ParkingEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ParkingSessionDTO(
    @JsonProperty("event_type") ParkingEvent event,
    @JsonProperty("entry_time") LocalDateTime entryTime,
    @JsonProperty("license_plate") String licensePlate,
    @JsonProperty("lat") BigDecimal lat,
    @JsonProperty("lng") BigDecimal lng,
    @JsonProperty("exit_time") LocalDateTime exitTime
) {
}
