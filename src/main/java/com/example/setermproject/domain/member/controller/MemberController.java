package com.example.setermproject.domain.member.controller;

import com.example.setermproject.domain.member.dto.request.MemberSiginupReq;
import com.example.setermproject.domain.member.dto.response.MemberInfo;
import com.example.setermproject.domain.member.service.MemberService;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.MemberSeatRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 최초 가입 회원 학번 입력
    @PatchMapping("/{member-id}/sign-up")
    public ResponseEntity<Boolean> registerMember(@PathVariable("member-id") Long id, @RequestBody MemberSiginupReq memberSiginupReq) {
        try {
            return new ResponseEntity<>(memberService.postMemberStudentId(id, memberSiginupReq.getStudentId()),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity("Member not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원이 이용 중인 좌석 조회
    @GetMapping("/{member-id}/seat")
    public ResponseEntity<MemberSeatRes> findMemberUsingSeat(@PathVariable("member-id") Long memberId) {
        try {
            return new ResponseEntity<>(memberService.findMemberSeat(memberId), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Member not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원 예약 내역 조회
    @GetMapping("/{member-id}/reservation")
    public ResponseEntity<List<GetReservationInfoRes>> findMemberReservations(@PathVariable("member-id") Long memberIdx) {
        try {
            return new ResponseEntity(memberService.findMemberReservations(memberIdx), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Member not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원 정보 조회
    @GetMapping("/{member-id}")
    public ResponseEntity<MemberInfo> findMember(@PathVariable("member-id") Long id) {
        try {
            return new ResponseEntity<>(memberService.findMember(id), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity("Member not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
