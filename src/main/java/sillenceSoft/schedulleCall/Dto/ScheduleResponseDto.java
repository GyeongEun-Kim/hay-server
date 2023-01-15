package sillenceSoft.schedulleCall.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ScheduleResponseDto {
    private long scheduleNo;
    private Integer StatusNo;
    private Integer week;
    private StartTime startTime;
    private EndTime endTime;
    private LocalDateTime modDt;

}
