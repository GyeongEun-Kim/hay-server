package sillenceSoft.schedulleCall.Controller;

import com.sun.net.httpserver.Headers;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Repository.RefreshTokenRepository;
import sillenceSoft.schedulleCall.Service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
public class UserController {

    private final UserService userService;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;
    private final JWTProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StatusService statusService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login (HttpServletRequest req, @ModelAttribute UserRequestDto userRequestDto,HttpServletResponse response) throws NoSuchAlgorithmException {
        try {
            UserDto userDto = userService.login(userRequestDto); //유저 정보
            String accessToken = jwtProvider.createAccessToken(userDto.getUserNo(), userDto.getSocial());
            String refreshToken = jwtProvider.createRefreshToken(userDto.getUserNo(), userDto.getSocial());

            jwtProvider.setHeaderAccessToken(accessToken, response);
            jwtProvider.setHeaderRefreshToken(refreshToken, response);

            System.out.println("accessToken = " + accessToken);
            System.out.println("refreshToken = " + refreshToken);
            logger.info(req.getRequestURI()+" Http Status: "+HttpStatus.OK);
            return new ResponseEntity(userDto, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            logger.info(req.getRequestURI()+" Http Status: "+HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/jwt-check") //리프레시 토큰의 유효성 확인
    public ResponseEntity jwtCheck (HttpServletRequest request,HttpServletResponse response) throws IOException {

        ResponseEntity responseEntity = userService.jwtCheck(request, response);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @PostMapping("/nowStatus") //현재 상태글 변경
    public ResponseEntity setNowStatus (HttpServletRequest request,Authentication authentication,
                              @RequestParam(name = "statusNo") Long statusNo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = userService.setNowStatus(userNo, statusNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }


    @GetMapping("/user/status/show") //statusOn 이 1인지 0인지 확인. 공개상태인지 비공개인지
    public ResponseEntity getStatusOnOff (HttpServletRequest request,Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = userService.getStatusOn(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }

    @PostMapping("/user/status/show") //statusOn을 1또는 0으로 바꿈
    public ResponseEntity statusOn (HttpServletRequest request, Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = userService.statusShow(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    }
