package com.example.setermproject.domain.reservation.dto.request;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PostReservationReq {
    private Long memberIdx;
    private Long meetingRoomIdx;
    private LocalDateTime start;
    private LocalDateTime end;

    @Builder
    public PostReservationReq(Long memberIdx, Long meetingRoomIdx, LocalDateTime start, LocalDateTime end) {
        this.memberIdx = memberIdx;
        this.meetingRoomIdx = meetingRoomIdx;
        this.start = start;
        this.end = end;
    }
}
