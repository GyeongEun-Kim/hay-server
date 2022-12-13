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
import java.util.List;

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
}
