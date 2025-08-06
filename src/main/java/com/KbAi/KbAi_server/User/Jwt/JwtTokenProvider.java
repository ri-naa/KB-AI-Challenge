package com.KbAi.KbAi_server.User.Jwt;

import com.KbAi.KbAi_server.User.Service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "BEARER";
    private final Long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60L * 60L; //만료 1시간
    private final Long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60L * 60L * 24L * 7L; //만료 7일


    private final CustomUserDetailsService customUserDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    //객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    //JWT AccessToken 생성
    public String createAccessToken(String email, String role) {
        Claims claims = Jwts.claims().setSubject(email); //JWT payload에 저장되는 정보 단위
        claims.put("role", role); //정보는 key-value 쌍으로 저장
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)  //정보 저장
                .setIssuedAt(now)   //토큰 발행 시간정보
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_TIME)) //만료 시간 설정
                .signWith(SignatureAlgorithm.HS512, secretKey) //사용할 암호화 알고리즘, signature에 들어갈 secret 값 세팅
                .compact();
    }

    //Refresh Token 생성
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    //토큰에서 회원 정보(이메일) 추출
    public String getUserEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    //JWT 토큰에서 인증 정보 조회 - DB 조회 1번 일어남
    public Authentication getAuthentication(String token){
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    //Request Header에서 token값 가져오기. "X-ACCESS-TOKEN" : "TOKEN 값"
    public String resolveAccessToken(HttpServletRequest request){
        return request.getHeader("X-ACCESS-TOKEN");
    }

    public String resolveRefreshToken(HttpServletRequest request){
        return request.getHeader("X-REFRESH-TOKEN");
    }

    //Access Token의 만료일자 가져오기
    public Long getAccessTokenExpiration(String accessToken) {
        Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    //Access Token의 유효성 + 만료일자 확인
    public boolean validAccessToken(String accessToken){
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    //RefreshToken의 유효성 + 만료일자 확인
    public boolean validRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        }catch (ExpiredJwtException e){
            return true;
        }catch (Exception e){
            return false;
        }
    }

}

