package com.example.setermproject.domain.seat.service;

import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.response.MemberSeatRes;
import com.example.setermproject.domain.seat.dto.reqeust.SeatReservation;
import com.example.setermproject.domain.seat.dto.response.SeatInfo;
import com.example.setermproject.domain.seat.entity.Seat;
import com.example.setermproject.domain.seat.entity.vo.SeatStatus;
import com.example.setermproject.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public MemberSeatRes findMemberSeat(Long memberId) {
        Optional<Seat> seat = seatRepository.findByMemberId(memberId);
        MemberSeatRes memberSeatRes;

        if(seat.isPresent()) {
            memberSeatRes = MemberSeatRes.builder()
                    .id(seat.get().getMemberId())
                    .seatNumber(seat.get().getSeatNumber())
                    .floor(seat.get().getFloor())
                    .build();
        }
        else {
            memberSeatRes = MemberSeatRes.builder()
                    .id(null)
                    .floor(null)
                    .seatNumber(null)
                    .build();
        }
        return memberSeatRes;
    }

    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatsByFloor(@PathVariable Integer floor) {
        return seatRepository.findByFloor(floor).stream().map(seat ->
            SeatInfo.builder()
                    .seatNumber(seat.getSeatNumber())
                    .isUsed(seat.getStatus() == SeatStatus.USED ? true : false)
                    .build()
        ).collect(Collectors.toList());
    }

    public String checkInAndOut(@RequestBody SeatReservation seatReservation) {
        Seat seat = seatRepository.findById(seatReservation.getSeatId())
                .orElseThrow(() -> new NotFoundException());

        Optional<Seat> memberSeat = seatRepository.findByMemberId(seatReservation.getMemberId());

        // 유저가 사용 중인 좌석이 있을 때
        if(memberSeat.isPresent()) {
            // 요청한 seat id가 사용 중인 좌석과 일치하면 체크아웃
            if(seat.getId() == memberSeat.get().getId()) {
                seat.setStatus(SeatStatus.UNUSED);
                seat.setMemberId(null);
                seatRepository.save(seat);
                return "체크아웃 성공";
            }
            // 요청한 seat id가 사용 중인 좌석과 일치하지 않으면 잘못된 요청
            else {
                return "잘못된 요청입니다.";
            }
        }
        // 유저가 사용 중인 좌석이 없을 때
        else {
            // 이미 사용 중인 좌석이면 잘못된 요청
            if(seat.getStatus().equals(SeatStatus.USED)) {
                return "잘못된 요청입니다.";
            }
            // 사용 중이 아니면 체크인
            else {
                seat.setMemberId(seatReservation.getMemberId());
                seat.setStatus(SeatStatus.USED);
                seatRepository.save(seat);
                return "체크인 성공";
            }
        }
    }
}

