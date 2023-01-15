package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.ScheduleRequestDto;
import sillenceSoft.schedulleCall.Dto.ScheduleResponseDto;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Dto.UserRequestDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.ScheduleRepository;
import sillenceSoft.schedulleCall.Repository.StatusRepository;
import sillenceSoft.schedulleCall.Repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AccessRepository accessRepository;
    private final StatusRepository statusRepository;
    private final SHA_256 sha256;
    private final UserService userService;
    private final UserRepository userRepository;


    public ResponseEntity getMySchedule(Integer userNo) {
        try {
            List<ScheduleResponseDto> schedule = scheduleRepository.getSchedule(userNo);
            return new ResponseEntity(schedule,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addSchedule(Integer userNo, ScheduleRequestDto schedule) {
        try {
            StatusDto statusDto = StatusDto.builder()
                    .userNo(userNo)
                    .status(schedule.getStatus())
                    .modDt(LocalDateTime.now())
                    .isFromSchedule(true)
                    .build();

            statusRepository.addStatus(statusDto);

            schedule.setStatusNo(statusDto.getStatusNo());
            scheduleRepository.addSchedule(userNo, schedule);
            return new ResponseEntity("success", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteSchedule(Integer userNo, Integer scheduleNo) {
        try {
            scheduleRepository.deleteSchedule(userNo, scheduleNo);
            return new ResponseEntity("success", HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

        public ResponseEntity getOthersSchedule (Integer userNo, String phone) throws IOException {
            Integer check = accessRepository.checkAccessOrNot(sha256.encrypt( phone), userNo);
            //check = 접근할수 ㅣㅆ는 userNo

            if (check != null) {
                List<ScheduleResponseDto> schedule =(List<ScheduleResponseDto>) getMySchedule(check);
                if (schedule.size()==0) {
                    return new ResponseEntity("사용자의 현재 스케줄이 존재하지 않습니다",HttpStatus.NO_CONTENT);
                }
                else return new ResponseEntity(schedule, HttpStatus.OK);
            } else { //접근 자체가 불가할때
                return new ResponseEntity("해당 사용자의 스케줄을 볼 수 없습니다",HttpStatus.NO_CONTENT);
            }
        }

        public ResponseEntity toScheduleStatus(Integer userNo) {
            LocalDateTime date = LocalDateTime.now();
            DayOfWeek dayOfWeek = date.getDayOfWeek();//요일 (월요일1, 일요일 7)
            int hour = date.getHour();
            int minute = date.getMinute();
            int week = dayOfWeek.getValue();
            if (week == 7 ) week= 0;
            try {
                Integer statusNo = scheduleRepository.getScheduleStatusNo(userNo, week, hour, minute );
                if (statusNo!= null)
                    userService.setNowStatus(userNo, statusNo);
                userRepository.setStatusState(userNo);
                return new ResponseEntity("success",HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }


        }

        public ResponseEntity cancelScheduleStatus (Integer userNo) {
            //user Repo statusState 값 변경.
            try {
                userRepository.cancelStatusState(userNo); //스케줄상태 해제
                //만약 현재상태가 스케줄의 상태글로 지정돼있으면 그것또한 해제
                Integer nowStatus = userRepository.getNowStatus(userNo);
                if (nowStatus!=null && statusRepository.checkIsFromSchedule(nowStatus)) {
                    userRepository.cancelNowStatus(userNo);
                }
                return new ResponseEntity("success", HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
}


