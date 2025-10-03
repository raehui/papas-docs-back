package com.example.news.back.service;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.entity.DocsUser;
import com.example.news.back.repository.DocsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DocsUserServiceImpl implements DocsUserService {

    // @Autowired  = 스프링 컨데이너에 있는 해당 객체를 알아서 가지고 옴
    @Autowired private DocsUserRepository docsUserRepository;
    // 스프링 시큐리티에 빈으로 설정된 PasswordEncoder 객체 주입 받기
    @Autowired private PasswordEncoder passwordEncoder;

    // 회원가입 dto에 담긴 내용을 entity로 변환해서 저장
    @Override
    public void joinDocsUser(DocsUserDto docsUserDto) {
        // 저장할 때는 비빌번호를 암호화한 후에 저장해야 함
        String password =  passwordEncoder.encode(docsUserDto.getPassword());
        docsUserDto.setPassword(password);
        // dto를 entity로 변경해서 저장하기
        // save는 자동으로 제공되기에 따로 설정 필요 없음
        docsUserRepository.save(DocsUser.toEntity(docsUserDto));
    }
    
    // 로그인
    @Override
    public void loginDocsUser(DocsUserDto docsUserDto) {
        docsUserRepository.findByEmail(docsUserDto.getEmail());
    }



}
