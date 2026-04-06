package com.estapar.challenge.parking.controller;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.service.ParkingSessionWebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/webhook")
public class ParkingSessionWebhookController {

  private final ParkingSessionWebhookService webhookService;

  @Autowired
  public ParkingSessionWebhookController(ParkingSessionWebhookService webhookService) {
    this.webhookService = webhookService;
  }

  @PostMapping()
  public ResponseEntity<Void> webhook(@RequestBody ParkingSessionDTO parkingSessionDTO) {
    webhookService.handleWebhookEvent( parkingSessionDTO );

    return ResponseEntity.ok().build();
  }
}
