package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Dto.ScheduleDto;
import sillenceSoft.schedulleCall.Dto.StartTime;
import sillenceSoft.schedulleCall.Repository.ScheduleContentRepository;
import sillenceSoft.schedulleCall.Repository.ScheduleRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleContentRepository scheduleContentRepository;


    public List<ScheduleDto> getMySchedule(Integer userNo) {
        List<Map<String, Object>> schedule = scheduleRepository.getSchedule(userNo);
        List<ScheduleDto> results = new ArrayList<ScheduleDto>();
        for (Map<String, Object> s : schedule) {
            results.add(new ScheduleDto((long) s.get("scheduleNo"), (int) s.get("week"), (String) s.get("status"),
                    (int) s.get("startHour"), (int) s.get("startMinute"), (int) s.get("endHour"), (int) s.get("endMinute"),
                    ((Timestamp)s.get("modDt")).toLocalDateTime()));
        }
        return results;

    }

    public void addSchedule(Integer userNo, ScheduleDto schedule) {
        scheduleRepository.addSchedule(userNo, schedule);
    }

    public void deleteSchedule(Integer userNo, Integer scheduleNo) {
        scheduleRepository.deleteSchedule(userNo, scheduleNo);
    }
}


