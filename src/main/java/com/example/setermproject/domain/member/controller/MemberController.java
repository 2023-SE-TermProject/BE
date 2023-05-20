package com.example.setermproject.domain.member.controller;

import com.example.setermproject.domain.member.service.MemberService;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/sign-up/{member-id}")
    public ResponseEntity<Boolean> registerMember(@PathVariable("member-id") Long id, @RequestBody String studentId) {
        try {
            return new ResponseEntity<>(memberService.postMemberStudentId(id, studentId),HttpStatus.OK);
        } catch (NotFoundException e){
            return new ResponseEntity("Member not exist.", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity("Unknown Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
