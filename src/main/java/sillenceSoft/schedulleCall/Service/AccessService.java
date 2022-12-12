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

    public String canAccess (Integer userNo, Integer accessUserNo) {

        accessRepository.saveAccess(userNo, accessUserNo);
        return "success";
    }

    public String cannotAccess (Integer userNo, Integer accessUserNo) {

        accessRepository.deleteAccess(userNo, accessUserNo);
        return "success";
    }

    public List<UserDto> getAccessList(Integer userNo) {

        return accessRepository.getAccessList(userNo);
    }

}
