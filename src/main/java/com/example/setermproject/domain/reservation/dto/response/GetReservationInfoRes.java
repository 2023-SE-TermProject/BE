package com.example.setermproject.domain.reservation.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GetReservationInfoRes {
    private Long idx;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String roomNumber;
}
