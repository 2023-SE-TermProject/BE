package com.example.setermproject.domain.member.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberDto {
    // member id
    private Long id;
    // member name
    private String name;
    // member email
    private String email;
}
