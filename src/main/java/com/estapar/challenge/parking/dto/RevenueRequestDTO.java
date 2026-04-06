package com.estapar.challenge.parking.dto;

import java.time.LocalDate;

public record RevenueRequestDTO(
    String sector,
    LocalDate date
) {

}
