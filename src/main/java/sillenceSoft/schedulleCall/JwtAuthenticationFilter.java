package sillenceSoft.schedulleCall;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sillenceSoft.schedulleCall.Controller.UserController;
import sillenceSoft.schedulleCall.Service.JWTProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtProvider.getAccessToken(request);
        String refreshToken = jwtProvider.getRefreshToken(request);

        // 유효한 토큰인지 확인합니다.
        if (accessToken != null) {
            // 어세스 토큰이 유효한 상황
            if (jwtProvider.validTokenAndReturnBody(accessToken)!=null) {
                System.out.println("엑세스토큰 유효");
                this.setAuthentication(accessToken);
            }
            // 어세스 토큰이 만료된 상황 | 리프레시 토큰 또한 존재하는 상황
            else if (jwtProvider.validTokenAndReturnBody(accessToken)==null && refreshToken != null) {
                // 재발급 후, 컨텍스트에 다시 넣기
                /// 리프레시 토큰 검증
                Claims claims = jwtProvider.validTokenAndReturnBody(refreshToken);
                /// 리프레시 토큰 저장소 존재유무 확인
               // boolean isRefreshToken = jwtProvider.existsRefreshToken(refreshToken);
             //   if (validateRefreshToken && isRefreshToken) {
                String id = (String)claims.get("id");
                LocalDateTime regTime = (LocalDateTime) claims.get("regTime");
                System.out.println("id = " + id);
                System.out.println("regTime = " + regTime);
                /// 토큰 발급
                    String newAccessToken = jwtProvider.createAccessToken(id, regTime);
                    /// 헤더에 어세스 토큰 추가
                   jwtProvider.setHeaderAccessToken(newAccessToken, response);
                    /// 컨텍스트에 넣기
                System.out.println("리프레시토큰 유효, 엑세스 새로 발급");
                    this.setAuthentication(newAccessToken);
                }
            }
        System.out.println("필터 동작");
        filterChain.doFilter(request, response);
        }


     //SecurityContext 에 Authentication 객체를 저장합니다.
    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
        System.out.println("get authentication");
        Authentication authentication = jwtProvider.getAuthentication(token);
        // SecurityContext 에 Authentication 객체를 저장합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return "/login".equals(path);
    }
    }

