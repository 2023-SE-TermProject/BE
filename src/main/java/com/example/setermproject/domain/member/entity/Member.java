package com.example.setermproject.domain.member.entity;

import com.example.setermproject.domain.member.entity.vo.MemberRole;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String googleId;

    @Column
    @Setter
    private String student_id;

    @Column
    private MemberRole role;

    @Builder
    public Member(String googleId) {
        this.googleId = googleId;
        this.role = MemberRole.COMMON;
    }
}
