package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.apache.ibatis.jdbc.Null;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.*;
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


    public ResponseEntity getMySchedule(Long userNo) {
        try {
            List<ScheduleResponseDto> schedule = scheduleRepository.getSchedule(userNo);
            return new ResponseEntity(schedule,HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity addSchedule(Long userNo, ScheduleRequestDto schedule) {
        try {
            StatusDto statusDto = StatusDto.builder()
                    .userNo(userNo)
                    .status(schedule.getStatus())
                    .modDt(LocalDateTime.now())
                    .isFromSchedule(true)
                    .build();

            statusRepository.addStatus(statusDto); //상태글 추가

            schedule.setStatusNo(statusDto.getStatusNo());
            scheduleRepository.addSchedule(userNo, schedule);
            return new ResponseEntity(schedule, HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity deleteSchedule(Long userNo, Long scheduleNo) {
        try {
            Long statusNo = scheduleRepository.getStatusNo(scheduleNo);
           // scheduleRepository.deleteSchedule(userNo, scheduleNo); cascade로 자동삭제됨
            if(statusNo==null) {
                return new ResponseEntity("삭제할 스케줄이 없습니다", HttpStatus.NO_CONTENT);
            }
            else {
                statusRepository.deleteStatus(statusNo);
                return new ResponseEntity("success", HttpStatus.OK);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

        public ResponseEntity getOthersSchedule (Long userNo, String accessUserPhone) throws IOException {
            //userNo가 accessUserPhone의 스케줄을 보려고 하는상태
            String encryptedAccessUserPhone = sha256.encrypt(accessUserPhone);
            String encryptedUserPhone = userRepository.getPhoneByUserNo(userNo);

            UserDto accessUserDto = userRepository.getUserDtoByPhone(encryptedAccessUserPhone);
            if(accessUserDto==null) {
                return new ResponseEntity("가입하지 않은 사용자입니다.",HttpStatus.NO_CONTENT);
            }
            Long check = accessRepository.checkAccessOrNot(accessUserDto.getUserNo(),encryptedUserPhone );
            boolean statusOn = accessUserDto.isStatusOn();

            if (check ==1 && statusOn==true) {
                ResponseEntity schedule = getMySchedule(accessUserDto.getUserNo());
                return schedule;
            } else { //접근 자체가 불가할때
                return new ResponseEntity("해당 사용자의 스케줄을 볼 수 없습니다",HttpStatus.NO_CONTENT);
            }
        }

        public ResponseEntity toScheduleStatus(Long userNo) {
            LocalDateTime date = LocalDateTime.now();
            DayOfWeek dayOfWeek = date.getDayOfWeek(); //요일 (월요일1, 일요일 7)
            int hour = date.getHour();
            int minute = date.getMinute();
            int week = dayOfWeek.getValue();
            if (week == 7 ) week= 0;
            try {
                Long statusNo = scheduleRepository.getScheduleStatusNo(userNo, week, hour, minute );
                userRepository.setStatusState(userNo, "1");
                userService.setScheduleNowStatus(userNo, statusNo);

                return new ResponseEntity("success",HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }



        public ResponseEntity cancelScheduleStatus (Long userNo) {
            //user  statusState 값 변경.
            try {
                UserDto userDto = userRepository.getUserDto(userNo);
               // System.out.println("userDto.isStatusOn() = " + userDto.isStatusOn());
                userDto.setStatusState("0"); //스케줄 상태 해제
                userDto.setStatusNo(0L); //현재 상태글 해제
                userRepository.updateUserDto(userDto);

                return new ResponseEntity("success", HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(),HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }

        public ResponseEntity updateSchedule (Long userNo, ScheduleRequestDto schedule) {
            try {
                Long statusNo = scheduleRepository.getStatusNo(schedule.getScheduleNo());
                /*
               만약 보내주시는 json에 statusNo가 같이오면 위에 코드는 필요없음~
                 */
                if (statusNo==null) return new ResponseEntity("수정할 스케줄이 존재하지 않습니다", HttpStatus.NO_CONTENT);


                statusRepository.updateStatus(schedule.getStatus(), statusNo,LocalDateTime.now());
                schedule.setStatusNo(statusNo);
                //상태글 업데이트
                scheduleRepository.updateSchedule(schedule);

                return new ResponseEntity(schedule, HttpStatus.OK);
            }
            catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
}


