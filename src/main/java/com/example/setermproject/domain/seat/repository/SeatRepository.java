package com.example.setermproject.domain.seat.repository;

import com.example.setermproject.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    public Optional<Seat> findByMemberId(Long memberId);

    @Query("SELECT s FROM Seat s WHERE s.floor = :floor AND s.status = 'available'")
    List<Seat> findAvailableSeatsByFloor(@Param("floor") Integer floor);
}
