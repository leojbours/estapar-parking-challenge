package com.estapar.challenge.parking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public record ParkingSpotDTO(
    @JsonProperty("id") Integer id,
    @JsonProperty("sector") String sector,
    @JsonProperty("lat") BigDecimal latitude,
    @JsonProperty("lng") BigDecimal longitude,
    @JsonProperty("occupied") boolean occupied
) {

}
