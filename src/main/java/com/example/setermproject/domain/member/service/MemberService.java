package com.example.setermproject.domain.member.service;

import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.domain.reservation.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Boolean postMemberStudentId(Long id, String studentId) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundException());
        member.setStudentId(studentId);
        member.setRole(Role.USER);
        memberRepository.save(member);

        return true;
    }
}
