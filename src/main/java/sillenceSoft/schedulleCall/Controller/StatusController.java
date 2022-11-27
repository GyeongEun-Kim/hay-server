package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Service.ScheduleService;
import sillenceSoft.schedulleCall.Service.StatusService;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
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

    @GetMapping(value = "/status")
    public ResponseEntity getAllStatus (Authentication authentication) {
        List<Map<String, String>> allStatusMemo = statusService.getAllStatus(authentication);
        return new ResponseEntity(allStatusMemo, HttpStatus.OK);
    }

    @PostMapping("/status")
    public ResponseEntity addStatus (Authentication authentication, @RequestParam(name = "status") String newStatusMemo) {
        return statusService.addStatus(authentication, newStatusMemo);
    }

    @DeleteMapping("/status")
    public String deleteStatus (Authentication authentication, @RequestParam(name = "statusNo") int statusNo) {

        statusService.deleteStatus(authentication,statusNo);
        return "success";
    }

    @PutMapping("/status")
    public String updateStatus (Authentication authentication, @RequestParam(name = "status") String status,
                                @RequestParam(name = "statusNo") int statusNo) {
        statusService.updateStatus(authentication, status, statusNo);
        return "success";
    }



}
