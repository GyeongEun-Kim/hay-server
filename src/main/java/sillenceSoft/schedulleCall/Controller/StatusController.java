package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Service.ScheduleService;
import sillenceSoft.schedulleCall.Service.StatusService;

import javax.servlet.http.HttpServletRequest;
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

    @GetMapping(value = "/status-memo")
    public ResponseEntity getAllStatusMemo (HttpServletRequest request) {
        List<Map<String, String>> allStatusMemo = statusService.getAllStatusMemo(request);
        return new ResponseEntity(allStatusMemo, HttpStatus.OK);
    }

    @PostMapping("/status-memo")
    public ResponseEntity addStatusMemo (HttpServletRequest request, @RequestParam(name = "status") String newStatusMemo) {
        return statusService.addStatusMemo(request, newStatusMemo);
    }

    @DeleteMapping("/schedule")
    public String deleteSchedule () {
        return "";
    }
}
