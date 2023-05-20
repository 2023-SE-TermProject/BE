package com.example.setermproject.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfo {
    private Long id;
    private String name;
    private String studentId;

    @Builder
    public MemberInfo(Long id, String name, String studentId) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
    }
}
