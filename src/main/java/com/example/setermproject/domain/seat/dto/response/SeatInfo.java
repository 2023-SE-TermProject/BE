package com.example.setermproject.domain.seat.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatInfo {
    private Integer seatNumber;
    private Boolean isUsed;
}
