package sillenceSoft.schedulleCall.Repository;

import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import sillenceSoft.schedulleCall.Dto.StatusDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Mapper
public interface StatusRepository  {

    List<Map<String,String>> getAllStatus (@Param("userNo") int userNo);
    void addStatus (@Param("statusDto")StatusDto statusDto);
    void deleteStatus (@Param("statusNo") int statusNo);
    void updateStatus (@Param("status") String status,@Param("statusNo") int StatusNo, @Param("modDt")LocalDateTime modDt);
    String getNowStatus (Integer userNo);

}
