package com.example.setermproject.domain.reservation.repository;

import com.example.setermproject.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    public Boolean existsByMeetingRoomIdxAndStartTimeGreaterThanAndStartTimeLessThan(Long roomIdx, LocalDateTime start, LocalDateTime end);

    public Boolean existsByMeetingRoomIdxAndEndTimeGreaterThanAndEndTimeLessThan(Long roomIdx, LocalDateTime start, LocalDateTime end);

    public List<Reservation> findByStudentIdxAndEndTimeAfterOrderByStartTime(Long memberIdx, LocalDateTime time);
}
