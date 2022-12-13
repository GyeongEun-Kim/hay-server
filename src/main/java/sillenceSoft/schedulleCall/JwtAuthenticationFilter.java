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
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 헤더에서 JWT 를 받아옵니다.
        String accessToken = jwtProvider.getAccessToken(request).orElse("empty");
        String refreshToken = jwtProvider.getRefreshToken(request).orElse("empty");


        if ( accessToken.equals("empty") || refreshToken.equals("empty")){ //두 토큰 자체가 오지않은경우
            response.sendError(401, "토큰을 보내주세요.");
        }

        else if (!accessToken.equals("empty") && !refreshToken.equals("empty")) { //토큰이 존재
            accessToken=accessToken.substring(7);
            refreshToken=refreshToken.substring(7);
            if (jwtProvider.validTokenAndReturnBody(accessToken)!=null) {  // 엑세스 토큰이 유효한 상황
                System.out.println("엑세스토큰 유효");
                this.setAuthentication(accessToken);
                jwtProvider.setHeaderAccessToken(accessToken, response);
                jwtProvider.setHeaderRefreshToken(refreshToken,response);
                filterChain.doFilter(request, response);
            }
            // 엑세스 토큰이 만료된 상황 | 리프레시 토큰은 유효한 상황 -> 둘다 재발급
            else if (jwtProvider.validTokenAndReturnBody(accessToken)==null && jwtProvider.validTokenAndReturnBody(refreshToken)!=null) {
                Claims claims = jwtProvider.validTokenAndReturnBody(refreshToken);
                String id = (String)claims.get("id");
                String regTime=(String) claims.get("regTime");

                /// 토큰 발급 (access, refresh 둘다 재발급)
                String newAccessToken = jwtProvider.createAccessToken(id, regTime);
                String newRefreshToken = jwtProvider.createRefreshToken(id, regTime);
                /// 헤더에 엑세스, 리프레시 토큰 추가
                jwtProvider.setHeaderAccessToken(newAccessToken, response);
                jwtProvider.setHeaderRefreshToken(newRefreshToken,response);

                this.setAuthentication(newAccessToken);

                filterChain.doFilter(request, response);
                }
            else if (jwtProvider.validTokenAndReturnBody(accessToken)==null && jwtProvider.validTokenAndReturnBody(refreshToken)==null){
                //두 토큰 모두 유효하지 않음
                response.sendError(401, "토큰이 유효하지 않습니다. 로그인 페이지로 돌아갑니다.");
                }
            }

        }

    public void setAuthentication(String token) {
        // 토큰으로부터 유저 정보를 받아옵니다.
       // System.out.println("get authentication");
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

