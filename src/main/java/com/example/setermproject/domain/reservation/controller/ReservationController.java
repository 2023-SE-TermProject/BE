package com.example.setermproject.domain.reservation.controller;

import com.example.setermproject.domain.reservation.dto.request.PostReservationReq;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.reservation.exception.ReservedMeetingRoomException;
import com.example.setermproject.domain.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    // 예약
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

    // 예약 취소
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


    // 예약 가능 시간 조회(요청을 어떻게..)
}
