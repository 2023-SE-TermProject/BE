package com.example.setermproject.domain.seat.service;

import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import com.example.setermproject.domain.seat.entity.Seat;
import com.example.setermproject.domain.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
