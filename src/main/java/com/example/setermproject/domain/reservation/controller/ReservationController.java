package com.example.setermproject.domain.reservation.controller;

import com.example.setermproject.domain.reservation.dto.request.PostReservationReq;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.dto.response.RoomInfo;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.reservation.exception.ReservedMeetingRoomException;
import com.example.setermproject.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    // dependency injection by spring container
    private final ReservationService reservationService;

    /**
     * reserve meeting room
     * @param postReservationReq
     * @return true or false
     */
    @PostMapping
    public ResponseEntity<Boolean> reserveMeetingRoom(@RequestBody PostReservationReq postReservationReq) {
        try {
            return new ResponseEntity<Boolean>(reservationService.reserveMeetingRoom(postReservationReq), HttpStatus.OK);

        } catch (ReservedMeetingRoomException e) {
            return new ResponseEntity("Already Reserved Meeting Room.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
         return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * cancel reservation meeting room
     * @param idx of reservation
     * @return true or false
     */
    @DeleteMapping("/{reservation-id}")
    public ResponseEntity<Boolean> cancelReservation(@PathVariable("reservation-id") Long idx) {
        try {
            return new ResponseEntity<Boolean>(reservationService.deleteReservation(idx), HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity("Reservation not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find meeting room list
     * @return List<RoomInfo>
     */
    @GetMapping("/room-list")
    public ResponseEntity<List<RoomInfo>> findRoomList() {
        try {
            return new ResponseEntity<>(reservationService.findRoomInfo(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find valid reservation start time list of meeting room
     * @param roomIdx
     * @param year
     * @param month
     * @param date
     * @return List<LocalTime>
     */
    @GetMapping("/{room-id}")
    public ResponseEntity<List<LocalTime>> findValidReservationStartTime(@PathVariable("room-id") Long roomIdx,
                                                                         @RequestParam("year") Integer year,
                                                                         @RequestParam("month") Integer month,
                                                                         @RequestParam("date") Integer date) {
        try {
            return new ResponseEntity<>(reservationService.findValidStartTime(roomIdx, LocalDate.of(year, month, date)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Reservation not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * find valid time scope from reservation start time of meeting room
     * @param roomIdx
     * @param year
     * @param month
     * @param date
     * @param start
     * @return List<LocalTime>
     */
    @GetMapping("/{room-id}/valid-time")
    public ResponseEntity<List<LocalTime>> findValidReservationStartTime(@PathVariable("room-id") Long roomIdx,
                                                                         @RequestParam("year") Integer year,
                                                                         @RequestParam("month") Integer month,
                                                                         @RequestParam("date") Integer date,
                                                                         @RequestParam("start") Integer start
    ) {
        try {
            return new ResponseEntity<>(reservationService.findValidTimeRange(roomIdx, LocalDateTime.of(LocalDate.of(year, month, date), LocalTime.of(start, 0, 0, 0))), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Reservation not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
