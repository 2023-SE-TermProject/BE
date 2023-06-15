package com.example.setermproject.global.auth.filter;

import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.global.util.jwt.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Jwt authorization filter
 * used when receiving request otherwise "/login" uri
 *
 * 1. if RefreshToken doesn't exist and AccessToken is valid -> authorization success
 * 2. if RefreshToken doesn't exist and AccessToken is invalid -> authorization fail, 403 ERROR
 * 3. if RefreshToken exist -> if RefreshToken is matched with Token value that is stored in database, then re-issue token
 *
 */
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/oauth2/authorization/**"; // "/oauth2/authorization/**"으로 들어오는 요청은 Filter 작동 X

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // call next filter on NO_CHECK_URL request
            return; // return for stopping filtering
        }

        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     * method for finding user information using RefreshToken and re-issuing AccessToken/RefreshToken
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        memberRepository.findByRefreshToken(refreshToken)
                .ifPresent(member -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(member);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(member.getId(), member.getRole()),
                            reIssuedRefreshToken);
                });
    }

    /**
     * method for re-issuing RefreshToken and updating RefreshToken on database
     */
    private String reIssueRefreshToken(Member member) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        member.updateRefreshToken(reIssuedRefreshToken);
        memberRepository.save(member);
        return reIssuedRefreshToken;
    }

    /**
     * method for checking AccessToken and authentication processing
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        System.out.println("access token 검사");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractId(accessToken)
                        .ifPresent(id -> memberRepository.findById(Long.parseLong(id))
                                .ifPresent(member -> this.saveAuthentication(member))));

        System.out.println("access token 검사 성공");


        filterChain.doFilter(request, response);
    }

    /**
     * method for approve authentication
     */
    public void saveAuthentication(Member myMember) {

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myMember.getId().toString())
                .password(myMember.getEmail())
                .roles(myMember.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        System.out.println("인증 객체 토큰 생성 및 저장 완료");
    }
}

