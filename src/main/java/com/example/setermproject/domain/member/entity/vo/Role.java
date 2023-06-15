package com.example.setermproject.domain.member.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * define type of member roles
 */
@AllArgsConstructor
@Getter
public enum Role {
    GUEST("ROLE_GUEST"),
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String key;
}
