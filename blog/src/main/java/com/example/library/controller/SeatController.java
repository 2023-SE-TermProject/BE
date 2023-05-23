package com.example.library.controller;

import com.example.library.model.Seat;
import com.example.library.model.SeatReservation;
import com.example.library.repository.SeatRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RestController
public class SeatController {
    private final SeatRepository seatRepository;

    public SeatController(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    @GetMapping("/seats/{floor}")
    public List<Seat> getSeatsByFloor(@PathVariable Integer floor) {
        return seatRepository.findAvailableSeatsByFloor(floor);
    }

    @Transactional
    @PostMapping("/reserveSeat")
    public Boolean reserveSeat(@RequestBody SeatReservation seatReservation) {
        Seat seat = seatRepository.findById(seatReservation.getSeatId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid seatId: " + seatReservation.getSeatId()));
        
        if (seat.getStatus().equals("available")) {
            seat.setStatus("reserved");
            seat.setUserName(String.valueOf(seatReservation.getUserId()));
            seatRepository.save(seat);
            return true;
        } else {
            return false;
        }
    }
}