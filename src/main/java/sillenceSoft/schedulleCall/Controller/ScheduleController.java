package sillenceSoft.schedulleCall.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import sillenceSoft.schedulleCall.Dto.ScheduleRequestDto;

import sillenceSoft.schedulleCall.Service.JWTProvider;
import sillenceSoft.schedulleCall.Service.ScheduleService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final JWTProvider jwtProvider;
    private final ScheduleService scheduleService;

    @GetMapping(value = "/schedule")
    public ResponseEntity getMySchedule (Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.getMySchedule(userNo);
    }

    @PostMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSchedule (Authentication authentication, @RequestBody ScheduleRequestDto schedule) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.addSchedule(userNo, schedule);

    }

    @PutMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSchedule (Authentication authentication, @RequestBody ScheduleRequestDto schedule) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.updateSchedule(userNo, schedule);

    }

    @DeleteMapping(value = "schedule")
    public ResponseEntity deleteSchedule (Authentication authentication,@RequestParam Long scheduleNo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.deleteSchedule(userNo, scheduleNo);
    }

    @GetMapping("/schedule/others")
    public ResponseEntity getOthersSchedule (Authentication authentication, @RequestParam(name = "phone") String phone) throws IOException {
        Long thisUserNo = jwtProvider.getUserNo(authentication);
        return scheduleService.getOthersSchedule(thisUserNo, phone);
    }

    @PostMapping("/schedule/status") //스케줄 상태로 표시
    public ResponseEntity toScheduleStatus (Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.toScheduleStatus(userNo);
    }

    @DeleteMapping("/schedule/status") //스케줄 상태로 표시 해제
    public ResponseEntity cancelScheduleStatus (Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.cancelScheduleStatus(userNo);
    }
}
