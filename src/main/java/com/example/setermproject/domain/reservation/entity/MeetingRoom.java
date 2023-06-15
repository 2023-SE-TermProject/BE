package com.example.setermproject.domain.reservation.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MeetingRoom entity definition
 */
@Table
@Entity
@Getter
@NoArgsConstructor
public class MeetingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomNumber;

    @Builder
    public MeetingRoom(String roomNumber) {
        this.roomNumber = roomNumber;
    }

}
