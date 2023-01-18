package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.AccessListResponseDto;
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

    public ResponseEntity canAccess (Long userNo, String accessUserPhone) {
            try {
                String encrypt = sha256.encrypt(accessUserPhone);
                //중복체크 해야함
                accessRepository.saveAccess(userNo, encrypt);
                return new ResponseEntity("success",HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

    }

    public ResponseEntity cannotAccess (Long userNo, String accessUserPhone) {
        try {
            String encrypt = sha256.encrypt(accessUserPhone);
            accessRepository.deleteAccess(userNo, encrypt);
            return new ResponseEntity("success", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity getAccessList(Long userNo) {
        try {

            List<String> accessList = accessRepository.getAccessList(userNo);
            return new ResponseEntity(accessList,HttpStatus.OK);
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
