package sillenceSoft.schedulleCall.Service;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public interface OAuthUserInfo {
    String getUserInfo (String token);
    void checkTokenValidity();
    void extendTokenTime();
}
