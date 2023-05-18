package com.example.setermproject.domain.seat.controller;

import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import com.example.setermproject.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // 회원이 이용 중인 좌석 조회
    @GetMapping("/member/{member-id}")
    public ResponseEntity<MemberSeatRes> findMemberUsingSeat(@PathVariable("member-id") Long memberId) {
        try {
            return new ResponseEntity<>(seatService.findMemberSeat(memberId), HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
