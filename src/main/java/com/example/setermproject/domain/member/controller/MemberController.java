package com.example.setermproject.domain.member.controller;

import com.example.setermproject.domain.member.dto.request.MemberSiginupReq;
import com.example.setermproject.domain.member.dto.response.MemberInfo;
import com.example.setermproject.domain.member.service.MemberService;
import com.example.setermproject.domain.reservation.dto.response.GetReservationInfoRes;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import com.example.setermproject.domain.seat.dto.response.MemberSeatRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    // dependency injection by spring container
    private final MemberService memberService;

    /**
     * register member information when first login
     * @param id
     * @param memberSiginupReq
     * @return true or false
     */
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

    /**
     * find seat that member is using
     * @param memberId
     * @return MemberSeatRes
     */
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

    /**
     * find member's reservation list
     * @param memberIdx
     * @return List<GetReservationInfoRes>
     */
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

    /**
     * find member information
     * @param id
     * @return MemberInfo
     */
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
