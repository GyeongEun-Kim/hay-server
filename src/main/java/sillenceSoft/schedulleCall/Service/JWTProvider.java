package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sillenceSoft.schedulleCall.Dto.UserDto;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JWTProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long AccessTokenValidTime= 60 * 30 * 1000; //30분
    private long RefreshTokenValidTime = 60* 60 * 24 * 30 * 2 * 1000; //2개월

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String id, LocalDateTime regTime) {
        Claims claims = Jwts.claims().setSubject("JWT제목"); // JWT payload 에 저장되는 정보단위
        claims.put("id",id); // 정보는 key / value 쌍으로 저장된다.
        claims.put("regTime", regTime.toString());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + AccessTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }
    // 리프레시 토큰 생성
    public String createRefreshToken(String id, LocalDateTime regTime) {
        Claims claims = Jwts.claims().setSubject("JWT제목"); // JWT payload 에 저장되는 정보단위
        claims.put("id",id); // 정보는 key / value 쌍으로 저장된다.
        claims.put("regTime", regTime.toString());
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + RefreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }


    // 토큰 해석
    public Claims validTokenAndReturnBody(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new InvalidParameterException("유효하지 않은 토큰입니다");
        }
    }

    public Authentication getAuthentication (String token) {
        Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody();
        return new UsernamePasswordAuthenticationToken(claims,null);
    }


    public void setHeaderAccessToken (String accessToken, HttpServletResponse response) {
        response.setHeader("authorization", "bearer "+ accessToken);
    }

    public void setHeaderRefreshToken (String refreshToken, HttpServletResponse response) {
        response.setHeader("refreshToken", "bearer "+ refreshToken);
    }

    public String getAccessToken (HttpServletRequest request) {
        return request.getHeader("authorization").substring(7);
    }

    public String getRefreshToken (HttpServletRequest request) {
        return request.getHeader("refreshToken").substring(7);
    }




}