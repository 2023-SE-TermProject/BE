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
    private String name;

    @Column
    private String email;

    @Column
    @Setter
    private String studentId;

    @Column
    private MemberRole role;

    @Builder
    public Member(String name, String email, String studentId) {
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.role = MemberRole.COMMON;
    }

    @Builder
    public Member(String name, String email) {
        this.name = name;
        this.email = email;
        this.role = MemberRole.COMMON;
    }
}
