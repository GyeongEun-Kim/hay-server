package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.http.HttpRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final SHA_256 sha256;
    private final UserRepository userRepository;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;
    private long AccessTokenValidTime= 60 * 30; //30분
    private long RefreshTokenValidTime = 60 * 60 * 24 * 60; //2개월

    public UserDto login(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {
        String id = sha256.encrypt(getIdBySocial(userRequestDto));
        //String id = sha256.encrypt("test"); //로컬테스트용
        UserDto userDto = userRepository.findById(id);

        if (userDto==null) { //신규회원인 경우
            userDto = UserDto.builder()
                    .id(id)
                    .phone(sha256.encrypt(userRequestDto.getPhone()))
                    .social(userRequestDto.getSocial())
                    .statusNo(-1)
                    .regTime(LocalDateTime.now())
                    .statusOn(true)
                    .build();
             userRepository.login(userDto);//회원가입
        }

        return userDto;
    }

    public String getIdBySocial ( UserRequestDto userRequestDto) {
        String id = "";
        if (userRequestDto.getSocial().equals("0")) { //구글로그인
            id = googleUserInfo.getUserInfo(userRequestDto.getToken());
        }
        else if (userRequestDto.getSocial().equals("1")) { //카카오로그인
            id = kakaoUserInfo.getUserInfo(userRequestDto.getToken());
        }
        System.out.println("id = " + id);
        return id;
    }

//        public String jwtCheck (String accessToken, String refreshToken) {
//            String newAccessToken="";
//            Claims accessClaims = jwtProvider.validTokenAndReturnBody(accessToken);
//            if (accessClaims.isEmpty()) { //엑세스 토큰이 만료되었으면
//                Claims refreshClaims = jwtProvider.validTokenAndReturnBody(refreshToken);
//                if (refreshClaims.isEmpty()) { //리프레시 토큰도 만료
//                    System.out.println("다시 로그인 페이지로");
//                }
//                else { //엑세스 토큰만 새로 발급
//                    newAccessToken = jwtProvider.createAccessToken((String)refreshClaims.get("id"),
//                            (String) refreshClaims.get("regTime"));
//                }
//                return newAccessToken;
//            }
//            else return accessToken;
//
//        }

    public void setNowStatus (Integer userNo, Integer statusNo) {

        userRepository.setNowStatus(userNo,statusNo);

    }



    }
