package com.example.setermproject.global.auth.handler;

import com.example.setermproject.domain.member.dto.MemberDto;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.global.auth.CustomOAuth2User;
import com.example.setermproject.global.util.jwt.JwtService;
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

/**
 * handler for success of log-in
 */
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    // dependency injection by spring container
    private final JwtService jwtService;
    private static final String targetUrl = "https://gcu-metaverse.shop/loginauth";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("로그인 성공 - memberId : " + authentication.getName());
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            ObjectMapper mapper = new ObjectMapper();
            String role;
            String target;

            String accessToken = jwtService.createAccessToken(oAuth2User.getId(), oAuth2User.getRole());

            // if role of member is guest, the member isn't registered
            if (oAuth2User.getRole() == Role.GUEST) {
                role = "guest";
            }
            else if(oAuth2User.getRole() == Role.ADMIN) {
                role = "admin";
            }
            else {
                role = "user";
            }
            target = UriComponentsBuilder
                    .fromHttpUrl(targetUrl)
                    .queryParam("role", role)
                    .queryParam("id", oAuth2User.getId())
                    .queryParam("token", accessToken)
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