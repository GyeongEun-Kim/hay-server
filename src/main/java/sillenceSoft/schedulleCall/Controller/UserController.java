package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.header.Header;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Service.GoogleUserInfo;
import sillenceSoft.schedulleCall.Service.KakaoUserInfo;
import sillenceSoft.schedulleCall.Service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class UserController {

    private final UserService userService;
    private final GoogleUserInfo googleUserInfo;
    private final KakaoUserInfo kakaoUserInfo;

    @PostMapping(value = "/login", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity login (@ModelAttribute UserRequestDto userRequestDto, HttpServletRequest request) throws NoSuchAlgorithmException {

        UserDto userDto = userService.login(userRequestDto);
        String sessionId = userService.setSession(request, userDto);
        System.out.println("sessionId = " + sessionId);
        //사용자 정보 받아와 DB저장 & 세션설정
        HttpHeaders header = new HttpHeaders();
       // header.set("Set-Cookie",sessionId=sessionId);
        return new ResponseEntity<UserDto>(userDto,header,HttpStatus.OK);
    }

    @GetMapping("/session-check") //세션연장 세션끊긴상태면 세션끊겼다고 알려서 로그인페이지로 다시 이동할수 있게
    public ResponseEntity sessionCheck (HttpServletRequest request) {
        return userService.sessionCheck(request);

    }




}
