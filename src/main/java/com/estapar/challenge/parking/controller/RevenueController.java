package com.estapar.challenge.parking.controller;

import com.estapar.challenge.parking.dto.RevenueRequestDTO;
import com.estapar.challenge.parking.dto.RevenueResponseDTO;
import com.estapar.challenge.parking.service.RevenueService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/revenue")
public class RevenueController {

  private final RevenueService revenueService;

  @Autowired
  public RevenueController(RevenueService revenueService) {
    this.revenueService = revenueService;
  }

  @GetMapping
  public ResponseEntity<RevenueResponseDTO> getRevenue(@RequestBody RevenueRequestDTO dto) {
    return ResponseEntity.ok(revenueService.getRevenue(dto));
  }
}
