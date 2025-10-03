package com.example.news.back.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/*
    로그인 성공 시:
    generateToken(...) → createToken(...) → (클라이언트에 토큰 반환)

    보호 API 접근 시:
    validateToken(token) → extractAllClaims(token) → (만료/issuer 확인) → OK면 다음 로직
 */

// 토큰 발급, 검증 함수 생성
// 토큰이 유효한지 확인
@Service
public class JwtUtil {
    // application.yml 에서 주입받는 값
    // 외부 설정 값을 주입
    @Value("${jwt.secret}") // JWT 서명(Signature)에 사용한 비밀키 (Base64 인코딩된 문자열)
    private String secretKey;

    @Value("${jwt.expiration}") // JWT 만료시간
    private long expiration;

    // 토큰을 발급하기 위한 재료 수집 느낌?
    // secretKey를 Key 객체로 변환
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    // 토큰에서 사용자 이름 추출
    // :: 파라미터로 들어온 객체의 메서드만 호출하는 경우 사용함.
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 토큰에서 만료일 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    // 토큰 안에 들어가있는 특정 정보 꺼내기
    // 토큰에서 특정 클래임(claim) 추출 -> claimsResolver 함수로 어떤 값이든 꺼낼 수 있음
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    // 토큰 안에 들어가있는 특정 정보 꺼내기
    // 토큰 전체 클레임 추출
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey()) //token 발급시 서명했던 키값(csutom.properties)도 일치하는지 확인도 된다. 서명했던 키값이 일치하지 않으면 token 생성 안됨
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    // 토큰이 만료되었는지 확인
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    // JWT 토튼 생성
    public String generateToken(String name, Map<String, Object> extraClaims) {
        Map<String, Object> claims = new HashMap<>(extraClaims);
        return createToken(claims, name);
    }
    // 토큰 생성 로직
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuer("your-issuer")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    // 토큰 검증 메서드
    // 토큰의 키값, issuer, role 검증
    // 회원가입 후 처음 로그인 시에는 실행되지 않음.
    public boolean validateToken(String token) {
        Claims claims = extractAllClaims(token);

        boolean isValid = !isTokenExpired(token) && "your-issuer".equals(claims.getIssuer());

        return isValid;
    }
}
