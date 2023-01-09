package sillenceSoft.schedulleCall.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusDto {
    private Integer userNo;
    private Integer statusNo;
    private String status;
    private LocalDateTime modDt;
    private boolean isFromSchedule;
}
