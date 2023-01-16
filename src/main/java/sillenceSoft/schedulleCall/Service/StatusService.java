package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.AllStatus;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Dto.StatusResponseDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.StatusRepository;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final SHA_256 sha256;


    public ResponseEntity getAllStatus (Integer userNo) {
        try {
            AllStatus allStatus = AllStatus.builder()
                    .allStatus(statusRepository.getAllStatus(userNo))
                    .StatusState(userRepository.getStatusState(userNo))
                    .build();

            Integer nowStatus = userRepository.getNowStatus(userNo);
            //현재 상태글 존재 여부

            for (Map m : allStatus.getAllStatus()) {
                if (nowStatus != null) {
                    if (m.get("statusNo").equals(nowStatus)) {
                        m.put("selected", true);
                    }
                }
            }
            return new ResponseEntity<AllStatus>(allStatus, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addStatus (Integer userNo, String newStatusMemo) {

        ResponseEntity responseEntity;
        StatusDto statusDto = StatusDto.builder()
                    .userNo(userNo)
                    .status(newStatusMemo)
                    .modDt(LocalDateTime.now())
                    .isFromSchedule(false)
                    .build();
        try {
            if (statusRepository.checkIfPresent(userNo,statusDto.getStatus())== 0) {
                statusRepository.addStatus(statusDto);
                responseEntity = new ResponseEntity(statusDto, HttpStatus.OK);
            }
            else {
                responseEntity = new ResponseEntity("이미 같은 상태글이 존재합니다.", HttpStatus.CONFLICT);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    public ResponseEntity deleteStatus ( int statusNo) {
        try {
            statusRepository.deleteStatus( statusNo);
            return new ResponseEntity("success", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity updateStatus (String status, int statusNo) {
        try {
            statusRepository.updateStatus(status, statusNo, LocalDateTime.now());
            return new ResponseEntity("success", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity getOthersStatus (Integer userNo, String accessUserPhone) throws IOException {
//        //userNo유저가  accessUserPhone유저의 상태를 보려고 하는상황

        //1. accessUser가 statusOn돼있어야함
        //2. accessUser가 userNo를 숨김해제해야함
        String encryptedUserPhone = sha256.encrypt(userRepository.getPhoneByUserNo(userNo));
        String encryptedAccessUserPhone = sha256.encrypt(accessUserPhone);

        Integer accessUserNo = userRepository.getUserNoByPhone(encryptedAccessUserPhone);
        boolean statusOn = userRepository.getStatusOn(accessUserNo);

        Integer check = accessRepository.checkAccessOrNot(userNo,encryptedAccessUserPhone );
        System.out.println("check = " + check);

        if (statusOn==true && check!=null) {
            StatusResponseDto nowStatusAndPhone = userRepository.getNowStatusAndPhone(accessUserNo);
            if (nowStatusAndPhone==null) return new ResponseEntity("사용자의 현재 상태글이 없습니다", HttpStatus.OK);
            else return new ResponseEntity(nowStatusAndPhone,HttpStatus.OK);
        }
        else {
            return new ResponseEntity("사용자의 상태글에 접근할 수 없습니다.",HttpStatus.OK);
        }

    }

    public ResponseEntity getAllOthersStatus (Integer thisUserNo) {
        try {
            String phone = userRepository.getPhoneByUserNo(thisUserNo);
            List<StatusResponseDto> allOthersStatus = statusRepository.getAllOthersStatus(phone);
            return new ResponseEntity(allOthersStatus,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
