package sillenceSoft.schedulleCall.Service;

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

    public UserDto login(UserRequestDto userRequestDto) throws NoSuchAlgorithmException {
        String id = sha256.encrypt(getIdBySocial(userRequestDto));
        //String id = sha256.encrypt("test"); //로컬테스트용
        UserDto userDto = userRepository.findById(id);
        
        if (userDto==null) { //신규회원인 경우
            userDto = UserDto.builder()
                    .id(id)
                    .phone(sha256.encrypt(userRequestDto.getPhone()))
                    .social(userRequestDto.getSocial())
                    .nowStatus("")
                    .regTime(LocalDateTime.now())
                    .build();
             userRepository.login(userDto);//회원가입
            }

        return userDto;
            }

        public String setSession (HttpServletRequest request, UserDto userDto) {

            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(7200); //2시간
            session.setAttribute("id",userDto.getId());
            return session.getId();
        }

        public String getIdBySocial ( UserRequestDto userRequestDto) {
            String id = "";
            if (userRequestDto.getSocial().equals(0)) { //구글로그인
                id = googleUserInfo.getUserInfo(userRequestDto.getToken());
            }
            else if (userRequestDto.getSocial().equals(1)) { //카카오로그인
                id = kakaoUserInfo.getUserInfo(userRequestDto.getToken());
            }
            return id;
        }

        public ResponseEntity sessionCheck (HttpServletRequest request) {
            HttpSession session = request.getSession();
            Cookie[] cookies = request.getCookies();
            String JSESSIONID ="";
            for (Cookie c : cookies) {
                if (c.getName().equals("JSESSIONID")) JSESSIONID=c.getValue() ;
            }
            if(session.getId().equals(JSESSIONID)) {
                session.setMaxInactiveInterval(7200);
                return new ResponseEntity("success",HttpStatus.OK);
            }
            else { //세션끊김 -> 재로그인
                return new ResponseEntity("session disconnected. Please login again",HttpStatus.UNAUTHORIZED);
            }
        }



    }
