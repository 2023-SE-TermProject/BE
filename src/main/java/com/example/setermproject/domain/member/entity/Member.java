package com.example.setermproject.domain.member.entity;

import com.example.setermproject.domain.member.entity.vo.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * member entity definition
 */
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

    @Setter
    @Column
    private Role role;

    @Setter
    @Column
    private String refreshToken;

    @Builder
    public Member(String name, String email, String studentId, Role memberRole) {
        this.name = name;
        this.email = email;
        this.studentId = studentId;
        this.role = memberRole;
    }

    @Builder
    public Member(String name, String email, Role memberRole) {
        this.name = name;
        this.email = email;
        this.role = memberRole;
    }

    public void updateRefreshToken(String refreshToken) {
        setRefreshToken(refreshToken);
    }
}
