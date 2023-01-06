package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.StatusDto;
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
        List<Map<String, String>> allStatusMemo = statusService.getAllStatus(userNo);
        return new ResponseEntity(allStatusMemo, HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity addStatus (Authentication authentication, @RequestParam(name = "status") String newStatusMemo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.addStatus(userNo, newStatusMemo);
    }

    @DeleteMapping("/status")
    public String deleteStatus (Authentication authentication, @RequestParam(name = "statusNo") int statusNo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        statusService.deleteStatus(statusNo);
        return "success";
    }

    @PutMapping("/status")
    public String updateStatus (Authentication authentication, @RequestParam(name = "status") String status,
                                @RequestParam(name = "statusNo") int statusNo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        statusService.updateStatus(status, statusNo);
        return "success";
    }

    @PostMapping("/statusOn")
    public String statusOn (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.statusOn(userNo);
    }

    @PostMapping("/statusOff")
    public String statusOff (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return statusService.statusOff(userNo);
    }

    @GetMapping ("/status/others")
    public List<Map<String,String>> getOthersStatus (Authentication authentication, @RequestParam(name = "phone", required = false) String phone, HttpServletResponse res) throws IOException {
        Integer thisUserNo = jwtProvider.getUserNo(authentication);
        if (phone!=null) {
            List<Map<String,String>> result = new ArrayList<>();
            result.add(statusService.getOthersStatus(thisUserNo, phone, res));
            return result;
        }
        else return statusService.getAllOthersStatus(thisUserNo);
    }



}
