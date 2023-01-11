package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.JwtAuthenticationFilter;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserService {

    private final SHA_256 sha256;
    private final UserRepository userRepository;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;
    private final JWTProvider jwtProvider;
    private final JwtAuthenticationFilter filter;

    public UserDto login(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {
        //String id = sha256.encrypt(getIdBySocial(userRequestDto));
        String id = sha256.encrypt("qqqq"); //로컬테스트용
        UserDto userDto = userRepository.findById(id);

        if (userDto == null) { //신규회원인 경우
            userDto = UserDto.builder()
                    .id(id)
                    .phone(sha256.encrypt(userRequestDto.getPhone()))
                    .social(userRequestDto.getSocial())
                    .statusNo(null)
                    .regTime(LocalDateTime.now())
                    .statusOn(true)
                    .build();
            userRepository.login(userDto);//회원가입
        }

        return userDto;
    }

    public String getIdBySocial(UserRequestDto userRequestDto) {
        String id = "";
        if (userRequestDto.getSocial().equals("0")) { //구글로그인
            id = googleUserInfo.getUserInfo(userRequestDto.getToken());
        } else if (userRequestDto.getSocial().equals("1")) { //카카오로그인
            id = kakaoUserInfo.getUserInfo(userRequestDto.getToken());
        }
        //System.out.println("id = " + id);
        return id;
    }


    public String setNowStatus(Integer userNo, Integer statusNo) {
        String msg;
        try {
            Integer nowStatus = userRepository.getStatusNo(userNo);
            if (nowStatus == statusNo)
                userRepository.cancelNowStatus(userNo); //해제
            else
                userRepository.setNowStatus(userNo, statusNo);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "fail to set status";
        }
        return msg;

    }

    public void cancelNowStatus(Integer userNo) {
        userRepository.cancelNowStatus(userNo);
    }

    public void jwtCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = jwtProvider.getRefreshToken(request).orElse("empty");
        if (refreshToken.equals("empty")) { //엑세스 토큰 자체가 오지않은경우
            response.sendError(401, "리프레시 토큰을 보내주세요.");
        } else if (!refreshToken.equals("empty")) { //리프레시 토큰이 존재
            refreshToken = refreshToken.substring(7);
            if (jwtProvider.validTokenAndReturnBody(refreshToken) != null) {//유효하면
                Claims claims = jwtProvider.validTokenAndReturnBody(refreshToken);
                String id = (String) claims.get("id");
                String regTime = (String) claims.get("regTime");

                /// 토큰 발급 (access, refresh 둘다 재발급)
                String newAccessToken = jwtProvider.createAccessToken(id, regTime);
                String newRefreshToken = jwtProvider.createRefreshToken(id, regTime);
                /// 헤더에 엑세스, 리프레시 토큰 추가
                jwtProvider.setHeaderAccessToken(newAccessToken, response);
                jwtProvider.setHeaderRefreshToken(newRefreshToken, response);

                filter.setAuthentication(newAccessToken);
            } else { //리프레시 토큰 유효x
                response.sendError(401, "리프레시 토큰 만료. 로그인 페이지로 돌아갑니다.");

            }
        }
    }

    public Map<String,Object> getStatusOn (Integer userNo) {
        return  userRepository.getStatusOn(userNo);
    }
}
