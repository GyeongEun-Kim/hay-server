package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.ScheduleDto;
import sillenceSoft.schedulleCall.Repository.ScheduleContentRepository;
import sillenceSoft.schedulleCall.Repository.ScheduleRepository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleContentRepository scheduleContentRepository;


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
}


