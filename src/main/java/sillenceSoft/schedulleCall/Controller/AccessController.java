package sillenceSoft.schedulleCall.Controller;

import io.jsonwebtoken.Claims;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Service.AccessService;
import sillenceSoft.schedulleCall.Service.JWTProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class AccessController {
    private final AccessService accessService;
    private final JWTProvider jwtProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    //디폴트 : 모두 숨김
    @PostMapping(value = "/access") // 숨김 해제
    public ResponseEntity canAccess (HttpServletRequest request, Authentication authentication,
                                     @RequestParam(name = "accessUserPhone") String accessUserPhone) {

        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = accessService.canAccess(userNo, accessUserPhone);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @DeleteMapping(value = "/access") //숨김
    public ResponseEntity cannotAccess (HttpServletRequest request, Authentication authentication,
                                @RequestParam(name = "accessUserPhone") String accessUserPhone)
    {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = accessService.cannotAccess(userNo, accessUserPhone);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @GetMapping(value = "/access")
    public ResponseEntity getAccessList(HttpServletRequest request, Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = accessService.getAccessList(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }
}
