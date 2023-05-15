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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    public Boolean reserveMeetingRoom(PostReservationReq postReservationReq) {

        if (reservationRepository.existsByMeetingRoomIdxAndEndTimeGreaterThanAndEndTimeLessThan(postReservationReq.getMeetingRoomIdx(), postReservationReq.getStart(), postReservationReq.getEnd()) ||
                reservationRepository.existsByMeetingRoomIdxAndStartTimeGreaterThanAndStartTimeLessThan(postReservationReq.getMeetingRoomIdx(), postReservationReq.getStart(), postReservationReq.getEnd())) {
            throw new ReservedMeetingRoomException();
        }

        MeetingRoom meetingRoom = meetingRoomRepository.findById(postReservationReq.getMeetingRoomIdx()).orElseThrow(() -> new MeetingRoomNotExistException());
        reservationRepository.save(Reservation.builder()
                        .meetingRoom(meetingRoom)
                        .studentIdx(postReservationReq.getMemberIdx())
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

    public List<GetReservationInfoRes> findMemberReservations(Long memberIdx) {
        List<Reservation> reservations = reservationRepository.findByStudentIdxAndEndTimeAfterOrderByStartTime(memberIdx, LocalDateTime.now());

        return reservations.stream().map(reservation -> reservation.toReservationInfo()).collect(Collectors.toList());
    }
}
