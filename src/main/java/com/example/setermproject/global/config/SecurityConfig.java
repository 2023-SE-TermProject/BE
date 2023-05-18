package com.example.setermproject.global.config;

import com.example.setermproject.global.auth.handler.OAuth2LoginFailureHandler;
import com.example.setermproject.global.auth.handler.OAuth2LoginSuccessHandler;
import com.example.setermproject.global.auth.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler successHandler;
    private final OAuth2LoginFailureHandler failureHandler;

    @Bean
    protected SecurityFilterChain config(HttpSecurity http) throws Exception {
        http.formLogin().disable();
        http.httpBasic().disable();
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/**").permitAll()
                /*
                .requestMatchers("/log-in/**").permitAll()
                .requestMatchers("/sign-up").hasRole("GUEST")
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/hello").permitAll()
                .anyRequest().hasRole("USER")

                 */
        );

        http.oauth2Login()
                .successHandler(successHandler) // 동의하고 계속하기를 눌렀을때 Handler 설정
                .failureHandler(failureHandler) // 로그인 실패시 Handler 설정
                .userInfoEndpoint().userService(oAuth2UserService); // CustomUserService 설정

        return http.build();
    }
}
