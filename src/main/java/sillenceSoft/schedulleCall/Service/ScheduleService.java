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


    public String getMySchedule(String id) {

        return "";
    }
}
