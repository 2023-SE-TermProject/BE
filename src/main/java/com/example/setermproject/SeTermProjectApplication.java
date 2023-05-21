package com.example.setermproject;

import com.example.setermproject.domain.reservation.entity.MeetingRoom;
import com.example.setermproject.domain.reservation.repository.MeetingRoomRepository;
import com.example.setermproject.domain.seat.entity.Seat;
import com.example.setermproject.domain.seat.repository.SeatRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

@EnableJpaRepositories
@SpringBootApplication
public class SeTermProjectApplication {

    @Autowired private MeetingRoomRepository meetingRoomRepository;
    @Autowired private SeatRepository seatRepository;

    @PostConstruct
    public void init() {
        List<MeetingRoom> meetingRooms = new ArrayList<>();
        meetingRooms.add(MeetingRoom.builder().roomNumber("403A").build());
        meetingRooms.add(MeetingRoom.builder().roomNumber("403B").build());
        meetingRooms.add(MeetingRoom.builder().roomNumber("403C").build());
        meetingRooms.add(MeetingRoom.builder().roomNumber("404A").build());
        meetingRooms.add(MeetingRoom.builder().roomNumber("405B").build());
        meetingRooms.add(MeetingRoom.builder().roomNumber("406C").build());

        List<Seat> seats = new ArrayList<>();
        for(int floor = 2; floor <= 7; floor++) {
            if(floor == 6) continue;
            for(int i = 0; i < 15; i++) {
                seats.add(Seat.builder().floor(floor).seatNumber(i).build());
            }
        }

        meetingRoomRepository.saveAll(meetingRooms);
        seatRepository.saveAll(seats);
    }

    public static void main(String[] args) {
        SpringApplication.run(SeTermProjectApplication.class, args);
    }

}
