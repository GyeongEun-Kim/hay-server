package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScheduleRepository {
    String getSchedule (Integer UserNo);
    List<String> getStatus (Integer UserNo);
}
