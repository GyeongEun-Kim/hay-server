package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.StatusDto;
import sillenceSoft.schedulleCall.Dto.StatusResponseDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface StatusRepository  {

    List<Map<String,Object>> getAllStatus (@Param("userNo") Long userNo);
    Long addStatus (@Param("statusDto")StatusDto statusDto);
    void deleteStatus (@Param("statusNo") Long statusNo);
    void updateStatus (@Param("status") String status,@Param("statusNo") Long StatusNo, @Param("modDt")LocalDateTime modDt);

    List<StatusResponseDto> getAllOthersStatus(String phone);
    Long checkIfPresent (@Param(value = "userNo") Long userNo, @Param(value = "status") String status);
    boolean checkIsFromSchedule (Long statusNo);
}
