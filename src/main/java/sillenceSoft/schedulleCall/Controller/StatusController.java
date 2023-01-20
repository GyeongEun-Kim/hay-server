package sillenceSoft.schedulleCall.Controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Slf4j
public class StatusController {
    private final ScheduleService scheduleService;
    private final StatusService statusService;
    private final JWTProvider jwtProvider;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = "/status")
    public ResponseEntity getAllStatus (HttpServletRequest request,Authentication authentication) {
        Long userNo = jwtProvider.getUserNo(authentication);

        ResponseEntity responseEntity = statusService.getAllStatus(userNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @PostMapping("/status")
    public ResponseEntity addStatus (HttpServletRequest request, Authentication authentication, @RequestParam(name = "status") String newStatusMemo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = statusService.addStatus(userNo, newStatusMemo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }

    @DeleteMapping("/status")
    public ResponseEntity deleteStatus (HttpServletRequest request, Authentication authentication, @RequestParam(name = "statusNo") Long statusNo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = statusService.deleteStatus(statusNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }

    @PutMapping("/status")
    public ResponseEntity updateStatus (HttpServletRequest request, Authentication authentication, @RequestParam(name = "status") String status,
                                @RequestParam(name = "statusNo") Long statusNo) {
        Long userNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity = statusService.updateStatus(userNo, status, statusNo);
        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;

    }


    @GetMapping ("/status/others")
    public ResponseEntity getOthersStatus (HttpServletRequest request, Authentication authentication, @RequestParam(name = "phone", required = false) String phone) throws IOException {
        Long thisUserNo = jwtProvider.getUserNo(authentication);
        ResponseEntity responseEntity;
        if (phone!=null) //한명의 상태글 조회

            responseEntity = statusService.getOthersStatus(thisUserNo, phone);

         else //내가 볼수 있는 모든 사람의 상태글 조회

            responseEntity = statusService.getAllOthersStatus(thisUserNo);

        logger.info(request.getRequestURI()+" Http Status: "+responseEntity.getStatusCode()+" "+responseEntity.getStatusCodeValue());
        return responseEntity;
    }




}
