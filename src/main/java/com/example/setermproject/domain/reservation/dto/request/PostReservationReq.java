package com.example.setermproject.domain.reservation.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PostReservationReq {
    private Long memberIdx;
    private Long meetingRoomIdx;
    private LocalDateTime start;
    private LocalDateTime end;
}
