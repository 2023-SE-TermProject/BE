package com.example.setermproject.domain.reservation.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoomInfo {
    private Long id;
    private String roomNumber;
}
