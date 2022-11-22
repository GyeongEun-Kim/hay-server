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

    public List<Map<String,String>> getAllStatusMemo (HttpServletRequest request) {
        ResponseEntity responseEntity = userService.sessionCheck(request);
        List<Map<String,String>> allStatus = new ArrayList<>();
        if (responseEntity.getStatusCode()== HttpStatus.OK) {
            String id = (String) request.getSession().getAttribute("id");
            int userNo = userRepository.getUserNoById(id);
            allStatus = statusRepository.getAllStatus(userNo);
        }
        return allStatus;
    }

    public ResponseEntity addStatusMemo (HttpServletRequest request, String newStatusMemo) {
        ResponseEntity responseEntity = userService.sessionCheck(request);
        if (responseEntity.getStatusCode()== HttpStatus.OK) {
            String id = (String) request.getSession().getAttribute("id");
            int userNo = userRepository.getUserNoById(id);
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
        }
        return responseEntity;
    }

}
