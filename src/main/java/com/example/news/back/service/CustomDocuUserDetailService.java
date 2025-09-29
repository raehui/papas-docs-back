package com.example.news.back.service;

import com.example.news.back.dto.DocsUserDto;
import com.example.news.back.entity.DocsUser;
import com.example.news.back.repository.DocsUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
    Spring Security가 로그인 처리 시 호출하는 메소드를 가지고 있는 서비스 클래스 정의하기
 */
@Service
public class CustomDocuUserDetailService implements UserDetailsService {
    @Autowired
    private DocsUserRepository repository;

    // name 을 전달하면 해당 user의 자세한 정보를 리턴하는 메소드
    // 엔티티에서 받아온 데이터
    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        DocsUser docsUser = repository.getDocsUserByName(name);

        // 만일 존재하지 않는 사용자라면
        if(docsUser == null){
            throw new UsernameNotFoundException("존재 하지 않는 사용자 입니다."+ name);
        }

        //권한 목록을 List 에 담아서  (지금은 1개 이지만)
        List<GrantedAuthority> authList=new ArrayList<>();
        //DB 에 저장된 role 정보에는 ROLE_ 가 붙어 있지 않다.
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails ud = new User(docsUser.getName(),docsUser.getPassword(),authList );

        return ud;
    }
}
