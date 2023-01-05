package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.StatusRepository;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final AccessRepository accessRepository;
    private final SHA_256 sha256;


    public List<Map<String,String>> getAllStatus (Integer userNo) {

        List<Map<String,String>> allStatus = statusRepository.getAllStatus(userNo);
        return allStatus;
    }

    public ResponseEntity addStatus (Integer userNo, String newStatusMemo) {

        ResponseEntity responseEntity;
        StatusDto statusDto = StatusDto.builder()
                    .userNo(userNo)
                    .status(newStatusMemo)
                    .modDt(LocalDateTime.now())
                    .build();
        try {
            statusRepository.addStatus(statusDto);
            responseEntity = new ResponseEntity(statusDto, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            responseEntity = new ResponseEntity("fail to add status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    public String deleteStatus ( int statusNo) {
        String msg;
        try {
            statusRepository.deleteStatus( statusNo);
            msg="success";
        } catch (Exception e) {
            e.printStackTrace();
            msg="fail to delete status";
        }
        return msg;
    }

    public String  updateStatus (String status, int statusNo) {
        String msg;
        try {
            statusRepository.updateStatus(status, statusNo, LocalDateTime.now());
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to update status";
        }
        return msg;
    }

    public String statusOn (Integer userNo) {
        String msg;
        try {
            userRepository.setStatusOn(userNo);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to set auto status";
        }
        return msg;
    }

    public String statusOff (Integer userNo) {
        String msg;
        try {
            userRepository.setStatusOff(userNo);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to set auto status";
        }
        return msg;
    }

    public String getOthersStatus (Integer userNo, String phone) {
        //userNo유저가  phone유저의 상태를 보려고 하는상황
        Integer check = accessRepository.checkAccessOrNot(sha256.encrypt( phone), userNo);
        System.out.println("check="+check);
        String result;
        if (check != null) {
            result = statusRepository.getNowStatus(check);
            if (result == null) return "사용자의 현재 상태가 존재하지 않습니다";
            else return result;
        } else { //접근 자체가 불가할때
            return "사용자의 현재 상태가 존재하지 않습니다"; //숨김 당해서 안보이는거지만 그냥 상태가 없다고 출력함.
        }
    }



}
