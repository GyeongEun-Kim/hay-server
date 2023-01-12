package sillenceSoft.schedulleCall.Controller;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Service.AccessService;
import sillenceSoft.schedulleCall.Service.JWTProvider;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class AccessController {
    private final AccessService accessService;
    private final JWTProvider jwtProvider;
    //디폴트 : 모두 숨김
    @PostMapping(value = "/access") // 숨김 해제
    public void canAccess (Authentication authentication,
                             @RequestParam(name = "accessUserPhone") String accessUserPhone) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        accessService.canAccess(userNo, accessUserPhone);
    }

    @DeleteMapping(value = "/access") //숨김
    public String cannotAccess (Authentication authentication,
                                @RequestParam(name = "accessUserPhone") String accessUserPhone)
    {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return accessService.cannotAccess(userNo, accessUserPhone);
    }

    @GetMapping(value = "/access")
    public Object getAccessList(Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return accessService.getAccessList(userNo);

    }
}
