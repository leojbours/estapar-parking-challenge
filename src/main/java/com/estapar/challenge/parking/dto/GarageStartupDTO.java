package com.estapar.challenge.parking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record GarageStartupDTO(
    @JsonProperty("garage") List<SectorDTO> sectors,
    @JsonProperty("spots") List<ParkingSpotDTO> parkingSpots
) {

}
