package com.example.setermproject.global.auth.handler;

import com.example.setermproject.domain.member.dto.MemberDto;
import com.example.setermproject.domain.member.entity.vo.MemberRole;
import com.example.setermproject.global.auth.CustomOAuth2User;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private static final String targetUrl = "http://localhost:3000/oauth/redirect";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            ObjectMapper mapper = new ObjectMapper();
            String role;
            String target;

            // Member의 Role이 GUEST일 경우 처음 로그인 한 회원
            if (oAuth2User.getRole() == MemberRole.ROLE_GUEST) {
                role = "guest";
            }
            else if(oAuth2User.getRole() == MemberRole.ROLE_ADMIN) {
                role = "admin";
            }
            else {
                role = "user";
            }
            target = UriComponentsBuilder
                    .fromHttpUrl(targetUrl)
                    .queryParam("role", role)
                    .queryParam("id", oAuth2User.getId())
                    .queryParam("token", "token")
                    .build().toUriString();

            getRedirectStrategy().sendRedirect(request, response, target);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            MemberDto memberDto = MemberDto.builder()
                    .id(oAuth2User.getId())
                    .name(oAuth2User.getName())
                    .email(oAuth2User.getEmail())
                    .build();

            String result = mapper.writeValueAsString(memberDto);
            response.getWriter().write(result);

        } catch (Exception e) {
            throw e;
        }
    }
}