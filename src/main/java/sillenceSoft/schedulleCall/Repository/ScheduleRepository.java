package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sillenceSoft.schedulleCall.Dto.ScheduleRequestDto;
import sillenceSoft.schedulleCall.Dto.ScheduleResponseDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleRepository {
    List<ScheduleResponseDto> getSchedule (Integer UserNo);
    void deleteSchedule (@Param(value = "userNo") Integer userNo,@Param(value = "scheduleNo") Integer scheduleNo);
    void addSchedule (@Param(value = "userNo") Integer userNo, @Param(value = "schedule") ScheduleRequestDto schedule);
    Integer getScheduleStatusNo(@Param(value = "userNo")Integer userNo, @Param(value = "week") Integer week,
            @Param(value = "hour")Integer hour, @Param(value = "minute")Integer minute);
}
