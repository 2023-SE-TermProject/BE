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

    // dependency injection by spring container
    private final SeatRepository seatRepository;

    /**
     * find seat that is using by member
     * @param memberId
     * @return
     */
    @Transactional(readOnly = true)
    public MemberSeatRes findMemberSeat(Long memberId) {
        // find seat entity that is using by member
        Optional<Seat> seat = seatRepository.findByMemberId(memberId);
        MemberSeatRes memberSeatRes;

        // if entity exist, build dto using entity
        if(seat.isPresent()) {
            memberSeatRes = MemberSeatRes.builder()
                    .id(seat.get().getMemberId())
                    .seatNumber(seat.get().getSeatNumber())
                    .floor(seat.get().getFloor())
                    .build();
        }
        // if it is not, build dto with null value
        else {
            memberSeatRes = MemberSeatRes.builder()
                    .id(null)
                    .floor(null)
                    .seatNumber(null)
                    .build();
        }
        return memberSeatRes;
    }

    /**
     * find seat list of specific floor
     * @param floor
     * @return List<SeatInfo>
     */
    @Transactional(readOnly = true)
    public List<SeatInfo> getSeatsByFloor(@PathVariable Integer floor) {
        return seatRepository.findByFloor(floor).stream().map(seat ->
            SeatInfo.builder()
                    .seatNumber(seat.getSeatNumber())
                    .isUsed(seat.getStatus() == SeatStatus.USED ? true : false)
                    .build()
        ).collect(Collectors.toList());
    }

    /**
     * if member already checked in to target seat, do check out member on seat.
     * if member not check in to target seat, do check in member to seat.
     * @param seatReservation
     * @return result of check in/out as string
     */
    public String checkInAndOut(@RequestBody SeatReservation seatReservation) {
        // find seat entity from database. if not exist, throw custom exception
        Seat seat = seatRepository.findById(seatReservation.getSeatId())
                .orElseThrow(() -> new NotFoundException());

        // find seat entity that is checked in by target member
        Optional<Seat> memberSeat = seatRepository.findByMemberId(seatReservation.getMemberId());

        // if user is being checked in any seat
        if(memberSeat.isPresent()) {
            // if request seat id equals with seat id that is checked in by user, do check out
            if(seat.getId() == memberSeat.get().getId()) {
                seat.setStatus(SeatStatus.UNUSED);
                seat.setMemberId(null);
                seatRepository.save(seat);
                return "체크아웃 성공";
            }
            // else it is invalid request
            else {
                return "잘못된 요청입니다.";
            }
        }
        // else
        else {
            // if the seat is in using, it is invalid request
            if(seat.getStatus().equals(SeatStatus.USED)) {
                return "잘못된 요청입니다.";
            }
            // else, do check in
            else {
                seat.setMemberId(seatReservation.getMemberId());
                seat.setStatus(SeatStatus.USED);
                seatRepository.save(seat);
                return "체크인 성공";
            }
        }
    }
}

