package com.example.setermproject.domain.seat.controller;

import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.reqeust.SeatReservation;
import com.example.setermproject.domain.seat.dto.response.SeatInfo;
import com.example.setermproject.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    // dependency injection by spring container
    private final SeatService seatService;

    /**
     * find seat information list of specific floor
     * @param floor
     * @return List<SeatInfo>
     */
    @GetMapping("/{floor}")
    public ResponseEntity<List<SeatInfo>> getSeatsByFloor(@PathVariable Integer floor) {
        try {
            return new ResponseEntity<>(seatService.getSeatsByFloor(floor), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * check in/out to seat
     * @param seatReservation
     * @return "체크아웃 성공" or "잘못된 요청입니다" as String
     */
    @PostMapping("/checkinout")
    public ResponseEntity<String> checkInAndOut(@RequestBody SeatReservation seatReservation) {
        try {
            return new ResponseEntity<>(seatService.checkInAndOut(seatReservation), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Seat not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
