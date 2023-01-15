package sillenceSoft.schedulleCall.Dto;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ScheduleRequestDto {
    private long scheduleNo;
    private int week;
    private String status;
    private StartTime startTime;
    private EndTime endTime;
    private LocalDateTime modDt;
    private Integer StatusNo;


    public ScheduleRequestDto() {
        this.modDt= LocalDateTime.now();
    }

    public ScheduleRequestDto(long scheduleNo, int week, String status, int startHour, int startMinute,
                              int endHour, int endMinute, LocalDateTime modDt) {
        this.scheduleNo=scheduleNo;
        this.week=week;
        this.status=status;
        this.modDt=modDt;
        this.startTime=new StartTime(startHour, startMinute);
        this.endTime= new EndTime(endHour, endMinute);
    }
}
