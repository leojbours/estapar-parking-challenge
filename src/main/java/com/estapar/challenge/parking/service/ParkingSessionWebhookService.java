package com.estapar.challenge.parking.service;

import com.estapar.challenge.parking.dto.ParkingSessionDTO;
import com.estapar.challenge.parking.model.ParkingSession;

public interface ParkingSessionWebhookService {
  void handleWebhookEvent(ParkingSessionDTO parkingSessionDTO);
  void handleVehicleEntry(ParkingSessionDTO parkingSessionDTO);
  void handleVehicleParked(ParkingSessionDTO parkingSessionDTO);
  void handleVehicleExit(ParkingSessionDTO parkingSessionDTO);
}
