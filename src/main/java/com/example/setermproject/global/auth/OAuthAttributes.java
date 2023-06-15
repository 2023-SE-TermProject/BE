package com.example.setermproject.global.auth;

import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.global.auth.userinfo.GoogleOAuth2UserInfo;
import com.example.setermproject.global.auth.userinfo.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private String nameAttributeKey; // key value in OAuth2 log-in
    private OAuth2UserInfo oauth2UserInfo; // user information that do log-in by OAuth2

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oauth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public static OAuthAttributes of(String userNameAttributeName, Map<String, Object> attributes) {

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oauth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(OAuth2UserInfo oauth2UserInfo) {
        return Member.builder()
                .name(oauth2UserInfo.getName())
                .email(oauth2UserInfo.getEmail())
                .memberRole(Role.GUEST)
                .build();
    }
}