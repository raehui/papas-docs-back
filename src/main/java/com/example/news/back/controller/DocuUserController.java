package com.example.news.back.controller;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.service.DocsUserService;
import com.example.news.back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
public class DocuUserController {

    @Autowired
    private DocsUserService docsUserService;
    @Autowired
    JwtUtil jwtUtil;
    // SecurityConfig 클래스에서 Bean이 된 AuthenticationManager 객체 주입받기
    @Autowired
    AuthenticationManager authnManager;

    // 회원가입
    // RequestBody = 클라이언트가 보낸 JSON을 자바 객체로 변환
    // POST, PUT 요청의 Body에 담긴 데이터를 DTO/객체로 매핑시 사용
    @PostMapping("/join")
    public ResponseEntity<Map> join(@RequestBody DocsUserDto docsUserDto) {
        docsUserService.joinDocsUser(docsUserDto);
        // 응답을 JSON으로 내려주기
        Map<String, Object> body = new HashMap<>();
        body.put("message", "회원가입 완료!");
        body.put("status", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    // 인증 매니저 객체를 통한 로그인 작업
    // 토큰 응답 메서드
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<String> login(@RequestBody DocsUserDto docsUserDto) throws Exception {
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken logintoken =
                    new UsernamePasswordAuthenticationToken(docsUserDto.getEmail(), docsUserDto.getPassword());
            // 이메일, 비밀번호로 인증 매니저 객체 이용해서 인증 진행
            authentication = authnManager.authenticate(logintoken);
        } catch (Exception e) {
            // 예외가 발생하면 인증실패
            e.printStackTrace();
            // 401 UNAUTHORIZED 에러를 응답하면서 문자열 한 줄 보내기
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 실패");
        }

        // authentication 객체에는 인증된 사용자 정보가 들어 있음. name, role 등등
        GrantedAuthority authority = authentication.getAuthorities().stream().toList().get(0);
        System.out.println(authority.getAuthority());

        // ROLE_XXX 형식
        String role = authority.getAuthority();
        // role 키값에 담기
        Map<String, Object> claims = Map.of("role", role);

        // 예외가 발상하지 않고 여기까지 실행된다면 인증 통과
        String token = jwtUtil.generateToken(docsUserDto.getEmail(),claims);


        return ResponseEntity.ok("Bearer "+token);
    }





}
