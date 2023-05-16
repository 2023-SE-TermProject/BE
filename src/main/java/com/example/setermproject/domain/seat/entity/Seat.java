package com.example.setermproject.domain.seat.entity;

import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.seat.entity.vo.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Integer floor;

    @Column
    private Integer seatNumber;

    @Column
    private Long memberId;

    @Column
    private SeatStatus status;

}
