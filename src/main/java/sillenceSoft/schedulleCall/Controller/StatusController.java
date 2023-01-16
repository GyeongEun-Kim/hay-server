package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.AllStatus;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Dto.StatusResponseDto;
import sillenceSoft.schedulleCall.Service.JWTProvider;
import sillenceSoft.schedulleCall.Service.ScheduleService;
import sillenceSoft.schedulleCall.Service.StatusService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Getter
@Setter
public class StatusController {
    private final ScheduleService scheduleService;
    private final StatusService statusService;
    private final JWTProvider jwtProvider;

    @GetMapping(value = "/status")
    public ResponseEntity getAllStatus (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.getAllStatus(userNo);
    }

    @PostMapping("/status")
    public ResponseEntity addStatus (Authentication authentication, @RequestParam(name = "status") String newStatusMemo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.addStatus(userNo, newStatusMemo);
    }

    @DeleteMapping("/status")
    public ResponseEntity deleteStatus (Authentication authentication, @RequestParam(name = "statusNo") int statusNo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.deleteStatus(statusNo);

    }

    @PutMapping("/status")
    public ResponseEntity updateStatus (Authentication authentication, @RequestParam(name = "status") String status,
                                @RequestParam(name = "statusNo") int statusNo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.updateStatus(status, statusNo);

    }


    @GetMapping ("/status/others")
    public ResponseEntity getOthersStatus (Authentication authentication, @RequestParam(name = "phone", required = false) String phone) throws IOException {
        Integer thisUserNo = jwtProvider.getUserNo(authentication);
        if (phone!=null) //한명의 상태글 조회
            return statusService.getOthersStatus(thisUserNo, phone);
        else //내가 볼수 있는 모든 사람의 상태글 조회
            return statusService.getAllOthersStatus(thisUserNo);
    }




}
