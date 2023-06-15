package com.example.setermproject.domain.reservation.entity;

import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.entity.vo.ReservationStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Reservation entity definition
 */
@Table
@Entity
@Getter
@NoArgsConstructor
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private Long memberIdx;

    @ManyToOne
    @JoinColumn
    private MeetingRoom meetingRoom;

    @Column
    private LocalDateTime startTime;

    @Column
    private LocalDateTime endTime;

    @Column
    @Setter
    private ReservationStatus status;

    public LocalDateTime getStartTime(){
        return this.startTime.plusHours(9);
    }

    public LocalDateTime getEndTime(){
        return this.endTime.plusHours(9);
    }

    @Builder
    private Reservation(Long memberIdx, MeetingRoom meetingRoom, LocalDateTime startTime, LocalDateTime endTime) {
        this.memberIdx = memberIdx;
        this.meetingRoom = meetingRoom;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = ReservationStatus.WAIT;
    }

    public GetReservationInfoRes toReservationInfo() {
        return GetReservationInfoRes.builder()
                .idx(this.idx)
                .startTime(getStartTime())
                .endTime(getEndTime())
                .roomNumber(this.meetingRoom.getRoomNumber())
                .build();
    }

}
