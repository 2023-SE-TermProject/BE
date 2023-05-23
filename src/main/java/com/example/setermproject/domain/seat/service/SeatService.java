package com.example.setermproject.domain.seat.service;

import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import com.example.setermproject.domain.seat.dto.SeatReservation;
import com.example.setermproject.domain.seat.entity.Seat;
import com.example.setermproject.domain.seat.entity.vo.SeatStatus;
import com.example.setermproject.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

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

    public List<Seat> getSeatsByFloor(@PathVariable Integer floor) {
        return seatRepository.findAvailableSeatsByFloor(floor);
    }

    public Boolean reserveSeat(@RequestBody SeatReservation seatReservation) {
        Seat seat = seatRepository.findById(seatReservation.getSeatId())
                .orElseThrow(() -> new NotFoundException());

        if (seat.getStatus().equals(SeatStatus.UNUSED)) {
            seat.setStatus(SeatStatus.USED);
            seat.setMemberId(seatReservation.getUserId());
            seatRepository.save(seat);
            return true;
        } else {
            return false;
        }
    }
}

