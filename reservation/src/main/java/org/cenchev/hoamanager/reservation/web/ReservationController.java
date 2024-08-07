package org.cenchev.hoamanager.reservation.web;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.cenchev.hoamanager.reservation.model.dto.ReservationInitiateDTO;
import org.cenchev.hoamanager.reservation.model.entity.Reservation;
import org.cenchev.hoamanager.reservation.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/initiate")
    public ResponseEntity<Reservation> initiateReservation(@RequestBody ReservationInitiateDTO reservationInitiateDTO) {
        Reservation reservation = reservationService.initiateReservation(reservationInitiateDTO);
        return ResponseEntity.ok(reservation);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Reservation> approveReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.approveReservation(id);
        return ResponseEntity.ok(reservation);
    }
}