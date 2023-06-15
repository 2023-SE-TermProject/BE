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

    // dependency injection by spring container
    private final ReservationRepository reservationRepository;
    private final MeetingRoomRepository meetingRoomRepository;

    // set valid reservation hour as constant
    private final static Integer RESERVATION_START_HOUR = 9;
    private final static Integer RESERVATION_END_HOUR = 18;

    /**
     * reserve meeting room
     * @param postReservationReq
     * @return true or false
     */
    public Boolean reserveMeetingRoom(PostReservationReq postReservationReq) {

        // find meeting room entity
        MeetingRoom meetingRoom = meetingRoomRepository.findById(postReservationReq.getMeetingRoomIdx()).orElseThrow(() -> new MeetingRoomNotExistException());

        // if reservation request is invalid throw exception
        if (reservationRepository.existsByMeetingRoomAndEndTimeGreaterThanAndEndTimeLessThanEqual(meetingRoom, postReservationReq.getStart(), postReservationReq.getEnd()) ||
                reservationRepository.existsByMeetingRoomAndStartTimeGreaterThanEqualAndStartTimeLessThan(meetingRoom, postReservationReq.getStart(), postReservationReq.getEnd())) {
            throw new ReservedMeetingRoomException();
        }

        // build reservation entity and save
        reservationRepository.save(Reservation.builder()
                        .meetingRoom(meetingRoom)
                        .memberIdx(postReservationReq.getMemberIdx())
                        .startTime(postReservationReq.getStart())
                        .endTime(postReservationReq.getEnd())
                .build());

        return true;
    }

    /**
     * delete reservation entity from database
     * @param idx
     * @return true or false
     */
    public Boolean deleteReservation(Long idx) {

        // find reservation entity, if entity not exist, throw custom exception
        Reservation reservation = reservationRepository.findById(idx).orElseThrow(() -> new NotFoundException());
        // delete reservation entity from database
        reservationRepository.delete(reservation);
        return true;
    }

    /**
     * find member's reservation list
     * @param memberIdx
     * @return List<GetReservationInfoRes>
     */
    @Transactional(readOnly = true)
    public List<GetReservationInfoRes> findMemberReservations(Long memberIdx) {
        // find member's reservation list after now
        List<Reservation> reservations = reservationRepository.findByMemberIdxAndEndTimeAfterOrderByStartTime(memberIdx, LocalDateTime.now().minusHours(9));

        // return reservation information list that transfer entity to dto
        return reservations.stream().map(reservation -> reservation.toReservationInfo()).collect(Collectors.toList());
    }

    /**
     * find meeting room list that exist currently
     * @return List<RoomInfo>
     */
    @Transactional(readOnly = true)
    public List<RoomInfo> findRoomInfo() {
        // return meeting room information list that transfer entity to dto
        return meetingRoomRepository.findAll().stream().map(
                meetingRoom ->
                    RoomInfo.builder()
                        .roomNumber(meetingRoom.getRoomNumber())
                        .id(meetingRoom.getId())
                        .build()
        ).collect(Collectors.toList());
    }

    /**
     * find valid reservation start time of meeting room
     * @param roomIdx
     * @param date
     * @return
     */
    @Transactional(readOnly = true)
    public List<LocalTime> findValidStartTime(Long roomIdx, LocalDate date) {
        // find meeting room entity, if entity not exist, throw custom exception
        MeetingRoom meetingRoom = meetingRoomRepository.findById(roomIdx).orElseThrow(() -> new MeetingRoomNotExistException());

        // set start, end time
        LocalDateTime start = LocalDateTime.of(date, LocalTime.of(RESERVATION_START_HOUR - 9,0, 0, 0));
        LocalDateTime end = LocalDateTime.of(date, LocalTime.of(RESERVATION_END_HOUR - 9,0, 0, 0));
        System.out.println(start);
        System.out.println(end);

        // find reservation list between start time and end time
        List<Reservation> reservations = reservationRepository.findByMeetingRoomAndStartTimeBetween(meetingRoom, start, end);

        // list for storing valid start times
        List<LocalTime> validStartTimes = new ArrayList<>();

        int startHour = RESERVATION_START_HOUR;

        if(date.isEqual(LocalDate.now())) {
            startHour = LocalDateTime.now().plusHours(1).getHour();
        }

        // calculate valid start time
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

    /**
     * find valid time that can choose as time scope
     * @param roomIdx
     * @param start
     * @return List<LocalTime>
     */
    public List<LocalTime> findValidTimeRange(Long roomIdx, LocalDateTime start) {
        // find meeting room entity, if entity not exist, throw custom exception
        MeetingRoom meetingRoom = meetingRoomRepository.findById(roomIdx).orElseThrow(() -> new MeetingRoomNotExistException());
        // find reservation list of meeting room after now
        Optional<Reservation> reservation = reservationRepository.findTopByMeetingRoomAndStartTimeGreaterThanOrderByStartTimeAsc(meetingRoom, start.minusHours(9));

        // calculate time scope that can be choosen of meeting room
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
