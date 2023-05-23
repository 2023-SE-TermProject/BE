package com.example.setermproject.domain.seat.dto.reqeust;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatReservation {
    private Long seatId;
    private Long memberId;
}
