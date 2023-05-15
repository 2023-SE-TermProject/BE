package com.example.setermproject.domain.reservation.entity;

import com.example.setermproject.domain.reservation.entity.vo.ReservationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Table
@Entity
@Getter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Long studentIdx;

    @Column
    private Long meetingRoomIdx;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    @Setter
    private ReservationStatus status;

    @Builder
    private Reservation(Long studentIdx, Long meetingRoomIdx, LocalDateTime startTime, LocalDateTime endTime) {
        this.studentIdx = studentIdx;
        this.meetingRoomIdx = meetingRoomIdx;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = ReservationStatus.WAIT;
    }
}
