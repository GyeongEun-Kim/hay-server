package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import sillenceSoft.schedulleCall.Dto.ScheduleRequestDto;
import sillenceSoft.schedulleCall.Dto.ScheduleResponseDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ScheduleRepository {
    List<ScheduleResponseDto> getSchedule (Long UserNo);

    void addSchedule (@Param(value = "userNo") Long userNo, @Param(value = "schedule") ScheduleRequestDto schedule);

    Long getScheduleStatusNo(@Param(value = "userNo")Long userNo, @Param(value = "week") Integer week,
            @Param(value = "hour")Integer hour, @Param(value = "minute")Integer minute);

    Long getStatusNo(Long scheduleNo);

    void updateSchedule (@Param(value = "schedule") ScheduleRequestDto schedule);
    ScheduleResponseDto getScheduleDto (Long scheduleNo);
}
