package sillenceSoft.schedulleCall.Dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusDto {
    private int userNo;
    private int statusNo;
    private String status;
    private LocalDateTime modDt;
}
