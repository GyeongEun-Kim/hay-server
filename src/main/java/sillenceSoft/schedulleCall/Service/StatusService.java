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


    public ResponseEntity getOthersStatus (Integer userNo, String phone, HttpServletResponse res) throws IOException {
        //userNo유저가  phone유저의 상태를 보려고 하는상황
        Integer check = accessRepository.checkAccessOrNot(sha256.encrypt( phone), userNo);
        //check가 null이면 access불가
        boolean statusOn =userRepository.getStatusOn(userRepository.findByPhone(phone));

        if (check != null && statusOn==true) { //상태표시 가능이고, 유저가 숨김해제 돼있으면 조회가능!
            StatusResponseDto result = userRepository.getNowStatusAndPhone(check);
            if (result == null)  {
                return new ResponseEntity("사용자의 현재 상태가 존재하지 않습니다", HttpStatus.NO_CONTENT);
            }
            else return new ResponseEntity(result,HttpStatus.OK);
        } else { //접근 자체가 불가할때
            return new ResponseEntity("사용자의 상태글에 접근 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity getAllOthersStatus (Integer thisUserNo) {
        try {
            List<StatusResponseDto> allOthersStatus = statusRepository.getAllOthersStatus(thisUserNo);
            return new ResponseEntity(allOthersStatus,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
