package sillenceSoft.schedulleCall.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Getter
public class AllStatus {
    Integer StatusState;
    List<Map<String,Object>> allStatus;
}
