package com.example.setermproject.global.auth.jwt;

import com.example.setermproject.domain.member.dto.MemberDto;
import com.example.setermproject.domain.member.entity.Member;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.global.util.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@RequiredArgsConstructor
public class JwtAuthenticationFilter  {
// extends GenericFilterBean {
/*
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest)request).getHeader("Auth");

        if (token != null && jwtProvider.verifyToken(token)) {
            Long uid = jwtProvider.getUid(token);
            Member member = memberRepository.findById(uid).orElseThrow(() -> new RuntimeException());
           MemberDto memberDto = MemberDto.builder()
                   .id(member.getId())



            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        chain.doFilter(request, response);
    }

    public Authentication getAuthentication(UserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
    }

 */

}