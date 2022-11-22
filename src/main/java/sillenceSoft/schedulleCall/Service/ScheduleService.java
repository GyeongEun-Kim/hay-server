package sillenceSoft.schedulleCall.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sillenceSoft.schedulleCall.Repository.ScheduleContentRepository;
import sillenceSoft.schedulleCall.Repository.ScheduleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final ScheduleContentRepository scheduleContentRepository;

    public List<String> getStatus () {
       // List<String> status = scheduleRepository.getStatus();
        return null;
    }

    public String getSchedule() {
        return "";
    }
}
