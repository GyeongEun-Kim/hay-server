package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/login")
    public String login (@RequestBody UserDto user) throws NoSuchAlgorithmException {
        return userService.login(user); //jwt반환
    }

    @GetMapping(value = "/session-check")
    public String checkSession (HttpServletRequest request) {
        HttpSession session = request.getSession();
        //session.
        return "";
    }
}
