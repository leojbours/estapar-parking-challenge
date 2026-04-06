package com.estapar.challenge.parking.service.impl;

import com.estapar.challenge.parking.dto.RevenueRequestDTO;
import com.estapar.challenge.parking.dto.RevenueResponseDTO;
import com.estapar.challenge.parking.service.ParkingSessionService;
import com.estapar.challenge.parking.service.RevenueService;
import java.math.BigDecimal;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class RevenueServiceImpl implements RevenueService {
  private final ParkingSessionService parkingSessionService;

  public RevenueServiceImpl( ParkingSessionService parkingSessionService) {
    this.parkingSessionService = parkingSessionService;
  }

  @Override
  public RevenueResponseDTO getRevenue(RevenueRequestDTO dto) {
    return new RevenueResponseDTO(
        parkingSessionService.sumRevenueBySectorAndDate(dto.sector(), dto.date()).orElse(BigDecimal.ZERO),
        "BRL",
        Instant.now()
    );
  }
}
