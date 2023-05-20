package com.example.setermproject.domain.reservation.service;

import com.example.setermproject.domain.reservation.dto.request.PostReservationReq;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.entity.MeetingRoom;
import com.example.setermproject.domain.reservation.entity.Reservation;
import com.example.setermproject.domain.reservation.exception.MeetingRoomNotExistException;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.reservation.exception.ReservedMeetingRoomException;
import com.example.setermproject.domain.reservation.repository.MeetingRoomRepository;
import com.example.setermproject.domain.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    public Boolean reserveMeetingRoom(PostReservationReq postReservationReq) {

        MeetingRoom meetingRoom = meetingRoomRepository.findById(postReservationReq.getMeetingRoomIdx()).orElseThrow(() -> new MeetingRoomNotExistException());

        if (reservationRepository.existsByMeetingRoomAndEndTimeGreaterThanAndEndTimeLessThanEqual(meetingRoom, postReservationReq.getStart(), postReservationReq.getEnd()) ||
                reservationRepository.existsByMeetingRoomAndStartTimeGreaterThanEqualAndStartTimeLessThan(meetingRoom, postReservationReq.getStart(), postReservationReq.getEnd())) {
            throw new ReservedMeetingRoomException();
        }

        reservationRepository.save(Reservation.builder()
                        .meetingRoom(meetingRoom)
                        .memberIdx(postReservationReq.getMemberIdx())
                        .startTime(postReservationReq.getStart())
                        .endTime(postReservationReq.getEnd())
                .build());

        return true;
    }

    public Boolean deleteReservation(Long idx) {
        Reservation reservation = reservationRepository.findById(idx).orElseThrow(() -> new NotFoundException());
        reservationRepository.delete(reservation);
        return true;
    }

    @Transactional(readOnly = true)
    public List<GetReservationInfoRes> findMemberReservations(Long memberIdx) {
        List<Reservation> reservations = reservationRepository.findByMemberIdxAndEndTimeAfterOrderByStartTime(memberIdx, LocalDateTime.now());

        return reservations.stream().map(reservation -> reservation.toReservationInfo()).collect(Collectors.toList());
    }
}
