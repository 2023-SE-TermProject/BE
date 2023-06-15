package com.example.setermproject.global.auth.service;

import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.global.auth.CustomOAuth2User;
import com.example.setermproject.global.auth.OAuthAttributes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        /**
         * create DefaultOAuth2UserService object and return DefaultOAuth2User object by loadUser(userRequest)
         */
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName(); // key value on OAuth2 log-in
        Map<String, Object> attributes = oAuth2User.getAttributes(); // json value of userInfo provided by social log-in api

        // create OAuthAttributes object
        OAuthAttributes extractAttributes = OAuthAttributes.of(userNameAttributeName, attributes);

        Member createdMember = getMember(extractAttributes); // return User object by getUser()

        // create object by CustomOAuth2User implemented DefaultOAuth2User
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(createdMember.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdMember.getId(),
                createdMember.getEmail(),
                createdMember.getRole()
        );
    }

    /**
     * find member by email and return Member object
     * return if member exist otherwise crate new Member object
     */
    private Member getMember(OAuthAttributes attributes) {
        Member findMember = memberRepository.findByEmail(attributes.getOauth2UserInfo().getEmail()).orElse(null);

        if(findMember == null) {
            return saveUser(attributes);
        }
        return findMember;
    }

    /**
     * create member object by toEntity() method
     * save created member object to database
     */
    private Member saveUser(OAuthAttributes attributes) {
        Member createdMember = attributes.toEntity(attributes.getOauth2UserInfo());
        return memberRepository.save(createdMember);
    }
}
