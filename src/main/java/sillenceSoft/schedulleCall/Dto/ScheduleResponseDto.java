package sillenceSoft.schedulleCall.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleResponseDto {
    private Long scheduleNo;
    private String status;
    private Long statusNo;
    private int week;
    private StartTime startTime;
    private EndTime endTime;
    private LocalDateTime modDt;




}
