package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final UserService userService;
    private final UserRepository userRepository;
    private final JWTProvider jwtProvider;

    public List<Map<String,String>> getAllStatus (HttpServletRequest request) {

        String accessToken = jwtProvider.getAccessToken(request);
        String id = (String)jwtProvider.validTokenAndReturnBody(accessToken).get("id");
        int userNo = userRepository.getUserNoById(id);
        List<Map<String,String>> allStatus = statusRepository.getAllStatus(userNo);
        return allStatus;
    }

    public ResponseEntity addStatus (HttpServletRequest request, String newStatusMemo) {
        String accessToken = jwtProvider.getAccessToken(request);
        String id = (String)jwtProvider.validTokenAndReturnBody(accessToken).get("id");
        int userNo = userRepository.getUserNoById(id);
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


    public void deleteStatus ( int statusNo) {
//        String accessToken = jwtProvider.getAccessToken(request);
//        String id = (String)jwtProvider.validTokenAndReturnBody(accessToken).get("id");
        //int userNo = userRepository.getUserNoById(id);
        try {
            statusRepository.deleteStatus(statusNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateStatus ( String status, int statusNo) {
        statusRepository.updateStatus(status,statusNo, LocalDateTime.now());
    }



}
