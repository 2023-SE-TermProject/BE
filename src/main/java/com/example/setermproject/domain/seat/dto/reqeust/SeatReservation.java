package com.example.setermproject.domain.seat.dto.reqeust;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatReservation {
    private Long seatId;
    private Long memberId;
}
