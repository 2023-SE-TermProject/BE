package com.example.setermproject.domain.seat.controller;

import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import com.example.setermproject.domain.seat.dto.SeatReservation;
import com.example.setermproject.domain.seat.entity.Seat;
import com.example.setermproject.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/seats/{floor}")
    public ResponseEntity<List<Seat>> getSeatsByFloor(@PathVariable Integer floor) {
        try {
            return new ResponseEntity<>(seatService.getSeatsByFloor(floor), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    @PostMapping("/reserveSeat")
    public ResponseEntity<Boolean> reserveSeat(@RequestBody SeatReservation seatReservation) {
        try {
            return new ResponseEntity<>(seatService.reserveSeat(seatReservation), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Seat not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
