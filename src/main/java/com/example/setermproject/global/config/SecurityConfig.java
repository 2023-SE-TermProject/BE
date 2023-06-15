package com.example.setermproject.global.config;

import com.example.setermproject.domain.member.entity.vo.Role;
import com.example.setermproject.domain.member.repository.MemberRepository;
import com.example.setermproject.global.auth.handler.OAuth2LoginFailureHandler;
import com.example.setermproject.global.auth.handler.OAuth2LoginSuccessHandler;
import com.example.setermproject.global.auth.filter.JwtAuthenticationProcessingFilter;
import com.example.setermproject.global.auth.service.CustomOAuth2UserService;
import com.example.setermproject.global.util.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // dependency injection by spring container
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler successHandler;
    private final OAuth2LoginFailureHandler failureHandler;
    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.formLogin().disable();
        http.httpBasic().disable();
        http.csrf().disable();
        http.cors();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // set access level of each uri
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                .requestMatchers("/oauth2/**").permitAll()
                .requestMatchers("/members/*/sign-up").hasRole(Role.GUEST.name())
                .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
                .anyRequest().hasRole(Role.USER.name())
        );

        // set oauth2 login attributes
        http.oauth2Login()
                .userInfoEndpoint().userService(oAuth2UserService)
                .and()// CustomUserService 설정
                .successHandler(successHandler) // 동의하고 계속하기를 눌렀을때 Handler 설정
                .failureHandler(failureHandler); // 로그인 실패시 Handler 설정


        http.addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
