package sillenceSoft.schedulleCall.Service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.StatusDto;
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
            responseEntity = new ResponseEntity("failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    public void deleteStatus (Integer userNo, int statusNo) {

        try {
            statusRepository.deleteStatus(statusNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus (Integer userNo, String status, int statusNo) {

        statusRepository.updateStatus(status,statusNo, LocalDateTime.now());
    }

    public void statusOn (Integer userNo) {

        userRepository.setStatusOn(userNo);
    }

    public void statusOff (Integer userNo) {

        userRepository.setStatusOff(userNo);
    }



}
