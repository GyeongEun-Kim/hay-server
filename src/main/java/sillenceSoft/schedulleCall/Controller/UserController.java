package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import sillenceSoft.schedulleCall.Service.UserService;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/signup")
    public String signup () {
        return userService.signup();
    }

    @GetMapping(value = "/login")
    public String login () {
        return userService.login();
    }

    //로그아웃?
}
