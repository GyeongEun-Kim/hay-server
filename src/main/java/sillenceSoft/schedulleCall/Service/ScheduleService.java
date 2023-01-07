package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.ScheduleDto;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Repository.AccessRepository;
import sillenceSoft.schedulleCall.Repository.ScheduleRepository;
import sillenceSoft.schedulleCall.Repository.StatusRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final AccessRepository accessRepository;
    private final StatusRepository statusRepository;
    private final SHA_256 sha256;
    private final UserService userService;


    public Object getMySchedule(Integer userNo) {
        try {
            List<Map<String, Object>> schedule = scheduleRepository.getSchedule(userNo);
            List<ScheduleDto> results = new ArrayList<ScheduleDto>();
            for (Map<String, Object> s : schedule) {
                results.add(new ScheduleDto((long) s.get("scheduleNo"), (int) s.get("week"), (String) s.get("status"),
                        (int) s.get("startHour"), (int) s.get("startMinute"), (int) s.get("endHour"), (int) s.get("endMinute"),
                        ((Timestamp) s.get("modDt")).toLocalDateTime()));
            }
            return results;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "fail to get schedule";
        }


    }

    public String addSchedule(Integer userNo, ScheduleDto schedule) {
        String msg;
        if (statusRepository.checkIfPresent(userNo, schedule.getStatus())==0) {
            statusRepository.addStatus(StatusDto.builder()              //status에 없는 상태면 추가하기
                    .userNo(userNo)
                    .status(schedule.getStatus())
                    .modDt(LocalDateTime.now())
                    .build());
        }
        try {
            scheduleRepository.addSchedule(userNo, schedule);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to add schedule";
        }
        return msg;
    }

    public String deleteSchedule(Integer userNo, Integer scheduleNo) {
        String msg;
        try {
            scheduleRepository.deleteSchedule(userNo, scheduleNo);
            msg="success";
        }
        catch (Exception e) {
            e.printStackTrace();
            msg="fail to delete schedule";
        }
        return msg;
        }

        public Object getOthersSchedule (Integer userNo, String phone, HttpServletResponse res) throws IOException {
            Integer check = accessRepository.checkAccessOrNot(sha256.encrypt( phone), userNo);
            System.out.println("userNo = "+check);

            if (check != null) {
                List<Map<String,Object>> mySchedule = (List<Map<String,Object>>)getMySchedule(check);
                if (mySchedule.size()==0) {
                    res.sendError(404,"사용자의 현재 스케줄이 존재하지 않습니다");
                    return null;
                }
                else return mySchedule;
            } else { //접근 자체가 불가할때
                res.sendError(404,"해당 사용자의 스케줄을 볼 수 없습니다");//숨김 당해서 안보이는거지만 그냥 상태가 없다고 출력함.
                return null;
            }
        }

        public void toScheduleStatus(Integer userNo) {
            LocalDateTime date = LocalDateTime.now();
            DayOfWeek dayOfWeek = date.getDayOfWeek();//요일 (월요일1, 일요일 7)
            int hour = date.getHour();
            int minute = date.getMinute();
            int week = dayOfWeek.getValue();
            if (week == 7 ) week= 0;
            Integer statusNo = scheduleRepository.getScheduleStatus(userNo, week, hour, minute );
            userService.setNowStatus(userNo, statusNo);

        }

        public void cancelScheduleStatus (Integer userNo) {
            userService.cancelNowStatus(userNo);
        }
}


