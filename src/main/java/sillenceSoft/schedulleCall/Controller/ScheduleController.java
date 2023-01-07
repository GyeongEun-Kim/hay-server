package sillenceSoft.schedulleCall.Controller;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.json.JSONException;
import org.json.JSONML;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import sillenceSoft.schedulleCall.Dto.ScheduleDto;

import sillenceSoft.schedulleCall.Service.JWTProvider;
import sillenceSoft.schedulleCall.Service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final JWTProvider jwtProvider;
    private final ScheduleService scheduleService;

    @GetMapping(value = "/schedule")
    public Object getMySchedule (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.getMySchedule(userNo);
    }

    @PostMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String addSchedule (Authentication authentication, @RequestBody ScheduleDto schedule) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.addSchedule(userNo, schedule);

    }

    @DeleteMapping(value = "schedule")
    public String deleteSchedule (Authentication authentication,@RequestParam Integer scheduleNo) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        return scheduleService.deleteSchedule(userNo, scheduleNo);
    }

    @GetMapping("/schedule/others")
    public Object getOthersSchedule (Authentication authentication, @RequestParam(name = "phone") String phone
                                                       ,HttpServletResponse res) throws IOException {
        Integer thisUserNo = jwtProvider.getUserNo(authentication);
        return scheduleService.getOthersSchedule(thisUserNo, phone, res);
    }

    @PostMapping("/schedule/status") //스케줄 상태로 표시
    public void toScheduleStatus (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        scheduleService.toScheduleStatus(userNo);
    }

    @DeleteMapping("/schedule/status") //스케줄 상태로 표시 해제
    public void cancelScheduleStatus (Authentication authentication) {
        Integer userNo = jwtProvider.getUserNo(authentication);
        scheduleService.cancelScheduleStatus(userNo);
    }
}
