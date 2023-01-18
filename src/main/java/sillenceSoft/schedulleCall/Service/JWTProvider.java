package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JWTProvider {
    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final UserRepository userRepository;

    private long AccessTokenValidTime= 60 * 1 * 60 * 1000; //1시간
   //private long AccessTokenValidTime= 1 * 1 * 60 * 1000;//1분
    private long RefreshTokenValidTime = 60* 24 *60 * 1000 * 30 * 2; //2개월

    // 객체 초기화, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 엑세스 토큰 생성
    public String createAccessToken(String id, String regTime) {
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
    public String createRefreshToken(String id, String regTime) {
        Claims claims = Jwts.claims().setSubject("JWT제목"); // JWT payload 에 저장되는 정보단위
        claims.put("id",id); // 정보는 key / value 쌍으로 저장된다.
        claims.put("regTime", regTime);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + RefreshTokenValidTime)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
    }


    // 토큰 해석 (유효한 토큰 일때만)
    public Claims validTokenAndReturnBody(String token) {
        Claims claims =null;
        try {
            claims= Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch(ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            e.printStackTrace();
           // throw new InvalidParameterException("유효하지 않은 토큰입니다");
        }
        return claims;
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

    public Optional<String> getAccessToken (HttpServletRequest request) {
        Optional<String> accessToken = Optional.ofNullable(request.getHeader("authorization"));
        return accessToken;
    }

    public Optional<String> getRefreshToken (HttpServletRequest request) {
        Optional<String> refreshToken = Optional.ofNullable(request.getHeader("refreshToken"));
        return refreshToken;
    }

    public Long getUserNo (Authentication authentication) {
        Claims principal = (Claims) authentication.getPrincipal();
        String id = (String)principal.get("id");
        Long userNo = userRepository.getUserNoById(id);
        return userNo;
    }




}