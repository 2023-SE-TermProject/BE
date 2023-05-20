package com.example.setermproject.domain.member.service;

import com.example.setermproject.domain.member.dto.response.MemberInfo;
import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.reservation.service.ReservationService;
import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import com.example.setermproject.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final SeatService seatService;
    private final ReservationService reservationService;

    public Boolean postMemberStudentId(Long id, String studentId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());
        member.setStudentId(studentId);
        member.setRole(Role.USER);
        memberRepository.save(member);

        return true;
    }

    public MemberSeatRes findMemberSeat(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        return seatService.findMemberSeat(id);
    }

    public List<GetReservationInfoRes> findMemberReservations(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        return reservationService.findMemberReservations(id);
    }

    public MemberInfo findMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        return MemberInfo.builder()
                .id(member.getId())
                .name(member.getName())
                .studentId(member.getStudentId())
                .build();
    }
}
