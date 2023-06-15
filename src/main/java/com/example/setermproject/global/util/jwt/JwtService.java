package com.example.setermproject.global.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";
    private final MemberRepository memberRepository;

    /**
     * create AccessToken
     */
    public String createAccessToken(Long id, Role role) {
        Date now = new Date();
        return JWT.create() // return JWT token builder
                .withSubject(ACCESS_TOKEN_SUBJECT) // set subject of JWT as AccessToken
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod)) // set token expired time

                // add id and role as claim
                .withClaim("id", id.toString())
                .withClaim("role", role.getKey())
                .sign(Algorithm.HMAC512(secretKey)); // use HMAC512 algorithm for encryption
    }

    /**
     * create RefreshToken
     */
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * add AccessToken to header
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
    }

    /**
     * add AccessToken + RefreshToken to header
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    /**
     * get RefreshToken from header
     * delete "Bearer" from value
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * get AccessToken from header
     * delete "Bearer" from value
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * get id form AccessToken
     */
    public Optional<String> extractId(String accessToken) {
        try {
            System.out.println("-----------------------토큰 추출------------------------");
            System.out.println(accessToken);
            // return JWT verifier builder for verifying token
            System.out.println("id : " + JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim("id")
                    .asString());

            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim("id")
                    .asString());
        } catch (Exception e) {
            // 유효하지 않은 AccessToken
            System.out.println("토큰 검증 실패");
            return Optional.empty();
        }
    }

    /**
     * set AccessToken header
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * set RefreshToken header
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * update RefreshToken on DB
     */
    public void updateRefreshToken(Long id, String refreshToken) {
        memberRepository.findById(id)
                .ifPresentOrElse(
                        member -> member.updateRefreshToken(refreshToken),
                        () -> new Exception("일치하는 회원이 없습니다.")
                );
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            System.out.println("유효한 토큰");
            return true;
        } catch (Exception e) {
            System.out.println("유효하지 않은 토큰");
            return false;
        }
    }
}
