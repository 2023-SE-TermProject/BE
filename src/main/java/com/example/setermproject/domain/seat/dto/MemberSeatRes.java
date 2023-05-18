package com.example.setermproject.domain.seat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberSeatRes {
    private Long id;
    private Integer floor;
    private Integer seatNumber;
}
