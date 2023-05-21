package com.example.setermproject.domain.reservation.repository;

import com.example.setermproject.domain.reservation.entity.MeetingRoom;
import com.example.setermproject.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public Boolean existsByMeetingRoomAndStartTimeGreaterThanEqualAndStartTimeLessThan(MeetingRoom meetingRoom, LocalDateTime start, LocalDateTime end);

    public Boolean existsByMeetingRoomAndEndTimeGreaterThanAndEndTimeLessThanEqual(MeetingRoom meetingRoom, LocalDateTime start, LocalDateTime end);

    public List<Reservation> findByMemberIdxAndEndTimeAfterOrderByStartTime(Long memberIdx, LocalDateTime time);

    public List<Reservation> findByMeetingRoomAndStartTimeBetween(MeetingRoom meetingRoom, LocalDateTime start, LocalDateTime end);

    public Optional<Reservation> findTopByMeetingRoomAndStartTimeGreaterThanOrderByStartTimeAsc(MeetingRoom meetingRoom, LocalDateTime start);
}
