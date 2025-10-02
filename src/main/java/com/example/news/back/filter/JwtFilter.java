package com.example.news.back.filter;

import com.example.news.back.service.CustomDocuUserDetailService;
import com.example.news.back.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

// 매 요청마다 토큰 확인하고 시큐리티 인증에 정보 넣어줌
// 요청 헤더에 토큰이 있다면 유틸로 보내서 검증 시작
// 토큰의 유무만 확인

/*


 */
@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    JwtUtil jwtUtil;

    // 쿠키에 저장된 token의 이름
    @Value("${jwt.name}")
    String jwtName;

    @Autowired
    CustomDocuUserDetailService service;

    // 모든 요청이 올 때 토큰을 추출
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        System.out.println("요청경로:"+path);

        //  회원가입, 로그인은 JWT 검증 안 하고 그냥 통과
        if (path.startsWith("/docs/join")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = "";

        // 쿠키에서 토큰 추출
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                // custum.properties 파일에 설정된  "jwtToken" 이라는 쿠키이름으로 저장된 value 가 있는지 확인해서
                if (jwtName.equals(cookie.getName())) {
                    //있다면 그 value 값을 디코딩해서 지역변수에 담기
                    jwtToken = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    break;
                }
            }
        }


        // 만일 쿠키에서 추출된 토큰이 없다면
        if (jwtToken.equals("")) {
            //요청의 Header 에 "Authorization" 이라는 키값으로 전달된 문자열이 있는지 읽어와 본다. ?
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                jwtToken = URLDecoder.decode(authHeader, StandardCharsets.UTF_8);
            }
        }

        String userName = null;
        if (jwtToken.startsWith("Bearer ")) {
            // "Bearer " 를 제외한 뒤의 token 문자열을 얻어낸다.
            jwtToken = jwtToken.substring(7);
            // userName 을 token 으로 부터 얻어낸다.
            userName = jwtUtil.extractName(jwtToken);
        }
        //토큰에서 뽑은 userName
        //userName 이 존재하고  Spring Security 에서 아직 인증을 받지 않은 상태라면
        //DB를 찾지않고 토큰으로
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //토큰이 유효한 토큰인지 체크한 다음
            boolean isValid = jwtUtil.validateToken(jwtToken);
            //유효한다면 1회성 로그인 (spring security 를 통과할 로그인)을 시켜준다.
            if (isValid) {

                Claims claims = jwtUtil.extractAllClaims(jwtToken); // JWT에서 모든 정보 가져오기
                String role = claims.get("role", String.class);
                // userName 과 role 정보를 담은 UserDetails 객체를 만든다.
                UserDetails ud = User.withUsername(userName)
                        .password("") // 비밀번호는 필요없지만 null 인 상태면 builder 에서 에러 발생
                        .authorities(role)
                        .build();
                //사용자가 제출한 사용자 이름과 비밀번호와 같은 인증 자격 증명을 저장
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(ud, null,
                                ud.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //Security 컨텍스트 업데이트 (1회성 로그인)
                //토큰에서 userName 과 role 을 담아서 로그인 진행
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }
        }

        //다음 spring 필터 chain 진행하기
        filterChain.doFilter(request, response);
        //토큰이 유효하지 않다면 스프링은 응답하지 않음


    }
}
