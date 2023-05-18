package com.example.setermproject.domain.seat.repository;

import com.example.setermproject.domain.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    public Optional<Seat> findByMemberId(Long memberId);
}
