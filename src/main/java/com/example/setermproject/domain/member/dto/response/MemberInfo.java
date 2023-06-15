package com.example.setermproject.domain.member.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfo {

    // member id
    private Long id;
    // member name
    private String name;
    // student number of member
    private String studentId;

    @Builder
    public MemberInfo(Long id, String name, String studentId) {
        this.id = id;
        this.name = name;
        this.studentId = studentId;
    }
}
