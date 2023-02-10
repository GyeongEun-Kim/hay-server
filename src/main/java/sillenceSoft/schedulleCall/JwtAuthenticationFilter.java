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

        if ( accessToken.equals("empty")){ //엑세스 토큰 자체가 오지않은경우
            response.sendError(401, "엑세스 토큰을 보내주세요.");
        }

        else if (!accessToken.equals("empty")) { //토큰이 존재
            accessToken=accessToken.substring(7);
            if (jwtProvider.validTokenAndReturnBody(accessToken)!=null) {  // 엑세스 토큰이 유효한 상황
                System.out.println("엑세스토큰 유효");
                this.setAuthentication(accessToken);
                jwtProvider.setHeaderAccessToken(accessToken, response);

                filterChain.doFilter(request, response);
            }
            // 엑세스 토큰이 만료된 상황  -> 리프레시 토큰을 보내라고 요청
            else if (jwtProvider.validTokenAndReturnBody(accessToken)==null){

                response.sendError(401, "엑세스 토큰 만료. 리프레시 토큰을 보내주세요.");

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
        return "/login".equals(path) || "/jwt-check".equals(path) || path.contains("/.well-known");
    }
    }

