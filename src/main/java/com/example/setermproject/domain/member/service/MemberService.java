package com.example.setermproject.domain.member.service;

import com.example.setermproject.domain.member.dto.response.MemberInfo;
import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.reservation.service.ReservationService;
import com.example.setermproject.domain.seat.dto.response.MemberSeatRes;
import com.example.setermproject.domain.seat.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    // dependency injection by spring container
    private final MemberRepository memberRepository;
    private final SeatService seatService;
    private final ReservationService reservationService;

    /**
     * business login for registering member
     * @param id
     * @param studentId
     * @return true or false
     */
    public Boolean postMemberStudentId(Long id, String studentId) {
        // find member from database and throw custom exception if member not exist
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());
        //insert member information using jpa
        member.setStudentId(studentId);
        member.setRole(Role.USER);
        memberRepository.save(member);

        return true;
    }

    /**
     * find seat information that is using by member from database
     * @param id
     * @return MemberSeatRes
     */
    public MemberSeatRes findMemberSeat(Long id) {
        // find member from database and throw custom exception if member not exist
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        // find seat information
        return seatService.findMemberSeat(id);
    }

    /**
     * find reservation list of member
     * @param id
     * @return List<GetReservationInfoRes>
     */
    public List<GetReservationInfoRes> findMemberReservations(Long id) {
        // find member from database and throw custom exception if member not exist
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        // find reservation list of member
        return reservationService.findMemberReservations(id);
    }

    /**
     * find member information
     * @param id
     * @return MemberInfo
     */
    public MemberInfo findMember(Long id) {
        // find member from database and throw custom exception if member not exist
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());

        // build MemberInfo using member entity
        return MemberInfo.builder()
                .id(member.getId())
                .name(member.getName())
                .studentId(member.getStudentId())
                .build();
    }
}
