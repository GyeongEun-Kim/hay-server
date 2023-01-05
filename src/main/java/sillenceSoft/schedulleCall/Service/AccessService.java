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
    private final SHA_256 sha256;

    public String canAccess (Integer userNo, String accessUserPhone) {
        String msg;

        try {
            String encrypt = sha256.encrypt(accessUserPhone);
            Integer accessUserNo = userRepository.findByPhone(encrypt);
            System.out.println("accessUserNo="+ accessUserNo);
            accessRepository.saveAccess(userNo, accessUserNo);
            msg="success";
        }catch (Exception e ) {
            e.printStackTrace();
            msg="fail to allow access";
        }
        return msg;
    }

    public String cannotAccess (Integer userNo, String accessUserPhone) {
        String msg;
        try {
            String encrypt = sha256.encrypt(accessUserPhone);
            Integer accessUserNo =userRepository.findByPhone(encrypt);
            System.out.println("accessUserNo="+ accessUserNo);
            accessRepository.deleteAccess(userNo, accessUserNo);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to disallow access";
        }
        return msg;
    }

    public Object getAccessList(Integer userNo) {
        try {
            return accessRepository.getAccessList(userNo);
        }catch (Exception e) {
            return "fail to get access list";
        }

    }

}
