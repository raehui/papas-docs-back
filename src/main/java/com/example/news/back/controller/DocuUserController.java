package com.example.news.back.controller;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.service.DocsUserService;
import com.example.news.back.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
public class DocuUserController {

    @Autowired private DocsUserService docsUserService;
    @Autowired JwtUtil jwtUtil;



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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Map> login(@RequestBody DocsUserDto docsUserDto) {
        // 이메일로 사용자 검색
        docsUserService.loginDocsUser(docsUserDto);
        Map<String, Object> body = new HashMap<>();
        body.put("message", "회원가입 완료!");
        body.put("status", "success");
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }






}
