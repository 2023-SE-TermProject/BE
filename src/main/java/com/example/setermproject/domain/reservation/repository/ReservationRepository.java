package com.example.setermproject.domain.reservation.repository;

import com.example.setermproject.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
