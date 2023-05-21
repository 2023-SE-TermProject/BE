package com.example.setermproject.domain.reservation.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GetReservationInfoRes {
    private Long idx;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String roomNumber;

    @Builder
    public GetReservationInfoRes(Long idx, LocalDateTime startTime, LocalDateTime endTime, String roomNumber) {
        this.idx = idx;
        this.startTime = startTime;
        this.endTime = endTime;
        this.roomNumber = roomNumber;
    }
}
