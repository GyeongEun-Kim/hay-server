package sillenceSoft.schedulleCall.Dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
@Builder
@Setter
@NoArgsConstructor
public class AllStatus {
    private String StatusState;
    private List<Map<String,Object>> allStatus;
}
