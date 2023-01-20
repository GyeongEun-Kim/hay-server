package sillenceSoft.schedulleCall.Controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import sillenceSoft.schedulleCall.Dto.ScheduleRequestDto;

import sillenceSoft.schedulleCall.Service.JWTProvider;
import sillenceSoft.schedulleCall.Service.ScheduleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ScheduleController {

    private final JWTProvider jwtProvider;
    private final ScheduleService scheduleService;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/schedule")
    public ResponseEntity getMySchedule (HttpServletRequest request, Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.getMySchedule(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @PostMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSchedule (HttpServletRequest request,
                                       Authentication authentication, @RequestBody ScheduleRequestDto schedule) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.addSchedule(userNo, schedule);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }

    @PutMapping(value = "/schedule", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateSchedule (HttpServletRequest request, Authentication authentication, @RequestBody ScheduleRequestDto schedule) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.updateSchedule(userNo, schedule);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }

    @DeleteMapping(value = "schedule")
    public ResponseEntity deleteSchedule (HttpServletRequest request, Authentication authentication,@RequestParam Long scheduleNo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.deleteSchedule(userNo, scheduleNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @GetMapping("/schedule/others")
    public ResponseEntity getOthersSchedule (HttpServletRequest request,Authentication authentication, @RequestParam(name = "phone") String phone) throws IOException {
        Long thisUserNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.getOthersSchedule(thisUserNo, phone);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @PostMapping("/schedule/status") //스케줄 상태로 표시
    public ResponseEntity toScheduleStatus (HttpServletRequest request, Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.toScheduleStatus(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @DeleteMapping("/schedule/status") //스케줄 상태로 표시 해제
    public ResponseEntity cancelScheduleStatus (HttpServletRequest request, Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = scheduleService.cancelScheduleStatus(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }
}
