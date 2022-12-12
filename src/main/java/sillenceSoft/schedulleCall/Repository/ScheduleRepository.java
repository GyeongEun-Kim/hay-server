package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sillenceSoft.schedulleCall.Dto.ScheduleDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleRepository {
    List<Map<String,Object>> getSchedule (Integer UserNo);
    void deleteSchedule (@Param(value = "userNo") Integer userNo,@Param(value = "scheduleNo") Integer scheduleNo);
    void addSchedule (@Param(value = "userNo") Integer userNo, @Param(value = "schedule") ScheduleDto schedule);

}
