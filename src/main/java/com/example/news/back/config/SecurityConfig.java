package com.example.news.back.config;

import com.example.news.back.filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // 설정 클래스
@EnableWebSecurity // Spring Security를 활성화
@EnableMethodSecurity(securedEnabled = true) // Controller 메소드 단위로 권한 체크 가능
public class SecurityConfig {

    // jwt 를 쿠키로 저장할 때 쿠키의 이름
    @Value("${jwt.name}")
    private String jwtName;

    @Autowired
    private JwtFilter jwtFilter;

    /*
       1. 나의 서비스에 맞는 설정을 만들어 시큐리티 체인을 생성
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 인증 필요없는  경로
        String[] whiteList = {"/join/**", "/login"};

        httpSecurity
                // 동일한 도메인에서 오는 iframe 허용
                // H2-console 같은 DB 콘솔 허용
                // 이거는 H2 콘솔을 사용하기 위해 설정한건가?
                .headers(header ->
                        header.frameOptions(option -> option.sameOrigin())
                )
                // CSRF 보호 기능 비활성화 (API 서버 + JWT 방식에서는 끔)
                .csrf(csrf -> csrf.disable())
                // 경로별 권한 설정
                .authorizeHttpRequests(config ->
                        config
                                .requestMatchers(whiteList).permitAll()
                                .anyRequest().authenticated()

                )
                // 세션을 사용하지 않고, 각 요청마다 인증 정보를 확인하도록 설정
                // JWT 토큰을 사용하기 위해서 설정함
                .sessionManagement(config ->
                        config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JWTfilter 를 Spring Security 필터보다 미리 수행되게 하기
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        // 설정 정보를 가지고 있는 HttpSecurity 객체의 build() 메소드를 호출해서 객체 리런
        return httpSecurity.build();
    }

    // 비밀번호 암화화를 위한 빈 등록
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /*
       AuthenticationManager Bean 등록
       - 스프링 시큐리티가 로그인(Authentication) 시도를 처리할 때 필요
       - PasswordEncoder를 기반으로 유저 정보를 검증
     */

    // 스프링이 자동으로 UserDetailService 와 PasswordEncoder 조합해서 AuthenticationManager 생성
    // UserDetailService = DB에서 유저 정보 조회, PasswordEncoder = 비밀번호 암호화/검증
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }
}
