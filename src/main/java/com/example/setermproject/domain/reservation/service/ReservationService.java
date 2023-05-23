package com.example.setermproject.domain.reservation.service;

import com.example.setermproject.domain.reservation.dto.request.PostReservationReq;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.dto.response.RoomInfo;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    private final static Integer RESERVATION_START_HOUR = 9;
    private final static Integer RESERVATION_END_HOUR = 18;

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
        List<Reservation> reservations = reservationRepository.findByMemberIdxAndEndTimeAfterOrderByStartTime(memberIdx, LocalDateTime.now().minusHours(9));

        return reservations.stream().map(reservation -> reservation.toReservationInfo()).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomInfo> findRoomInfo() {
        return meetingRoomRepository.findAll().stream().map(
                meetingRoom ->
                    RoomInfo.builder()
                        .roomNumber(meetingRoom.getRoomNumber())
                        .id(meetingRoom.getId())
                        .build()
        ).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LocalTime> findValidStartTime(Long roomIdx, LocalDate date) {
        MeetingRoom meetingRoom = meetingRoomRepository.findById(roomIdx).orElseThrow(() -> new MeetingRoomNotExistException());

        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(RESERVATION_START_HOUR - 9,0, 0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(RESERVATION_END_HOUR - 9,0, 0, 0));
        System.out.println(start);
        System.out.println(end);

        List<Reservation> reservations = reservationRepository.findByMeetingRoomAndStartTimeBetween(meetingRoom, start, end);

        List<LocalTime> validStartTimes = new ArrayList<>();

        int startHour = RESERVATION_START_HOUR;

        if(date.isEqual(LocalDate.now())) {
            startHour = LocalDateTime.now().getHour();
        }

        for(int i = startHour; i < RESERVATION_END_HOUR; i++) {
            validStartTimes.add(LocalTime.of(i, 0));
        }
        for(Reservation reservation : reservations) {
            for(int i = (reservation.getStartTime().getHour()); i < (reservation.getEndTime().getHour()); i++) {
                validStartTimes.remove(LocalTime.of(i,0));
                System.out.println("------------------- delete list : " + i);
            }
        }

        return validStartTimes;
    }

    public List<LocalTime> findValidTimeRange(Long roomIdx, LocalDateTime start) {
        MeetingRoom meetingRoom = meetingRoomRepository.findById(roomIdx).orElseThrow(() -> new MeetingRoomNotExistException());

        Optional<Reservation> reservation = reservationRepository.findTopByMeetingRoomAndStartTimeGreaterThanOrderByStartTimeAsc(meetingRoom, start.minusHours(9));

        int limit = RESERVATION_END_HOUR - start.getHour();
        int timeRange;
        if(reservation.isPresent()) {
            System.out.println("--------------------start get hour : " + start);
            System.out.println("--------------------reservation get hour : " + reservation.get().getStartTime());
            if(reservation.get().getStartTime().toLocalDate().isEqual(start.toLocalDate())) {
                int gap = (reservation.get().getStartTime().getHour()) - start.getHour();
                timeRange = Math.min(Math.min(gap, limit), 4);
            }
            else {
                timeRange = timeRange = Math.min(limit, 4);
            }
        }
        else {
            timeRange = Math.min(limit, 4);
        }

        List<LocalTime> validTimeRanges = new ArrayList<>();
        for(int i = 1; i <= timeRange; i++) {
            validTimeRanges.add(LocalTime.of(i, 0));
        }
        return validTimeRanges;
    }
}
