package sillenceSoft.schedulleCall.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Repository.AccessRepository;


import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessService {
    private final AccessRepository accessRepository;
    private final SHA_256 sha256;

    public ResponseEntity canAccess (Long userNo, String accessUserPhone) {
            try {
                String encrypt = sha256.encrypt(accessUserPhone);
                if (checkIfPresent(userNo, encrypt)>0) {
                    return new ResponseEntity("이미 숨김해제가 되어 있습니다",HttpStatus.CONFLICT);
                }
                else {
                    accessRepository.saveAccess(userNo, encrypt);
                    return new ResponseEntity("success", HttpStatus.OK);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    public ResponseEntity cannotAccess (Long userNo, String accessUserPhone) {
        try {
            String encrypt = sha256.encrypt(accessUserPhone);
            if (checkIfPresent(userNo, encrypt)==0) {
                return new ResponseEntity("이미 숨김이 되어 있습니다", HttpStatus.CONFLICT);
            }
            else {
                accessRepository.deleteAccess(userNo, encrypt);
                return new ResponseEntity("success", HttpStatus.OK);
            }
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

    public Long checkIfPresent (Long userNo, String accessUserPhone) {
        return accessRepository.checkAccessOrNot(userNo,accessUserPhone);
    }

}
