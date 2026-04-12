package com.estapar.challenge.parking.service;

import com.estapar.challenge.parking.dto.RevenueRequestDTO;
import com.estapar.challenge.parking.dto.RevenueResponseDTO;

public interface RevenueService {
  RevenueResponseDTO getRevenue(RevenueRequestDTO dto);
}
