package com.example.setermproject.domain.reservation.repository;

import com.example.setermproject.domain.reservation.entity.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {
}
