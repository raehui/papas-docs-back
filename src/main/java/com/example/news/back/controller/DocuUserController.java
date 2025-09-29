package com.example.news.back.controller;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.service.DocsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DocuUserController {

    @Autowired private DocsUserService docsUserService;

    // 회원가입
    @PostMapping("/docs/save")
    public ResponseEntity<String> save(@RequestBody DocsUserDto docsUserDto) {
        docsUserService.joinDocsUser(docsUserDto);
        return ResponseEntity.ok("회원을 성공적으로 저장했습니다.");
    }

}
