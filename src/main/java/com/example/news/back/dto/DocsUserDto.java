package com.example.news.back.dto;

import com.example.news.back.entity.DocsUser;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data // Getter, Setter, toString(), equals(), hasCode() 등 자주 쓰는 메서드를 한 번에 만들어줌.
public class DocsUserDto {
    private Long userId;
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String createdAt;

    // entity 를 dto로 변환하는 static 메서드
    public static DocsUserDto toDto(DocsUser docsUser) {
        // 매개변수에 전달되는 DocuUser entity 객체에 담긴 내용을 이용해서 DocuUserDto 객체를 만들어서 리턴함
        return DocsUserDto.builder()
                .userId(docsUser.getUserId())
                .name(docsUser.getName())
                .nickname(docsUser.getNickname())
                .email(docsUser.getEmail())
                .password(docsUser.getPassword())
                .createdAt(String.valueOf(docsUser.getCreatedAt()))
                .build();
    }

}
