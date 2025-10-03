package com.example.news.back.entity;

import com.example.news.back.dto.DocsUserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 인자로 받는 생성자
@Getter // 모든 필드에 대해 Getter 메서드 자동 생성
@Setter // 모든 필드에 대해 Setter 메서드 자동 생성
@Builder // 빌더 패턴으로 객체 생성 가능
@Entity
@Table(name="docs_user")
public class DocsUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    
    // id라고 가정
    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    // 가입날짜
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // dto을 entity 로 변환하는 static 메서드
    // 서비스에서 DB로 보낼 때 DTO-> entity
    // 클라이언트 -> DB 로의 방향에만 사용된다.
    public static DocsUser toEntity(DocsUserDto docsUserDto) {
        return DocsUser.builder()
                //.userId(docsUserDto.getUserId()) DB에서 자동 생성
                .name(docsUserDto.getName())
                .nickname(docsUserDto.getNickname())
                .email(docsUserDto.getEmail())
                .password(docsUserDto.getPassword())
                //.createdAt(LocalDateTime.parse(docsUserDto.getCreatedAt())) DB의 DEFAULT CURRENT_TIMESTAMP 사용
                .build();
    }



}
