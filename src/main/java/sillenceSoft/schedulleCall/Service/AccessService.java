package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.UserDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final AccessRepository accessRepository;
    private final UserRepository userRepository;

    public String canAccess (Authentication authentication, Integer allowUserNo) {
        Claims principal = (Claims) authentication.getPrincipal();
        String id = (String)principal.get("id");
        int userNo = userRepository.getUserNoById(id);
        accessRepository.saveAccess(userNo, allowUserNo);
        return "";
    }

    public String cannotAccess () {

        //accessRepository.delete();
        return "";
    }

    public List<UserDto> getAccessList(String id) {
        Integer userNo = userRepository.getUserNoById(id);
        return accessRepository.getAccessList(userNo);
    }

}
